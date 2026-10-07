package transporte.entidad;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.UUID;

public record AsignacionEntidad(
        UUID id,
        UUID conductorId,
        UUID vehiculoId,
        LocalDate fechaInicio,
        @Nullable LocalDate fechaFin) {}
