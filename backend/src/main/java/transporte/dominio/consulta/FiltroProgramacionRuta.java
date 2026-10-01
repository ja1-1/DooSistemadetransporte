package transporte.dominio.consulta;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record FiltroProgramacionRuta(
        @Nullable UUID id,
        @Nullable UUID rutaId,
        @Nullable UUID asignacionId,
        @Nullable LocalTime horaSalida,
        @Nullable LocalTime horaUltimaSalida,
        @Nullable Integer intervaloMinutos,
        @Nullable Integer duracionEstimada,
        @Nullable LocalDate vigenciaDesde,
        @Nullable LocalDate vigenciaHasta,
        @Nullable Boolean activa) {
    public static FiltroProgramacionRuta todos() {
        return new FiltroProgramacionRuta(
                null, null, null, null, null, null, null, null, null, null);
    }

    public FiltroProgramacionRuta conId(@Nullable UUID valor) {
        return new FiltroProgramacionRuta(
                valor,
                rutaId,
                asignacionId,
                horaSalida,
                horaUltimaSalida,
                intervaloMinutos,
                duracionEstimada,
                vigenciaDesde,
                vigenciaHasta,
                activa);
    }

    public FiltroProgramacionRuta conRutaId(@Nullable UUID valor) {
        return new FiltroProgramacionRuta(
                id,
                valor,
                asignacionId,
                horaSalida,
                horaUltimaSalida,
                intervaloMinutos,
                duracionEstimada,
                vigenciaDesde,
                vigenciaHasta,
                activa);
    }

    public FiltroProgramacionRuta conAsignacionId(@Nullable UUID valor) {
        return new FiltroProgramacionRuta(
                id,
                rutaId,
                valor,
                horaSalida,
                horaUltimaSalida,
                intervaloMinutos,
                duracionEstimada,
                vigenciaDesde,
                vigenciaHasta,
                activa);
    }

    public FiltroProgramacionRuta conHoraSalida(@Nullable LocalTime valor) {
        return new FiltroProgramacionRuta(
                id,
                rutaId,
                asignacionId,
                valor,
                horaUltimaSalida,
                intervaloMinutos,
                duracionEstimada,
                vigenciaDesde,
                vigenciaHasta,
                activa);
    }

    public FiltroProgramacionRuta conHoraUltimaSalida(@Nullable LocalTime valor) {
        return new FiltroProgramacionRuta(
                id,
                rutaId,
                asignacionId,
                horaSalida,
                valor,
                intervaloMinutos,
                duracionEstimada,
                vigenciaDesde,
                vigenciaHasta,
                activa);
    }

    public FiltroProgramacionRuta conIntervaloMinutos(@Nullable Integer valor) {
        return new FiltroProgramacionRuta(
                id,
                rutaId,
                asignacionId,
                horaSalida,
                horaUltimaSalida,
                valor,
                duracionEstimada,
                vigenciaDesde,
                vigenciaHasta,
                activa);
    }

    public FiltroProgramacionRuta conDuracionEstimada(@Nullable Integer valor) {
        return new FiltroProgramacionRuta(
                id,
                rutaId,
                asignacionId,
                horaSalida,
                horaUltimaSalida,
                intervaloMinutos,
                valor,
                vigenciaDesde,
                vigenciaHasta,
                activa);
    }

    public FiltroProgramacionRuta conVigenciaDesde(@Nullable LocalDate valor) {
        return new FiltroProgramacionRuta(
                id,
                rutaId,
                asignacionId,
                horaSalida,
                horaUltimaSalida,
                intervaloMinutos,
                duracionEstimada,
                valor,
                vigenciaHasta,
                activa);
    }

    public FiltroProgramacionRuta conVigenciaHasta(@Nullable LocalDate valor) {
        return new FiltroProgramacionRuta(
                id,
                rutaId,
                asignacionId,
                horaSalida,
                horaUltimaSalida,
                intervaloMinutos,
                duracionEstimada,
                vigenciaDesde,
                valor,
                activa);
    }

    public FiltroProgramacionRuta conActiva(@Nullable Boolean valor) {
        return new FiltroProgramacionRuta(
                id,
                rutaId,
                asignacionId,
                horaSalida,
                horaUltimaSalida,
                intervaloMinutos,
                duracionEstimada,
                vigenciaDesde,
                vigenciaHasta,
                valor);
    }
}
