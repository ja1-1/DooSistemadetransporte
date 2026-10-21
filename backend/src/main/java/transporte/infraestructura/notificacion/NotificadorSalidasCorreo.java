package transporte.infraestructura.notificacion;

import transporte.dominio.SalidaDominio;
import transporte.dominio.consulta.FiltroUsuario;
import transporte.dominio.enums.Rol;
import transporte.dominio.puerto.NotificadorSalidas;
import transporte.dominio.repositorio.AsignacionRepositorio;
import transporte.dominio.repositorio.UsuarioRepositorio;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

@Component
public class NotificadorSalidasCorreo implements NotificadorSalidas {
    private static final Logger LOG = LoggerFactory.getLogger(NotificadorSalidasCorreo.class);
    private final JavaMailSender correo;
    private final AsignacionRepositorio asignaciones;
    private final UsuarioRepositorio usuarios;
    private final String remitente;

    public NotificadorSalidasCorreo(
            JavaMailSender javaMailSender,
            AsignacionRepositorio repositorioAsignacion,
            UsuarioRepositorio repositorioUsuario,
            @Value("${transporte.correo.remitente}") String textoRecibido) {
        correo = javaMailSender;
        asignaciones = repositorioAsignacion;
        usuarios = repositorioUsuario;
        remitente = textoRecibido;
    }

    @Override
    public void notificarCancelacion(SalidaDominio salida) {
        publicar(
                salida,
                "Salida cancelada",
                "Se canceló la salida " + salida.getId() + " de " + salida.getNombreRuta() + ".");
    }

    @Override
    public void notificarReprogramacion(SalidaDominio salida, LocalDateTime instanteAnterior) {
        publicar(
                salida,
                "Salida reprogramada",
                "Salida "
                        + salida.getId()
                        + " de "
                        + salida.getNombreRuta()
                        + ". Salida anterior: "
                        + instanteAnterior
                        + ". Nueva ventana: "
                        + salida.getVentana()
                        + ".");
    }

    private void publicar(SalidaDominio salida, String asunto, String texto) {
        try {
            var asignacion = asignaciones.buscarPorId(salida.getAsignacionId()).orElseThrow();
            var destinatarios =
                    usuarios.relacionados(
                            FiltroUsuario.todos().conConductorId(asignacion.getConductorId()));
            if (destinatarios.isEmpty()) {
                destinatarios =
                        usuarios.relacionados(FiltroUsuario.todos().conRol(Rol.JEFE_DE_OPERACION));
            }
            String[] direcciones =
                    destinatarios.stream()
                            .map(usuario -> usuario.getCorreo().valor())
                            .toArray(String[]::new);
            var mensaje = new SimpleMailMessage();
            mensaje.setFrom(remitente);
            mensaje.setTo(direcciones);
            mensaje.setSubject(asunto);
            mensaje.setText(texto);
            Runnable enviar =
                    () -> {
                        try {
                            if (direcciones.length == 0) {
                                throw new IllegalStateException(
                                        CatalogoMensajes.NO_HAY_DESTINATARIO_PARA_EL_AVISO);
                            }
                            correo.send(mensaje);
                        } catch (RuntimeException error) {
                            LOG.warn(
                                    CatalogoMensajes.NO_SE_PUDO_ENVIAR_EL_AVISO_DE,
                                    salida.getId(),
                                    error.getClass().getSimpleName());
                        }
                    };
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationManager.registerSynchronization(
                        new TransactionSynchronization() {
                            @Override
                            public void afterCommit() {
                                enviar.run();
                            }
                        });
            } else {
                enviar.run();
            }
        } catch (RuntimeException error) {
            LOG.warn(
                    CatalogoMensajes.NO_SE_PUDO_PREPARAR_EL_AVISO_DE,
                    salida.getId(),
                    error.getClass().getSimpleName());
        }
    }
}
