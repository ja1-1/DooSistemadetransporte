package transporte.entidad;

import java.util.UUID;

public record ViaEntidad(UUID id, String nombre, String descripcion, boolean activo) {}
