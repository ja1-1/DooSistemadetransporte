package transporte.infraestructura.persistencia.repositorio.impl;

import transporte.dao.datos.entidad.LugarDAO;
import transporte.dao.factoria.DAOFactory;
import transporte.dominio.LugarDominio;
import transporte.dominio.repositorio.LugarRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.infraestructura.persistencia.assembler.impl.LugarEntidadAssembler;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LugarRepositorioImpl implements LugarRepositorio {
    private final LugarDAO dao;
    private final LugarEntidadAssembler assembler;

    public LugarRepositorioImpl(DAOFactory fabrica, LugarEntidadAssembler assembler) {
        this.dao = fabrica.lugar();
        this.assembler = assembler;
    }

    @Override
    public void guardar(LugarDominio lugar) {
        dao.guardar(assembler.convertirAEntidad(lugar));
    }

    @Override
    public Optional<LugarDominio> buscarPorId(UUID id) {
        return dao.buscarPorId(id).map(assembler::convertirADominio);
    }

    @Override
    public List<LugarDominio> buscar(
            @Nullable String nombre,
            @Nullable UUID sectorId,
            @Nullable Boolean activo,
            Pagina pagina) {
        return dao.buscar(nombre, sectorId, activo, pagina).stream()
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
    public boolean existePorSectorYNombre(UUID sectorId, String nombre) {
        return dao.existePorSectorYNombre(sectorId, nombre);
    }
}
