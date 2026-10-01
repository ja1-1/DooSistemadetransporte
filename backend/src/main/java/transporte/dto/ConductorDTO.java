package transporte.dto;

import transporte.dominio.valor.Licencia;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.UUID;

public record ConductorDTO(
        UUID id,
        String cedula,
        String primerNombre,
        @Nullable String segundoNombre,
        String primerApellido,
        @Nullable String segundoApellido,
        LocalDate fechaNacimiento,
        String numeroTelefono,
        String tipoSangre,
        Licencia licencia,
        boolean activo)
        implements VistaDTO {}
