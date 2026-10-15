package transporte.negocio.fachada;

import transporte.dto.PaginaDTO;
import transporte.dto.VistaDTO;

import java.util.Map;
import java.util.UUID;

public interface ConsultaFachada {
    public VistaDTO consultar(String recurso, UUID id);

    public PaginaDTO<? extends VistaDTO> buscar(String recurso, Map<String, String> parametros);
}
