package transporte.dto.geografia;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record RegistrarLugarDTO(@Nullable String nombre, @Nullable UUID sectorId) {}
