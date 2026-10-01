package transporte.dto;

import java.util.UUID;

public record VehiculoDTO(
        UUID id,
        String placa,
        String marca,
        String modelo,
        int anioFabricacion,
        int capacidadPasajeros,
        UUID categoriaVehiculoId,
        boolean activo)
        implements VistaDTO {}
