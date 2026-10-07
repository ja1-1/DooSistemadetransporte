package transporte.infraestructura.persistencia.repositorio.impl;

import transporte.dao.datos.entidad.AsignacionDAO;
import transporte.dao.datos.entidad.ConductorDAO;
import transporte.dao.datos.entidad.UsuarioDAO;
import transporte.dao.factoria.DAOFactory;
import transporte.dominio.ConductorDominio;
import transporte.dominio.consulta.FiltroAsignacion;
import transporte.dominio.consulta.FiltroConductor;
import transporte.dominio.consulta.FiltroUsuario;
import transporte.dominio.repositorio.ConductorRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.entidad.ConductorEntidad;
import transporte.infraestructura.persistencia.assembler.impl.ConductorEntidadAssembler;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ConductorRepositorioImpl implements ConductorRepositorio {
    private final ConductorDAO dao;
    private final AsignacionDAO asignaciones;
    private final UsuarioDAO usuarios;
    private final ConductorEntidadAssembler assembler;

    public ConductorRepositorioImpl(DAOFactory fabrica, ConductorEntidadAssembler assembler) {
        dao = fabrica.conductor();
        asignaciones = fabrica.asignacion();
        usuarios = fabrica.usuario();
        this.assembler = assembler;
    }

    private ConductorDominio reconstruir(ConductorEntidad conductorEntidad) {
        return assembler.convertirADominio(conductorEntidad);
    }

    @Override
    @Transactional
    public void guardar(ConductorDominio conductor) {
        dao.guardar(assembler.convertirAEntidad(conductor));
    }

    @Override
    public Optional<ConductorDominio> buscarPorId(UUID id) {
        return dao.buscarPorId(id).map(this::reconstruir);
    }

    @Override
    public List<ConductorDominio> buscar(FiltroConductor filtro, Pagina pagina) {
        return dao.buscar(filtro, pagina).stream().map(this::reconstruir).toList();
    }

    @Override
    public List<ConductorDominio> relacionados(FiltroConductor filtro) {
        return dao.relacionados(filtro).stream().map(this::reconstruir).toList();
    }

    @Override
    public void eliminar(UUID id) {
        dao.eliminar(id);
    }

    @Override
    public boolean estaEnUso(UUID id) {
        return asignaciones.contar(FiltroAsignacion.todos().conConductorId(id)) > 0
                || usuarios.contar(FiltroUsuario.todos().conConductorId(id)) > 0;
    }

    @Override
    public long contar(FiltroConductor filtro) {
        return dao.contar(filtro);
    }
}
