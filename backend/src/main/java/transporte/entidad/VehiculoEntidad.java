package transporte.entidad;

import java.util.UUID;

public record VehiculoEntidad(
        UUID id,
        String placa,
        String marca,
        String modelo,
        Integer anioFabricacion,
        Integer capacidadPasajeros,
        UUID categoriaVehiculoId,
        Boolean activo) {}
