package transporte.infraestructura.persistencia.repositorio.impl;

import transporte.dao.datos.entidad.CatalogoDAO;
import transporte.dao.factoria.DAOFactory;
import transporte.dominio.enums.ValorCatalogo;
import transporte.dominio.repositorio.CatalogoRepositorio;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CatalogoRepositorioImpl implements CatalogoRepositorio {
    private final CatalogoDAO dao;

    public CatalogoRepositorioImpl(DAOFactory fabrica) {
        this.dao = fabrica.catalogos();
    }

    @Override
    public List<ValorCatalogo> consultar(String catalogo) {
        return dao.consultar(catalogo);
    }
}
