package transporte.dto.geografia;

import java.util.UUID;

public record SectorDTO(UUID id, String nombre, UUID municipioId, boolean activo) {}
