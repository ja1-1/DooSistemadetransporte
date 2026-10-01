package transporte.dto;

import transporte.dominio.enums.EstadoAsignacion;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.UUID;

public record AsignacionDTO(
        UUID id,
        UUID conductorId,
        UUID vehiculoId,
        LocalDate fechaInicio,
        @Nullable LocalDate fechaFin,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable EstadoAsignacion estado)
        implements VistaDTO {
    public AsignacionDTO conEstado(EstadoAsignacion valor) {
        return new AsignacionDTO(id, conductorId, vehiculoId, fechaInicio, fechaFin, valor);
    }
}
