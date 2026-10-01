package transporte.dominio.consulta;

import transporte.dominio.enums.EstadoSalida;

import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record FiltroSalida(
        @Nullable UUID id,
        @Nullable UUID rutaId,
        @Nullable String nombreRuta,
        @Nullable UUID asignacionId,
        @Nullable LocalDateTime instanteSalidaProgramado,
        @Nullable LocalDateTime instanteLlegadaEstimado,
        @Nullable LocalDateTime instanteSalidaReal,
        @Nullable LocalDateTime instanteLlegadaReal,
        @Nullable EstadoSalida estadoSalida,
        @Nullable UUID programacionRutaId,
        @Nullable LocalDateTime salidaDesde,
        @Nullable LocalDateTime salidaHasta,
        @Nullable Set<UUID> asignacionIds) {
    public FiltroSalida {
        asignacionIds = asignacionIds == null ? null : Set.copyOf(asignacionIds);
    }

    public static FiltroSalida todos() {
        return new FiltroSalida(
                null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public FiltroSalida conId(@Nullable UUID valor) {
        return new FiltroSalida(
                valor,
                rutaId,
                nombreRuta,
                asignacionId,
                instanteSalidaProgramado,
                instanteLlegadaEstimado,
                instanteSalidaReal,
                instanteLlegadaReal,
                estadoSalida,
                programacionRutaId,
                salidaDesde,
                salidaHasta,
                asignacionIds);
    }

    public FiltroSalida conRutaId(@Nullable UUID valor) {
        return new FiltroSalida(
                id,
                valor,
                nombreRuta,
                asignacionId,
                instanteSalidaProgramado,
                instanteLlegadaEstimado,
                instanteSalidaReal,
                instanteLlegadaReal,
                estadoSalida,
                programacionRutaId,
                salidaDesde,
                salidaHasta,
                asignacionIds);
    }

    public FiltroSalida conNombreRuta(@Nullable String valor) {
        return new FiltroSalida(
                id,
                rutaId,
                valor,
                asignacionId,
                instanteSalidaProgramado,
                instanteLlegadaEstimado,
                instanteSalidaReal,
                instanteLlegadaReal,
                estadoSalida,
                programacionRutaId,
                salidaDesde,
                salidaHasta,
                asignacionIds);
    }

    public FiltroSalida conAsignacionId(@Nullable UUID valor) {
        return new FiltroSalida(
                id,
                rutaId,
                nombreRuta,
                valor,
                instanteSalidaProgramado,
                instanteLlegadaEstimado,
                instanteSalidaReal,
                instanteLlegadaReal,
                estadoSalida,
                programacionRutaId,
                salidaDesde,
                salidaHasta,
                asignacionIds);
    }

    public FiltroSalida conInstanteSalidaProgramado(@Nullable LocalDateTime valor) {
        return new FiltroSalida(
                id,
                rutaId,
                nombreRuta,
                asignacionId,
                valor,
                instanteLlegadaEstimado,
                instanteSalidaReal,
                instanteLlegadaReal,
                estadoSalida,
                programacionRutaId,
                salidaDesde,
                salidaHasta,
                asignacionIds);
    }

    public FiltroSalida conInstanteLlegadaEstimado(@Nullable LocalDateTime valor) {
        return new FiltroSalida(
                id,
                rutaId,
                nombreRuta,
                asignacionId,
                instanteSalidaProgramado,
                valor,
                instanteSalidaReal,
                instanteLlegadaReal,
                estadoSalida,
                programacionRutaId,
                salidaDesde,
                salidaHasta,
                asignacionIds);
    }

    public FiltroSalida conInstanteSalidaReal(@Nullable LocalDateTime valor) {
        return new FiltroSalida(
                id,
                rutaId,
                nombreRuta,
                asignacionId,
                instanteSalidaProgramado,
                instanteLlegadaEstimado,
                valor,
                instanteLlegadaReal,
                estadoSalida,
                programacionRutaId,
                salidaDesde,
                salidaHasta,
                asignacionIds);
    }

    public FiltroSalida conInstanteLlegadaReal(@Nullable LocalDateTime valor) {
        return new FiltroSalida(
                id,
                rutaId,
                nombreRuta,
                asignacionId,
                instanteSalidaProgramado,
                instanteLlegadaEstimado,
                instanteSalidaReal,
                valor,
                estadoSalida,
                programacionRutaId,
                salidaDesde,
                salidaHasta,
                asignacionIds);
    }

    public FiltroSalida conEstadoSalida(@Nullable EstadoSalida valor) {
        return new FiltroSalida(
                id,
                rutaId,
                nombreRuta,
                asignacionId,
                instanteSalidaProgramado,
                instanteLlegadaEstimado,
                instanteSalidaReal,
                instanteLlegadaReal,
                valor,
                programacionRutaId,
                salidaDesde,
                salidaHasta,
                asignacionIds);
    }

    public FiltroSalida conProgramacionRutaId(@Nullable UUID valor) {
        return new FiltroSalida(
                id,
                rutaId,
                nombreRuta,
                asignacionId,
                instanteSalidaProgramado,
                instanteLlegadaEstimado,
                instanteSalidaReal,
                instanteLlegadaReal,
                estadoSalida,
                valor,
                salidaDesde,
                salidaHasta,
                asignacionIds);
    }

    public FiltroSalida conSalidaDesde(@Nullable LocalDateTime valor) {
        return new FiltroSalida(
                id,
                rutaId,
                nombreRuta,
                asignacionId,
                instanteSalidaProgramado,
                instanteLlegadaEstimado,
                instanteSalidaReal,
                instanteLlegadaReal,
                estadoSalida,
                programacionRutaId,
                valor,
                salidaHasta,
                asignacionIds);
    }

    public FiltroSalida conSalidaHasta(@Nullable LocalDateTime valor) {
        return new FiltroSalida(
                id,
                rutaId,
                nombreRuta,
                asignacionId,
                instanteSalidaProgramado,
                instanteLlegadaEstimado,
                instanteSalidaReal,
                instanteLlegadaReal,
                estadoSalida,
                programacionRutaId,
                salidaDesde,
                valor,
                asignacionIds);
    }

    public FiltroSalida conAsignacionIds(@Nullable Set<UUID> valor) {
        return new FiltroSalida(
                id,
                rutaId,
                nombreRuta,
                asignacionId,
                instanteSalidaProgramado,
                instanteLlegadaEstimado,
                instanteSalidaReal,
                instanteLlegadaReal,
                estadoSalida,
                programacionRutaId,
                salidaDesde,
                salidaHasta,
                valor);
    }
}
