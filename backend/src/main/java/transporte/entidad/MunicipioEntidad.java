package transporte.entidad;

import java.util.UUID;

public record MunicipioEntidad(UUID id, String nombre, String departamento, boolean activo) {}
