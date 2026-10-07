package transporte.entidad;

import java.time.LocalDateTime;
import java.util.UUID;

public record IncidenciaEntidad(
        UUID id,
        UUID salidaId,
        UUID tipoIncidenciaId,
        LocalDateTime instanteOcurrencia,
        LocalDateTime instanteReporte,
        String descripcion,
        UUID responsableId,
        String responsableNombre,
        String responsableRol) {
    public IncidenciaEntidad conId(UUID valor) {
        return new IncidenciaEntidad(
                valor,
                salidaId,
                tipoIncidenciaId,
                instanteOcurrencia,
                instanteReporte,
                descripcion,
                responsableId,
                responsableNombre,
                responsableRol);
    }

    public IncidenciaEntidad conSalidaId(UUID valor) {
        return new IncidenciaEntidad(
                id,
                valor,
                tipoIncidenciaId,
                instanteOcurrencia,
                instanteReporte,
                descripcion,
                responsableId,
                responsableNombre,
                responsableRol);
    }
}
