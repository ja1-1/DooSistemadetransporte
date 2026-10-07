package transporte.entidad;

import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SalidaEntidad(
        UUID id,
        UUID rutaId,
        String nombreRuta,
        UUID asignacionId,
        LocalDateTime instanteSalidaProgramado,
        LocalDateTime instanteLlegadaEstimado,
        @Nullable LocalDateTime instanteSalidaReal,
        @Nullable LocalDateTime instanteLlegadaReal,
        UUID estadoSalidaId,
        @Nullable UUID programacionRutaId,
        List<IncidenciaEntidad> incidencias) {
    public SalidaEntidad {
        incidencias = List.copyOf(incidencias);
    }

    public SalidaEntidad conIncidencias(List<IncidenciaEntidad> valor) {
        return new SalidaEntidad(
                id,
                rutaId,
                nombreRuta,
                asignacionId,
                instanteSalidaProgramado,
                instanteLlegadaEstimado,
                instanteSalidaReal,
                instanteLlegadaReal,
                estadoSalidaId,
                programacionRutaId,
                valor);
    }
}
