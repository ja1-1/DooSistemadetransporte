package transporte.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record RutaDTO(
        UUID id,
        String nombre,
        String descripcion,
        UUID tipoRutaId,
        UUID origenId,
        UUID destinoId,
        UUID viaId,
        BigDecimal distanciaTotalKm,
        boolean activa)
        implements VistaDTO {}
