package transporte.dominio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.opcional;
import static transporte.transversal.utilitarios.Validacion.requerido;
import static transporte.transversal.utilitarios.Validacion.texto;

import transporte.dominio.enums.CategoriaLicencia;
import transporte.dominio.enums.TipoSangre;
import transporte.dominio.valor.Cedula;
import transporte.dominio.valor.Licencia;
import transporte.dominio.valor.Telefono;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public final class ConductorDominio {
    private final UUID id;
    private final Cedula cedula;
    private final LocalDate fechaNacimiento;
    private String primerNombre, segundoNombre, primerApellido, segundoApellido;
    private Telefono numeroTelefono;
    private TipoSangre tipoSangre;
    private Licencia licencia;
    private boolean activo;

    public ConductorDominio(
            UUID id,
            Cedula cedula,
            String primerNombre,
            @Nullable String segundoNombre,
            String primerApellido,
            @Nullable String segundoApellido,
            LocalDate fechaNacimiento,
            Telefono numeroTelefono,
            TipoSangre tipoSangre,
            Licencia licencia,
            boolean activo) {
        this.id = requerido(id, "id");
        this.cedula = requerido(cedula, "cedula");
        this.fechaNacimiento = requerido(fechaNacimiento, "fechaNacimiento");
        datos(
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                numeroTelefono,
                tipoSangre,
                licencia);
        this.activo = activo;
    }

    private void datos(
            String primerNombre,
            @Nullable String segundoNombre,
            String primerApellido,
            @Nullable String segundoApellido,
            Telefono telefono,
            TipoSangre sangre,
            Licencia nueva) {
        String primerNombreValido = texto(primerNombre, "primerNombre", 1, 40),
                segundoNombreValido = opcional(segundoNombre, "segundoNombre", 40),
                primerApellidoValido = texto(primerApellido, "primerApellido", 1, 40),
                segundoApellidoValido = opcional(segundoApellido, "segundoApellido", 40);
        requerido(telefono, "numeroTelefono");
        requerido(sangre, "tipoSangre");
        requerido(nueva, "licencia");
        this.primerNombre = primerNombreValido;
        this.segundoNombre = segundoNombreValido;
        this.primerApellido = primerApellidoValido;
        this.segundoApellido = segundoApellidoValido;
        numeroTelefono = telefono;
        tipoSangre = sangre;
        licencia = nueva;
    }

    private static void vigente(Licencia licencia, LocalDate hoy) {
        if (!requerido(licencia, "licencia").fechaVencimiento().isAfter(requerido(hoy, "hoy"))) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.LA_LICENCIA_DEBE_VENCER_DESPUES_DE_HOY);
        }
    }

    public static ConductorDominio registrar(
            Cedula cedula,
            String primerNombre,
            Optional<String> segundoNombre,
            String primerApellido,
            Optional<String> segundoApellido,
            LocalDate fechaNacimiento,
            Telefono numeroTelefono,
            TipoSangre tipoSangre,
            Licencia licencia,
            LocalDate hoy) {
        vigente(licencia, hoy);
        if (requerido(fechaNacimiento, "fechaNacimiento").isAfter(hoy.minusYears(18))) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.EL_CONDUCTOR_DEBE_SER_MAYOR_DE_EDAD);
        }
        return new ConductorDominio(
                UUID.randomUUID(),
                cedula,
                primerNombre,
                opcional(segundoNombre, "segundoNombre").orElse(""),
                primerApellido,
                opcional(segundoApellido, "segundoApellido").orElse(""),
                fechaNacimiento,
                numeroTelefono,
                tipoSangre,
                licencia,
                true);
    }

    public void modificar(
            String primerNombre,
            Optional<String> segundoNombre,
            String primerApellido,
            Optional<String> segundoApellido,
            Telefono numeroTelefono,
            TipoSangre tipoSangre,
            Licencia licencia,
            LocalDate hoy) {
        vigente(licencia, hoy);
        datos(
                primerNombre,
                opcional(segundoNombre, "segundoNombre").orElse(""),
                primerApellido,
                opcional(segundoApellido, "segundoApellido").orElse(""),
                numeroTelefono,
                tipoSangre,
                licencia);
    }

    public void desactivar() {
        estado(activo, CatalogoMensajes.EL_CONDUCTOR_DEBE_ESTAR_ACTIVO);
        activo = false;
    }

    public void reactivar() {
        estado(!activo, CatalogoMensajes.EL_CONDUCTOR_DEBE_ESTAR_INACTIVO);
        activo = true;
    }

    public boolean estaHabilitadoPara(CategoriaLicencia categoria, LocalDate fecha) {
        return activo && licencia.estaVigenteEn(fecha) && licencia.cubre(categoria);
    }

    public UUID getId() {
        return id;
    }

    public Cedula getCedula() {
        return cedula;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
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

    public Telefono getNumeroTelefono() {
        return numeroTelefono;
    }

    public TipoSangre getTipoSangre() {
        return tipoSangre;
    }

    public Licencia getLicencia() {
        return licencia;
    }

    public boolean isActivo() {
        return activo;
    }
}
