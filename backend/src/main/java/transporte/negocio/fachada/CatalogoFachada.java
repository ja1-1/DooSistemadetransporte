package transporte.negocio.fachada;

import transporte.dto.CatalogoDTO;

import java.util.List;
import java.util.UUID;

public interface CatalogoFachada {
    List<CatalogoDTO> consultar(String catalogo);

    CatalogoDTO consultarPorId(String catalogo, UUID id);
}
