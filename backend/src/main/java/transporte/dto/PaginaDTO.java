package transporte.dto;

import java.util.List;

public record PaginaDTO<T>(List<T> datos, long total, int pagina, int tamano) {
    public PaginaDTO {
        datos = List.copyOf(datos);
    }
}
