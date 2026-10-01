package transporte.dominio.consulta;

import transporte.dominio.enums.TipoRuta;

import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.UUID;

public record FiltroRuta(
        @Nullable UUID id,
        @Nullable String nombre,
        @Nullable String descripcion,
        @Nullable Boolean activa,
        @Nullable UUID origenLugarId,
        @Nullable UUID destinoLugarId,
        @Nullable BigDecimal distanciaTotalKm,
        @Nullable TipoRuta tipoRuta,
        @Nullable UUID viaId,
        @Nullable UUID municipioId) {
    public static FiltroRuta todos() {
        return new FiltroRuta(null, null, null, null, null, null, null, null, null, null);
    }

    public FiltroRuta conId(@Nullable UUID valor) {
        return new FiltroRuta(
                valor,
                nombre,
                descripcion,
                activa,
                origenLugarId,
                destinoLugarId,
                distanciaTotalKm,
                tipoRuta,
                viaId,
                municipioId);
    }

    public FiltroRuta conNombre(@Nullable String valor) {
        return new FiltroRuta(
                id,
                valor,
                descripcion,
                activa,
                origenLugarId,
                destinoLugarId,
                distanciaTotalKm,
                tipoRuta,
                viaId,
                municipioId);
    }

    public FiltroRuta conDescripcion(@Nullable String valor) {
        return new FiltroRuta(
                id,
                nombre,
                valor,
                activa,
                origenLugarId,
                destinoLugarId,
                distanciaTotalKm,
                tipoRuta,
                viaId,
                municipioId);
    }

    public FiltroRuta conActiva(@Nullable Boolean valor) {
        return new FiltroRuta(
                id,
                nombre,
                descripcion,
                valor,
                origenLugarId,
                destinoLugarId,
                distanciaTotalKm,
                tipoRuta,
                viaId,
                municipioId);
    }

    public FiltroRuta conOrigenLugarId(@Nullable UUID valor) {
        return new FiltroRuta(
                id,
                nombre,
                descripcion,
                activa,
                valor,
                destinoLugarId,
                distanciaTotalKm,
                tipoRuta,
                viaId,
                municipioId);
    }

    public FiltroRuta conDestinoLugarId(@Nullable UUID valor) {
        return new FiltroRuta(
                id,
                nombre,
                descripcion,
                activa,
                origenLugarId,
                valor,
                distanciaTotalKm,
                tipoRuta,
                viaId,
                municipioId);
    }

    public FiltroRuta conDistanciaTotalKm(@Nullable BigDecimal valor) {
        return new FiltroRuta(
                id,
                nombre,
                descripcion,
                activa,
                origenLugarId,
                destinoLugarId,
                valor,
                tipoRuta,
                viaId,
                municipioId);
    }

    public FiltroRuta conTipoRuta(@Nullable TipoRuta valor) {
        return new FiltroRuta(
                id,
                nombre,
                descripcion,
                activa,
                origenLugarId,
                destinoLugarId,
                distanciaTotalKm,
                valor,
                viaId,
                municipioId);
    }

    public FiltroRuta conViaId(@Nullable UUID valor) {
        return new FiltroRuta(
                id,
                nombre,
                descripcion,
                activa,
                origenLugarId,
                destinoLugarId,
                distanciaTotalKm,
                tipoRuta,
                valor,
                municipioId);
    }

    public FiltroRuta conMunicipioId(@Nullable UUID valor) {
        return new FiltroRuta(
                id,
                nombre,
                descripcion,
                activa,
                origenLugarId,
                destinoLugarId,
                distanciaTotalKm,
                tipoRuta,
                viaId,
                valor);
    }
}
