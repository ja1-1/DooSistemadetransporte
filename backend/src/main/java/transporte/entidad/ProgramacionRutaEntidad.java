package transporte.entidad;

import org.jspecify.annotations.Nullable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

public record ProgramacionRutaEntidad(
        UUID id,
        UUID rutaId,
        UUID asignacionId,
        LocalTime horaSalida,
        LocalTime horaUltimaSalida,
        Integer intervaloMinutos,
        Integer duracionEstimada,
        LocalDate vigenciaDesde,
        @Nullable LocalDate vigenciaHasta,
        Boolean activa,
        Set<DayOfWeek> dias) {
    public ProgramacionRutaEntidad {
        dias = Set.copyOf(dias);
    }

    public ProgramacionRutaEntidad conDias(Set<DayOfWeek> valor) {
        return new ProgramacionRutaEntidad(
                id,
                rutaId,
                asignacionId,
                horaSalida,
                horaUltimaSalida,
                intervaloMinutos,
                duracionEstimada,
                vigenciaDesde,
                vigenciaHasta,
                activa,
                valor);
    }
}
