package transporte.dto.geografia;

import java.util.UUID;

public record MunicipioDTO(UUID id, String nombre, String departamento, boolean activo) {}
