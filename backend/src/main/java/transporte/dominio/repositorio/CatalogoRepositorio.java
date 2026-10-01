package transporte.dominio.repositorio;

import transporte.dominio.enums.ValorCatalogo;

import java.util.List;

public interface CatalogoRepositorio {
    List<ValorCatalogo> consultar(String catalogo);
}
