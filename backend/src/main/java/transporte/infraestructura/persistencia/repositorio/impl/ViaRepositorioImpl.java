package transporte.infraestructura.persistencia.repositorio.impl;

import transporte.dao.datos.entidad.ViaDAO;
import transporte.dao.factoria.DAOFactory;
import transporte.dominio.ViaDominio;
import transporte.dominio.repositorio.ViaRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.infraestructura.persistencia.assembler.impl.ViaEntidadAssembler;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ViaRepositorioImpl implements ViaRepositorio {
    private final ViaDAO dao;
    private final ViaEntidadAssembler assembler;

    public ViaRepositorioImpl(DAOFactory fabrica, ViaEntidadAssembler assembler) {
        this.dao = fabrica.via();
        this.assembler = assembler;
    }

    @Override
    public void guardar(ViaDominio via) {
        dao.guardar(assembler.convertirAEntidad(via));
    }

    @Override
    public Optional<ViaDominio> buscarPorId(UUID id) {
        return dao.buscarPorId(id).map(assembler::convertirADominio);
    }

    @Override
    public List<ViaDominio> buscar(
            @Nullable String nombre,
            @Nullable String descripcion,
            @Nullable Boolean activo,
            Pagina pagina) {
        return dao.buscar(nombre, descripcion, activo, pagina).stream()
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
    public boolean existePorNombre(String nombre) {
        return dao.existePorNombre(nombre);
    }
}
