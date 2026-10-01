package transporte.dominio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.opcional;
import static transporte.transversal.utilitarios.Validacion.requerido;
import static transporte.transversal.utilitarios.Validacion.texto;

import transporte.dominio.enums.Rol;
import transporte.dominio.puerto.CifradorContrasena;
import transporte.dominio.valor.Cedula;
import transporte.dominio.valor.Correo;
import transporte.dominio.valor.HashContrasena;
import transporte.dominio.valor.Telefono;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public final class UsuarioDominio {
    private final UUID id;
    private final Cedula cedula;
    private final Correo correo;
    private String primerNombre, segundoNombre, primerApellido, segundoApellido;
    private @Nullable Telefono numeroTelefono;
    private HashContrasena hashContrasena;
    private boolean debeCambiarContrasena, activo;
    private Rol rol;
    private @Nullable UUID conductorId;

    public UsuarioDominio(
            UUID id,
            String primerNombre,
            @Nullable String segundoNombre,
            String primerApellido,
            @Nullable String segundoApellido,
            Cedula cedula,
            Correo correo,
            @Nullable Telefono numeroTelefono,
            HashContrasena hashContrasena,
            boolean debeCambiarContrasena,
            Rol rol,
            @Nullable UUID conductorId,
            boolean activo) {
        this.id = requerido(id, "id");
        this.cedula = requerido(cedula, "cedula");
        this.correo = requerido(correo, "correo");
        this.hashContrasena = requerido(hashContrasena, "hashContrasena");
        datos(primerNombre, segundoNombre, primerApellido, segundoApellido, numeroTelefono);
        requerido(rol, "rol");
        if ((rol == Rol.CONDUCTOR) != (conductorId != null)) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.EL_ROL_CONDUCTOR_REQUIERE_CONDUCTOR_ASOCIADO_Y);
        }
        this.rol = rol;
        this.conductorId = conductorId;
        this.debeCambiarContrasena = debeCambiarContrasena;
        this.activo = activo;
    }

    private void datos(
            String primerNombre,
            @Nullable String segundoNombre,
            String primerApellido,
            @Nullable String segundoApellido,
            @Nullable Telefono telefono) {
        String primerNombreValido = texto(primerNombre, "primerNombre", 1, 40),
                segundoNombreValido = opcional(segundoNombre, "segundoNombre", 40),
                primerApellidoValido = texto(primerApellido, "primerApellido", 1, 40),
                segundoApellidoValido = opcional(segundoApellido, "segundoApellido", 40);
        this.primerNombre = primerNombreValido;
        this.segundoNombre = segundoNombreValido;
        this.primerApellido = primerApellidoValido;
        this.segundoApellido = segundoApellidoValido;
        numeroTelefono = telefono;
    }

    public static UsuarioDominio registrar(
            String primerNombre,
            Optional<String> segundoNombre,
            String primerApellido,
            Optional<String> segundoApellido,
            Cedula cedula,
            Correo correo,
            Optional<Telefono> numeroTelefono,
            Rol rol,
            Optional<ConductorDominio> conductor,
            String contrasenaTemporal,
            CifradorContrasena cifrador) {
        temporal(contrasenaTemporal);
        var usuario =
                new UsuarioDominio(
                        UUID.randomUUID(),
                        primerNombre,
                        opcional(segundoNombre, "segundoNombre").orElse(""),
                        primerApellido,
                        opcional(segundoApellido, "segundoApellido").orElse(""),
                        cedula,
                        correo,
                        opcional(numeroTelefono, "numeroTelefono").orElse(null),
                        requerido(cifrador, "cifrador").cifrar(contrasenaTemporal),
                        true,
                        rol,
                        opcional(conductor, "conductor").map(ConductorDominio::getId).orElse(null),
                        true);
        usuario.modificar(rol, conductor);
        return usuario;
    }

    private static void temporal(String textoRecibido) {
        if (requerido(textoRecibido, "contrasenaTemporal").codePointCount(0, textoRecibido.length())
                != 12) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.LA_CONTRASENA_TEMPORAL_DEBE_TENER_12_CARACTERES);
        }
    }

    public void autenticar(String contrasena, CifradorContrasena cifrador, LocalDateTime ahora) {
        requerido(ahora, "ahora");
        estado(activo, CatalogoMensajes.LAS_CREDENCIALES_NO_SON_VALIDAS);
        if (!requerido(cifrador, "cifrador").coincide(contrasena, hashContrasena)) {
            throw new IllegalArgumentException(CatalogoMensajes.LAS_CREDENCIALES_NO_SON_VALIDAS);
        }
    }

    public void cambiarContrasena(
            String contrasenaActual, String contrasenaNueva, CifradorContrasena cifrador) {
        estado(activo, CatalogoMensajes.EL_USUARIO_DEBE_ESTAR_ACTIVO);
        if (!requerido(cifrador, "cifrador").coincide(contrasenaActual, hashContrasena)) {
            throw new IllegalArgumentException(CatalogoMensajes.LA_CONTRASENA_ACTUAL_NO_ES_VALIDA);
        }
        if (requerido(contrasenaNueva, "contrasenaNueva").codePointCount(0, contrasenaNueva.length()) < 8) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.LA_CONTRASENA_NUEVA_DEBE_TENER_AL_MENOS);
        }
        hashContrasena = cifrador.cifrar(contrasenaNueva);
        debeCambiarContrasena = false;
    }

    public void restablecerContrasena(String contrasenaTemporal, CifradorContrasena cifrador) {
        estado(activo, CatalogoMensajes.EL_USUARIO_DEBE_ESTAR_ACTIVO);
        temporal(contrasenaTemporal);
        hashContrasena = requerido(cifrador, "cifrador").cifrar(contrasenaTemporal);
        debeCambiarContrasena = true;
    }

    public void modificar(Rol rol, Optional<ConductorDominio> conductor) {
        requerido(rol, "rol");
        opcional(conductor, "conductor");
        estado(
                (rol == Rol.CONDUCTOR) == conductor.isPresent(),
                CatalogoMensajes.EL_CONDUCTOR_ASOCIADO_NO_CORRESPONDE_AL_ROL);
        estado(
                conductor.isEmpty() || conductor.get().getCedula().equals(cedula),
                CatalogoMensajes.LA_CEDULA_DEBE_COINCIDIR_CON_LA_DEL);
        this.rol = rol;
        conductorId = conductor.map(ConductorDominio::getId).orElse(null);
    }

    public void modificarInformacionPropia(
            String primerNombre,
            Optional<String> segundoNombre,
            String primerApellido,
            Optional<String> segundoApellido,
            Optional<Telefono> numeroTelefono) {
        datos(
                primerNombre,
                opcional(segundoNombre, "segundoNombre").orElse(""),
                primerApellido,
                opcional(segundoApellido, "segundoApellido").orElse(""),
                opcional(numeroTelefono, "numeroTelefono").orElse(null));
    }

    public void desactivar() {
        estado(activo, CatalogoMensajes.EL_USUARIO_DEBE_ESTAR_ACTIVO);
        activo = false;
    }

    public void reactivar() {
        estado(!activo, CatalogoMensajes.EL_USUARIO_DEBE_ESTAR_INACTIVO);
        activo = true;
    }

    public UUID getId() {
        return id;
    }

    public Cedula getCedula() {
        return cedula;
    }

    public Correo getCorreo() {
        return correo;
    }

    public HashContrasena getHashContrasena() {
        return hashContrasena;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public Optional<String> getSegundoNombre() {
        return segundoNombre.isEmpty() ? Optional.empty() : Optional.of(segundoNombre);
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public Optional<String> getSegundoApellido() {
        return segundoApellido.isEmpty() ? Optional.empty() : Optional.of(segundoApellido);
    }

    public Optional<Telefono> getNumeroTelefono() {
        return Optional.ofNullable(numeroTelefono);
    }

    public Rol getRol() {
        return rol;
    }

    public Optional<UUID> getConductorId() {
        return Optional.ofNullable(conductorId);
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean isDebeCambiarContrasena() {
        return debeCambiarContrasena;
    }
}
