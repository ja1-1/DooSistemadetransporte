package transporte.dto.geografia;

import java.util.UUID;

public record ViaDTO(UUID id, String nombre, String descripcion, boolean activo) {}
