package transporte.dto;

import transporte.dominio.valor.HorarioProgramado;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.UUID;

public record ProgramacionRutaDTO(
        UUID id,
        UUID rutaId,
        UUID asignacionId,
        HorarioProgramado horario,
        LocalDate vigenciaDesde,
        @Nullable LocalDate vigenciaHasta,
        boolean activa)
        implements VistaDTO {}
