package transporte.negocio.negocio.impl;

import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.NO_ENCONTRADO;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.Incidencia;
import transporte.dominio.SalidaDominio;
import transporte.dominio.enums.TipoIncidencia;
import transporte.dominio.puerto.NotificadorSalidas;
import transporte.dominio.repositorio.SalidaRepositorio;
import transporte.dominio.servicio.ServicioProgramacion;
import transporte.dominio.valor.Responsable;
import transporte.dominio.valor.VentanaViaje;
import transporte.negocio.negocio.AsignacionNegocio;
import transporte.negocio.negocio.RutaNegocio;
import transporte.negocio.negocio.SalidaNegocio;
import transporte.negocio.negocio.UsuarioNegocio;
import transporte.negocio.solicitud.Solicitudes.Motivo;
import transporte.negocio.solicitud.Solicitudes.ProgramarSalida;
import transporte.negocio.solicitud.Solicitudes.Reemplazar;
import transporte.negocio.solicitud.Solicitudes.Refuerzo;
import transporte.negocio.solicitud.Solicitudes.RegistrarIncidencia;
import transporte.negocio.solicitud.Solicitudes.Reprogramar;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.IdentidadDisponible;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SalidaNegocioImpl implements SalidaNegocio {
    private final SalidaRepositorio salidas;
    private final ServicioProgramacion servicioProgramacion;
    private final RutaNegocio rutas;
    private final UsuarioNegocio usuarios;
    private final NotificadorSalidas notificador;
    private final Clock reloj;
    private final AsignacionNegocio asignacionesNegocio;

    public SalidaNegocioImpl(
            SalidaRepositorio salidas,
            ServicioProgramacion servicioProgramacion,
            RutaNegocio rutas,
            UsuarioNegocio usuarios,
            NotificadorSalidas notificador,
            Clock reloj,
            AsignacionNegocio asignacionesNegocio) {
        this.salidas = salidas;
        this.servicioProgramacion = servicioProgramacion;
        this.rutas = rutas;
        this.usuarios = usuarios;
        this.notificador = notificador;
        this.reloj = reloj;
        this.asignacionesNegocio = asignacionesNegocio;
    }

    @Override
    public SalidaDominio programar(ProgramarSalida programarSalida) {
        requerido(programarSalida, "datos");
        return servicioProgramacion.programarSalida(
                rutas.consultar(programarSalida.rutaId()),
                asignacionesNegocio.consultar(programarSalida.asignacionId()),
                new VentanaViaje(
                        programarSalida.instanteSalidaProgramado(),
                        programarSalida.instanteLlegadaEstimado()),
                ahora());
    }

    @Override
    public SalidaDominio iniciar(UUID id) {
        var salida = consultar(id);
        servicioProgramacion.iniciarSalida(salida, ahora());
        return salida;
    }

    @Override
    public SalidaDominio finalizarSalida(UUID id) {
        var salida = consultar(id);
        salida.finalizar(ahora());
        salidas.guardar(salida);
        return salida;
    }

    @Override
    public SalidaDominio cancelar(UUID id, Motivo motivo, UUID actor) {
        requerido(motivo, "datos");
        var salida = consultar(id);
        salida.cancelar(motivo.motivo(), responsable(actor), ahora(), notificador);
        salidas.guardar(salida);
        return salida;
    }

    @Override
    public SalidaDominio reemplazar(UUID id, Reemplazar reemplazar, UUID actor) {
        requerido(reemplazar, "datos");
        var salida = consultar(id);
        servicioProgramacion.reemplazarAsignacion(
                salida,
                asignacionesNegocio.consultar(reemplazar.nuevaAsignacionId()),
                reemplazar.motivo(),
                responsable(actor),
                reemplazar.nuevaLlegadaEstimada(),
                ahora());
        return salida;
    }

    @Override
    public SalidaDominio reprogramar(UUID id, Reprogramar reprogramar, UUID actor) {
        requerido(reprogramar, "datos");
        var salida = consultar(id);
        servicioProgramacion.reprogramarSalida(
                salida,
                nuevaVentana(salida.getVentana(), reprogramar),
                reprogramar.motivo(),
                responsable(actor),
                ahora());
        return salida;
    }

    @Override
    public SalidaDominio refuerzo(UUID id, Refuerzo refuerzo, UUID actor) {
        requerido(refuerzo, "datos");
        return servicioProgramacion.programarRefuerzo(
                consultar(id),
                asignacionesNegocio.consultar(refuerzo.asignacionId()),
                refuerzo.instanteSalida(),
                refuerzo.motivo(),
                responsable(actor),
                ahora());
    }

    @Override
    public Incidencia incidencia(UUID id, RegistrarIncidencia registrarIncidencia, UUID actor) {
        requerido(registrarIncidencia, "datos");
        var tipoIncidencia = TipoIncidencia.desdeId(registrarIncidencia.tipoIncidenciaId());
        var responsable = responsable(actor);
        var registro =
                IdentidadDisponible.crear(
                        () -> {
                            var salida = consultar(id);
                            return new IncidenciaRegistrada(
                                    salida,
                                    salida.registrarIncidencia(
                                            tipoIncidencia,
                                            registrarIncidencia.instanteOcurrencia(),
                                            registrarIncidencia.descripcion(),
                                            responsable,
                                            ahora()));
                        },
                        candidato ->
                                salidas.buscarIncidenciaPorId(candidato.incidencia().getId())
                                        .isPresent());
        salidas.guardar(registro.salida());
        return registro.incidencia();
    }

    private record IncidenciaRegistrada(SalidaDominio salida, Incidencia incidencia) {}

    @Override
    public List<Incidencia> historial(UUID id) {
        return consultar(id).consultarHistorialIncidencias();
    }

    @Override
    public SalidaDominio consultar(UUID id) {
        return salidas.buscarPorId(requerido(id, "salidaId"))
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO, CatalogoMensajes.NO_SE_ENCONTRO_LA_SALIDA));
    }

    private static VentanaViaje nuevaVentana(VentanaViaje actual, Reprogramar reprogramar) {
        var nuevaSalida =
                requerido(reprogramar.instanteSalidaProgramado(), "instanteSalidaProgramado");
        var llegadaIndicada = reprogramar.instanteLlegadaEstimado();
        if (llegadaIndicada != null) {
            return new VentanaViaje(nuevaSalida, llegadaIndicada);
        }
        var duracion =
                Duration.between(
                        actual.instanteSalidaProgramado(), actual.instanteLlegadaEstimado());
        return new VentanaViaje(nuevaSalida, nuevaSalida.plus(duracion));
    }

    private Responsable responsable(UUID actor) {
        return Responsable.desde(usuarios.consultar(actor));
    }

    private LocalDateTime ahora() {
        return LocalDateTime.now(reloj);
    }
}
