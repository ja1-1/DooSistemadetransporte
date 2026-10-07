package transporte.infraestructura.persistencia.repositorio.impl;

import transporte.dao.datos.entidad.MunicipioDAO;
import transporte.dao.factoria.DAOFactory;
import transporte.dominio.MunicipioDominio;
import transporte.dominio.repositorio.MunicipioRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.infraestructura.persistencia.assembler.impl.MunicipioEntidadAssembler;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MunicipioRepositorioImpl implements MunicipioRepositorio {
    private final MunicipioDAO dao;
    private final MunicipioEntidadAssembler assembler;

    public MunicipioRepositorioImpl(DAOFactory fabrica, MunicipioEntidadAssembler assembler) {
        this.dao = fabrica.municipio();
        this.assembler = assembler;
    }

    @Override
    public void guardar(MunicipioDominio municipio) {
        dao.guardar(assembler.convertirAEntidad(municipio));
    }

    @Override
    public Optional<MunicipioDominio> buscarPorId(UUID id) {
        return dao.buscarPorId(id).map(assembler::convertirADominio);
    }

    @Override
    public List<MunicipioDominio> buscar(
            @Nullable String nombre,
            @Nullable String departamento,
            @Nullable Boolean activo,
            Pagina pagina) {
        return dao.buscar(nombre, departamento, activo, pagina).stream()
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
    public boolean existePorNombreYDepartamento(String nombre, String departamento) {
        return dao.existePorNombreYDepartamento(nombre, departamento);
    }
}
