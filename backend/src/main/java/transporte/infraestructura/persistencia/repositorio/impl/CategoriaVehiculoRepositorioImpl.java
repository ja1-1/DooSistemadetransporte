package transporte.infraestructura.persistencia.repositorio.impl;

import transporte.dao.datos.entidad.CategoriaVehiculoDAO;
import transporte.dao.factoria.DAOFactory;
import transporte.dominio.CategoriaVehiculoDominio;
import transporte.dominio.enums.CategoriaLicencia;
import transporte.dominio.repositorio.CategoriaVehiculoRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.infraestructura.persistencia.assembler.impl.CategoriaVehiculoEntidadAssembler;
import transporte.transversal.excepciones.IdentificadorOcupadoExcepcion;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoriaVehiculoRepositorioImpl implements CategoriaVehiculoRepositorio {
    private final CategoriaVehiculoDAO dao;
    private final CategoriaVehiculoEntidadAssembler assembler;

    public CategoriaVehiculoRepositorioImpl(
            DAOFactory factoria, CategoriaVehiculoEntidadAssembler assembler) {
        this.dao = factoria.categoriaVehiculo();
        this.assembler = assembler;
    }

    @Override
    public UUID nuevoIdentificador() {
        return dao.nuevoIdentificador();
    }

    @Override
    public void guardar(CategoriaVehiculoDominio categoriaVehiculo) {
        if (dao.buscarPorId(categoriaVehiculo.getId()).isPresent()) {
            dao.actualizar(assembler.convertirAEntidad(categoriaVehiculo));
            return;
        }
        if (!dao.crear(assembler.convertirAEntidad(categoriaVehiculo))) {
            throw new IdentificadorOcupadoExcepcion();
        }
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return dao.existePorNombre(nombre);
    }

    @Override
    public Optional<CategoriaVehiculoDominio> buscarPorId(UUID id) {
        return dao.buscarPorId(id).map(assembler::convertirADominio);
    }

    @Override
    public List<CategoriaVehiculoDominio> buscar(
            @Nullable String nombre,
            @Nullable CategoriaLicencia categoriaLicenciaMinima,
            Pagina pagina) {
        return dao
                .buscar(
                        nombre,
                        categoriaLicenciaMinima == null ? null : categoriaLicenciaMinima.name(),
                        pagina.tamano(),
                        pagina.desplazamiento())
                .stream()
                .map(assembler::convertirADominio)
                .toList();
    }

    @Override
    public long contar(
            @Nullable String nombre, @Nullable CategoriaLicencia categoriaLicenciaMinima) {
        return dao.contar(
                nombre, categoriaLicenciaMinima == null ? null : categoriaLicenciaMinima.name());
    }

    @Override
    public void eliminar(UUID id) {
        dao.eliminar(id);
    }

    @Override
    public boolean estaEnUso(UUID id) {
        return dao.estaEnUso(id);
    }
}
