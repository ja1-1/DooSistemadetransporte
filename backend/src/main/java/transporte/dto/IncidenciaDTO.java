package transporte.dto;

import transporte.dominio.valor.Responsable;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public record IncidenciaDTO(
        UUID id,
        UUID tipoIncidenciaId,
        LocalDateTime instanteOcurrencia,
        LocalDateTime instanteReporte,
        String descripcion,
        Responsable responsable,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable UUID salidaId)
        implements VistaDTO {
    public IncidenciaDTO conSalidaId(UUID valor) {
        return new IncidenciaDTO(
                id,
                tipoIncidenciaId,
                instanteOcurrencia,
                instanteReporte,
                descripcion,
                responsable,
                valor);
    }
}
