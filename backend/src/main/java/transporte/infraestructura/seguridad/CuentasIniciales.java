package transporte.infraestructura.seguridad;

import transporte.dominio.UsuarioDominio;
import transporte.dominio.enums.Rol;
import transporte.dominio.puerto.CifradorContrasena;
import transporte.dominio.repositorio.UsuarioRepositorio;
import transporte.dominio.valor.Cedula;
import transporte.dominio.valor.Correo;
import transporte.infraestructura.persistencia.BloqueoEscritura;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

@Component
public class CuentasIniciales implements ApplicationRunner {
    public static final String CORREO_ADMINISTRADOR = "admin@transporte.local";
    public static final String CORREO_OPERACION = "operacion@transporte.local";

    private final UsuarioRepositorio usuarios;
    private final CifradorContrasena cifrador;
    private final TransactionTemplate transaccion;
    private final BloqueoEscritura unidad;
    private final String claveAdmin, claveOperacion;

    public CuentasIniciales(
            UsuarioRepositorio repositorioUsuario,
            CifradorContrasena cifradorContrasena,
            PlatformTransactionManager platformTransactionManager,
            BloqueoEscritura unidad,
            @Value("${transporte.seguridad.admin.clave}") String contrasenaAdministradorInicial,
            @Value("${transporte.seguridad.operacion.clave}") String contrasenaOperacionInicial) {
        usuarios = repositorioUsuario;
        cifrador = cifradorContrasena;
        transaccion = new TransactionTemplate(platformTransactionManager);
        this.unidad = unidad;
        claveAdmin = contrasenaAdministradorInicial;
        claveOperacion = contrasenaOperacionInicial;
    }

    @Override
    public void run(ApplicationArguments args) {
        transaccion.executeWithoutResult(
                transactionStatus -> {
                    unidad.adquirir();
                    crear(CORREO_ADMINISTRADOR, "SISTEMAADMIN", Rol.ADMINISTRADOR, claveAdmin);
                    crear(
                            CORREO_OPERACION,
                            "SISTEMAOPERACION",
                            Rol.JEFE_DE_OPERACION,
                            claveOperacion);
                });
    }

    private void crear(String correo, String cedula, Rol rol, String clave) {
        if (usuarios.buscarPorCorreo(new Correo(correo)).isPresent()) {
            return;
        }
        var usuario =
                new UsuarioDominio(
                        UUID.randomUUID(),
                        "Cuenta",
                        null,
                        rol == Rol.ADMINISTRADOR ? "Administración" : "Operación",
                        null,
                        new Cedula(cedula),
                        new Correo(correo),
                        null,
                        cifrador.cifrar(clave),
                        false,
                        rol,
                        null,
                        true);
        usuarios.guardar(usuario);
    }
}
