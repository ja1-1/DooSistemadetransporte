package transporte.entidad;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.UUID;

public record ConductorEntidad(
        UUID id,
        String cedula,
        String primerNombre,
        @Nullable String segundoNombre,
        String primerApellido,
        @Nullable String segundoApellido,
        LocalDate fechaNacimiento,
        String numeroTelefono,
        String tipoSangre,
        String numeroLicencia,
        String categoriaLicencia,
        LocalDate fechaVencimientoLicencia,
        Boolean activo) {}
