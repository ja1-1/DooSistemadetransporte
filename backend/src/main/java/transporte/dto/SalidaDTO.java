package transporte.dto;

import transporte.dominio.valor.VentanaViaje;

import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public record SalidaDTO(
        UUID id,
        UUID rutaId,
        String nombreRuta,
        UUID asignacionId,
        VentanaViaje ventana,
        @Nullable LocalDateTime instanteSalidaReal,
        @Nullable LocalDateTime instanteLlegadaReal,
        UUID estadoSalidaId,
        String estadoSalida,
        @Nullable UUID programacionRutaId)
        implements VistaDTO {}
