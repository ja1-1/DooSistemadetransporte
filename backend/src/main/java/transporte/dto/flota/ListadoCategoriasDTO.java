package transporte.dto.flota;

import java.util.List;

public record ListadoCategoriasDTO(
        List<CategoriaVehiculoDTO> datos, long total, int pagina, int tamano) {}
