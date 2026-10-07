package transporte.dao.datos.entidad;

import transporte.dominio.enums.ValorCatalogo;

import java.util.List;

public interface CatalogoDAO {
    List<ValorCatalogo> consultar(String catalogo);
}
