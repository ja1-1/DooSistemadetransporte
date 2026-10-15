package transporte.negocio.negocio.impl;

import transporte.dominio.enums.ValorCatalogo;
import transporte.dominio.repositorio.CatalogoRepositorio;
import transporte.negocio.negocio.CatalogoNegocio;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CatalogoNegocioImpl implements CatalogoNegocio {
    private final CatalogoRepositorio catalogos;

    public CatalogoNegocioImpl(CatalogoRepositorio catalogos) {
        this.catalogos = catalogos;
    }

    @Override
    public List<ValorCatalogo> consultar(String catalogo) {
        return catalogos.consultar(catalogo);
    }

    @Override
    public ValorCatalogo consultarPorId(String catalogo, UUID id) {
        return consultar(catalogo).stream()
                .filter(valor -> valor.getId().equals(id))
                .findFirst()
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NegocioExcepcion.Tipo.NO_ENCONTRADO,
                                        CatalogoMensajes.NO_SE_ENCONTRO_EL_VALOR_DEL_CATALOGO));
    }
}
