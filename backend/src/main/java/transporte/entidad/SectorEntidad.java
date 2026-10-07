package transporte.entidad;

import java.util.UUID;

public record SectorEntidad(UUID id, String nombre, UUID municipioId, boolean activo) {}
