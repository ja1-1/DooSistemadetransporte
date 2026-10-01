package transporte.dominio.consulta;

import transporte.dominio.enums.TipoIncidencia;

import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record FiltroIncidencia(
        @Nullable UUID id,
        @Nullable UUID salidaId,
        @Nullable TipoIncidencia tipoIncidencia,
        @Nullable LocalDateTime instanteOcurrencia,
        @Nullable LocalDateTime instanteReporte,
        @Nullable String descripcion,
        @Nullable UUID responsableId,
        @Nullable String responsableNombre,
        @Nullable String responsableRol,
        @Nullable LocalDateTime ocurrenciaDesde,
        @Nullable LocalDateTime ocurrenciaHasta,
        @Nullable Set<UUID> salidaIds) {
    public FiltroIncidencia {
        salidaIds = salidaIds == null ? null : Set.copyOf(salidaIds);
    }

    public static FiltroIncidencia todos() {
        return new FiltroIncidencia(
                null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public FiltroIncidencia conId(@Nullable UUID valor) {
        return new FiltroIncidencia(
                valor,
                salidaId,
                tipoIncidencia,
                instanteOcurrencia,
                instanteReporte,
                descripcion,
                responsableId,
                responsableNombre,
                responsableRol,
                ocurrenciaDesde,
                ocurrenciaHasta,
                salidaIds);
    }

    public FiltroIncidencia conSalidaId(@Nullable UUID valor) {
        return new FiltroIncidencia(
                id,
                valor,
                tipoIncidencia,
                instanteOcurrencia,
                instanteReporte,
                descripcion,
                responsableId,
                responsableNombre,
                responsableRol,
                ocurrenciaDesde,
                ocurrenciaHasta,
                salidaIds);
    }

    public FiltroIncidencia conTipoIncidencia(@Nullable TipoIncidencia valor) {
        return new FiltroIncidencia(
                id,
                salidaId,
                valor,
                instanteOcurrencia,
                instanteReporte,
                descripcion,
                responsableId,
                responsableNombre,
                responsableRol,
                ocurrenciaDesde,
                ocurrenciaHasta,
                salidaIds);
    }

    public FiltroIncidencia conInstanteOcurrencia(@Nullable LocalDateTime valor) {
        return new FiltroIncidencia(
                id,
                salidaId,
                tipoIncidencia,
                valor,
                instanteReporte,
                descripcion,
                responsableId,
                responsableNombre,
                responsableRol,
                ocurrenciaDesde,
                ocurrenciaHasta,
                salidaIds);
    }

    public FiltroIncidencia conInstanteReporte(@Nullable LocalDateTime valor) {
        return new FiltroIncidencia(
                id,
                salidaId,
                tipoIncidencia,
                instanteOcurrencia,
                valor,
                descripcion,
                responsableId,
                responsableNombre,
                responsableRol,
                ocurrenciaDesde,
                ocurrenciaHasta,
                salidaIds);
    }

    public FiltroIncidencia conDescripcion(@Nullable String valor) {
        return new FiltroIncidencia(
                id,
                salidaId,
                tipoIncidencia,
                instanteOcurrencia,
                instanteReporte,
                valor,
                responsableId,
                responsableNombre,
                responsableRol,
                ocurrenciaDesde,
                ocurrenciaHasta,
                salidaIds);
    }

    public FiltroIncidencia conResponsableId(@Nullable UUID valor) {
        return new FiltroIncidencia(
                id,
                salidaId,
                tipoIncidencia,
                instanteOcurrencia,
                instanteReporte,
                descripcion,
                valor,
                responsableNombre,
                responsableRol,
                ocurrenciaDesde,
                ocurrenciaHasta,
                salidaIds);
    }

    public FiltroIncidencia conResponsableNombre(@Nullable String valor) {
        return new FiltroIncidencia(
                id,
                salidaId,
                tipoIncidencia,
                instanteOcurrencia,
                instanteReporte,
                descripcion,
                responsableId,
                valor,
                responsableRol,
                ocurrenciaDesde,
                ocurrenciaHasta,
                salidaIds);
    }

    public FiltroIncidencia conResponsableRol(@Nullable String valor) {
        return new FiltroIncidencia(
                id,
                salidaId,
                tipoIncidencia,
                instanteOcurrencia,
                instanteReporte,
                descripcion,
                responsableId,
                responsableNombre,
                valor,
                ocurrenciaDesde,
                ocurrenciaHasta,
                salidaIds);
    }

    public FiltroIncidencia conOcurrenciaDesde(@Nullable LocalDateTime valor) {
        return new FiltroIncidencia(
                id,
                salidaId,
                tipoIncidencia,
                instanteOcurrencia,
                instanteReporte,
                descripcion,
                responsableId,
                responsableNombre,
                responsableRol,
                valor,
                ocurrenciaHasta,
                salidaIds);
    }

    public FiltroIncidencia conOcurrenciaHasta(@Nullable LocalDateTime valor) {
        return new FiltroIncidencia(
                id,
                salidaId,
                tipoIncidencia,
                instanteOcurrencia,
                instanteReporte,
                descripcion,
                responsableId,
                responsableNombre,
                responsableRol,
                ocurrenciaDesde,
                valor,
                salidaIds);
    }

    public FiltroIncidencia conSalidaIds(@Nullable Set<UUID> valor) {
        return new FiltroIncidencia(
                id,
                salidaId,
                tipoIncidencia,
                instanteOcurrencia,
                instanteReporte,
                descripcion,
                responsableId,
                responsableNombre,
                responsableRol,
                ocurrenciaDesde,
                ocurrenciaHasta,
                valor);
    }
}
