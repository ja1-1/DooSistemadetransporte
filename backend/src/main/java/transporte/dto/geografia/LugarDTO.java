package transporte.dto.geografia;

import java.util.UUID;

public record LugarDTO(UUID id, String nombre, UUID sectorId, boolean activo) {}
