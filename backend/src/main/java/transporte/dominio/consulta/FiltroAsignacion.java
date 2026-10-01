package transporte.dominio.consulta;

import transporte.dominio.enums.EstadoAsignacion;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.UUID;

public record FiltroAsignacion(
        @Nullable UUID id,
        @Nullable UUID conductorId,
        @Nullable UUID vehiculoId,
        @Nullable LocalDate fechaInicio,
        @Nullable LocalDate fechaFin,
        @Nullable EstadoAsignacion estado,
        @Nullable LocalDate fechaReferencia) {
    public static FiltroAsignacion todos() {
        return new FiltroAsignacion(null, null, null, null, null, null, null);
    }

    public FiltroAsignacion conId(@Nullable UUID valor) {
        return new FiltroAsignacion(
                valor, conductorId, vehiculoId, fechaInicio, fechaFin, estado, fechaReferencia);
    }

    public FiltroAsignacion conConductorId(@Nullable UUID valor) {
        return new FiltroAsignacion(
                id, valor, vehiculoId, fechaInicio, fechaFin, estado, fechaReferencia);
    }

    public FiltroAsignacion conVehiculoId(@Nullable UUID valor) {
        return new FiltroAsignacion(
                id, conductorId, valor, fechaInicio, fechaFin, estado, fechaReferencia);
    }

    public FiltroAsignacion conFechaInicio(@Nullable LocalDate valor) {
        return new FiltroAsignacion(
                id, conductorId, vehiculoId, valor, fechaFin, estado, fechaReferencia);
    }

    public FiltroAsignacion conFechaFin(@Nullable LocalDate valor) {
        return new FiltroAsignacion(
                id, conductorId, vehiculoId, fechaInicio, valor, estado, fechaReferencia);
    }

    public FiltroAsignacion conEstado(@Nullable EstadoAsignacion valor) {
        return new FiltroAsignacion(
                id, conductorId, vehiculoId, fechaInicio, fechaFin, valor, fechaReferencia);
    }

    public FiltroAsignacion conFechaReferencia(@Nullable LocalDate valor) {
        return new FiltroAsignacion(
                id, conductorId, vehiculoId, fechaInicio, fechaFin, estado, valor);
    }
}
