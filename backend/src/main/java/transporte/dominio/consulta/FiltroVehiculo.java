package transporte.dominio.consulta;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record FiltroVehiculo(
        @Nullable UUID id,
        @Nullable String placa,
        @Nullable String marca,
        @Nullable String modelo,
        @Nullable Integer anioFabricacion,
        @Nullable Integer capacidadPasajeros,
        @Nullable UUID categoriaVehiculoId,
        @Nullable Boolean activo) {
    public static FiltroVehiculo todos() {
        return new FiltroVehiculo(null, null, null, null, null, null, null, null);
    }

    public FiltroVehiculo conId(@Nullable UUID valor) {
        return new FiltroVehiculo(
                valor,
                placa,
                marca,
                modelo,
                anioFabricacion,
                capacidadPasajeros,
                categoriaVehiculoId,
                activo);
    }

    public FiltroVehiculo conPlaca(@Nullable String valor) {
        return new FiltroVehiculo(
                id,
                valor,
                marca,
                modelo,
                anioFabricacion,
                capacidadPasajeros,
                categoriaVehiculoId,
                activo);
    }

    public FiltroVehiculo conMarca(@Nullable String valor) {
        return new FiltroVehiculo(
                id,
                placa,
                valor,
                modelo,
                anioFabricacion,
                capacidadPasajeros,
                categoriaVehiculoId,
                activo);
    }

    public FiltroVehiculo conModelo(@Nullable String valor) {
        return new FiltroVehiculo(
                id,
                placa,
                marca,
                valor,
                anioFabricacion,
                capacidadPasajeros,
                categoriaVehiculoId,
                activo);
    }

    public FiltroVehiculo conAnioFabricacion(@Nullable Integer valor) {
        return new FiltroVehiculo(
                id, placa, marca, modelo, valor, capacidadPasajeros, categoriaVehiculoId, activo);
    }

    public FiltroVehiculo conCapacidadPasajeros(@Nullable Integer valor) {
        return new FiltroVehiculo(
                id, placa, marca, modelo, anioFabricacion, valor, categoriaVehiculoId, activo);
    }

    public FiltroVehiculo conCategoriaVehiculoId(@Nullable UUID valor) {
        return new FiltroVehiculo(
                id, placa, marca, modelo, anioFabricacion, capacidadPasajeros, valor, activo);
    }

    public FiltroVehiculo conActivo(@Nullable Boolean valor) {
        return new FiltroVehiculo(
                id,
                placa,
                marca,
                modelo,
                anioFabricacion,
                capacidadPasajeros,
                categoriaVehiculoId,
                valor);
    }
}
