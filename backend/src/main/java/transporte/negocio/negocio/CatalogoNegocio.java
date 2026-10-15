package transporte.negocio.negocio;

import transporte.dominio.enums.ValorCatalogo;

import java.util.List;
import java.util.UUID;

public interface CatalogoNegocio {
    List<ValorCatalogo> consultar(String catalogo);

    ValorCatalogo consultarPorId(String catalogo, UUID id);
}
