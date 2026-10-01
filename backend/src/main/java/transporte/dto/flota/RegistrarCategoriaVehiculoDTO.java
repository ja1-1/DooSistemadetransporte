package transporte.dto.flota;

import org.jspecify.annotations.Nullable;

public record RegistrarCategoriaVehiculoDTO(
        @Nullable String nombre, @Nullable String categoriaLicenciaMinima) {}
