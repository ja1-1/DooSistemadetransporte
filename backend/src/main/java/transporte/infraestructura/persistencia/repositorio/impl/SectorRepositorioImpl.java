package transporte.infraestructura.persistencia.repositorio.impl;

import transporte.dao.datos.entidad.SectorDAO;
import transporte.dao.factoria.DAOFactory;
import transporte.dominio.SectorDominio;
import transporte.dominio.repositorio.SectorRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.infraestructura.persistencia.assembler.impl.SectorEntidadAssembler;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SectorRepositorioImpl implements SectorRepositorio {
    private final SectorDAO dao;
    private final SectorEntidadAssembler assembler;

    public SectorRepositorioImpl(DAOFactory fabrica, SectorEntidadAssembler assembler) {
        this.dao = fabrica.sector();
        this.assembler = assembler;
    }

    @Override
    public void guardar(SectorDominio sector) {
        dao.guardar(assembler.convertirAEntidad(sector));
    }

    @Override
    public Optional<SectorDominio> buscarPorId(UUID id) {
        return dao.buscarPorId(id).map(assembler::convertirADominio);
    }

    @Override
    public List<SectorDominio> buscar(
            @Nullable String nombre,
            @Nullable UUID municipioId,
            @Nullable Boolean activo,
            Pagina pagina) {
        return dao.buscar(nombre, municipioId, activo, pagina).stream()
                .map(assembler::convertirADominio)
                .toList();
    }

    @Override
    public void eliminar(UUID id) {
        dao.eliminar(id);
    }

    @Override
    public boolean estaEnUso(UUID id) {
        return dao.estaEnUso(id);
    }

    @Override
    public boolean existePorMunicipioYNombre(UUID municipioId, String nombre) {
        return dao.existePorMunicipioYNombre(municipioId, nombre);
    }
}
