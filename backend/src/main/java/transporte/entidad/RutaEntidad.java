package transporte.entidad;

import java.math.BigDecimal;
import java.util.UUID;

public record RutaEntidad(
        UUID id,
        String nombre,
        String descripcion,
        Boolean activa,
        UUID origenLugarId,
        UUID destinoLugarId,
        BigDecimal distanciaTotalKm,
        UUID tipoRutaId,
        UUID viaId) {}
