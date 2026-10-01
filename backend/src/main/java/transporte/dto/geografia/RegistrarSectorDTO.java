package transporte.dto.geografia;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record RegistrarSectorDTO(@Nullable String nombre, @Nullable UUID municipioId) {}
