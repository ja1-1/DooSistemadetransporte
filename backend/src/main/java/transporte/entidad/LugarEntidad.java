package transporte.entidad;

import java.util.UUID;

public record LugarEntidad(UUID id, String nombre, UUID sectorId, boolean activo) {}
