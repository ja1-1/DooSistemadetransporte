package transporte.infraestructura.persistencia.repositorio.impl;

import transporte.dao.datos.entidad.AsignacionDAO;
import transporte.dao.factoria.DAOFactory;
import transporte.dominio.AsignacionDominio;
import transporte.dominio.consulta.FiltroAsignacion;
import transporte.dominio.repositorio.AsignacionRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.entidad.AsignacionEntidad;
import transporte.infraestructura.persistencia.assembler.impl.AsignacionEntidadAssembler;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AsignacionRepositorioImpl implements AsignacionRepositorio {
    private final AsignacionDAO dao;
    private final AsignacionEntidadAssembler assembler;

    public AsignacionRepositorioImpl(DAOFactory fabrica, AsignacionEntidadAssembler assembler) {
        dao = fabrica.asignacion();
        this.assembler = assembler;
    }

    private AsignacionDominio reconstruir(AsignacionEntidad asignacionEntidad) {
        return assembler.convertirADominio(asignacionEntidad);
    }

    @Override
    @Transactional
    public void guardar(AsignacionDominio asignacion) {
        dao.guardar(assembler.convertirAEntidad(asignacion));
    }

    @Override
    public Optional<AsignacionDominio> buscarPorId(UUID id) {
        return dao.buscarPorId(id).map(this::reconstruir);
    }

    @Override
    public List<AsignacionDominio> buscar(FiltroAsignacion filtro, Pagina pagina) {
        return dao.buscar(filtro, pagina).stream().map(this::reconstruir).toList();
    }

    @Override
    public List<AsignacionDominio> relacionados(FiltroAsignacion filtro) {
        return dao.relacionados(filtro).stream().map(this::reconstruir).toList();
    }

    @Override
    public long contar(FiltroAsignacion filtro) {
        return dao.contar(filtro);
    }
}
