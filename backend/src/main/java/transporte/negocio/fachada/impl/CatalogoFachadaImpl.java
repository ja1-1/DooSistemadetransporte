package transporte.negocio.fachada.impl;

import transporte.dto.CatalogoDTO;
import transporte.negocio.fachada.CatalogoFachada;
import transporte.negocio.fachada.assembler.impl.CatalogoDTOAssembler;
import transporte.negocio.negocio.CatalogoNegocio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
public class CatalogoFachadaImpl implements CatalogoFachada {
    private final CatalogoNegocio negocio;
    private final CatalogoDTOAssembler assembler;

    public CatalogoFachadaImpl(CatalogoNegocio negocio, CatalogoDTOAssembler assembler) {
        this.negocio = negocio;
        this.assembler = assembler;
    }

    @Override
    public List<CatalogoDTO> consultar(String catalogo) {
        return assembler.convertirADTO(negocio.consultar(catalogo));
    }

    @Override
    public CatalogoDTO consultarPorId(String catalogo, UUID id) {
        return assembler.convertirADTO(negocio.consultarPorId(catalogo, id));
    }
}
