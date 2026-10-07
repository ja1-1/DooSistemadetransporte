package transporte.infraestructura.persistencia.repositorio.impl;

import transporte.dao.datos.entidad.AsignacionDAO;
import transporte.dao.datos.entidad.VehiculoDAO;
import transporte.dao.factoria.DAOFactory;
import transporte.dominio.VehiculoDominio;
import transporte.dominio.consulta.FiltroAsignacion;
import transporte.dominio.consulta.FiltroVehiculo;
import transporte.dominio.repositorio.VehiculoRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.entidad.VehiculoEntidad;
import transporte.infraestructura.persistencia.assembler.impl.VehiculoEntidadAssembler;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class VehiculoRepositorioImpl implements VehiculoRepositorio {
    private final VehiculoDAO dao;
    private final AsignacionDAO asignaciones;
    private final VehiculoEntidadAssembler assembler;

    public VehiculoRepositorioImpl(DAOFactory fabrica, VehiculoEntidadAssembler assembler) {
        dao = fabrica.vehiculo();
        asignaciones = fabrica.asignacion();
        this.assembler = assembler;
    }

    private VehiculoDominio reconstruir(VehiculoEntidad vehiculoEntidad) {
        return assembler.convertirADominio(vehiculoEntidad);
    }

    @Override
    @Transactional
    public void guardar(VehiculoDominio vehiculo) {
        dao.guardar(assembler.convertirAEntidad(vehiculo));
    }

    @Override
    public Optional<VehiculoDominio> buscarPorId(UUID id) {
        return dao.buscarPorId(id).map(this::reconstruir);
    }

    @Override
    public List<VehiculoDominio> buscar(FiltroVehiculo filtro, Pagina pagina) {
        return dao.buscar(filtro, pagina).stream().map(this::reconstruir).toList();
    }

    @Override
    public List<VehiculoDominio> relacionados(FiltroVehiculo filtro) {
        return dao.relacionados(filtro).stream().map(this::reconstruir).toList();
    }

    @Override
    public void eliminar(UUID id) {
        dao.eliminar(id);
    }

    @Override
    public boolean estaEnUso(UUID id) {
        return asignaciones.contar(FiltroAsignacion.todos().conVehiculoId(id)) > 0;
    }

    @Override
    public long contar(FiltroVehiculo filtro) {
        return dao.contar(filtro);
    }
}
