package transporte.infraestructura.persistencia.repositorio.impl;

import transporte.dao.datos.entidad.ProgramacionRutaDAO;
import transporte.dao.factoria.DAOFactory;
import transporte.dominio.ProgramacionRutaDominio;
import transporte.dominio.consulta.FiltroProgramacionRuta;
import transporte.dominio.repositorio.ProgramacionRutaRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.entidad.ProgramacionRutaEntidad;
import transporte.infraestructura.persistencia.assembler.impl.ProgramacionRutaEntidadAssembler;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProgramacionRutaRepositorioImpl implements ProgramacionRutaRepositorio {
    private final ProgramacionRutaDAO dao;
    private final ProgramacionRutaEntidadAssembler assembler;

    public ProgramacionRutaRepositorioImpl(
            DAOFactory fabrica, ProgramacionRutaEntidadAssembler assembler) {
        dao = fabrica.programacionRuta();
        this.assembler = assembler;
    }

    private ProgramacionRutaDominio reconstruir(ProgramacionRutaEntidad entidad) {
        return assembler.convertirADominio(entidad);
    }

    @Override
    @Transactional
    public void guardar(ProgramacionRutaDominio programacionRuta) {
        dao.guardar(assembler.convertirAEntidad(programacionRuta));
    }

    @Override
    public Optional<ProgramacionRutaDominio> buscarPorId(UUID id) {
        return dao.buscarPorId(id).map(this::reconstruir);
    }

    @Override
    public List<ProgramacionRutaDominio> buscar(FiltroProgramacionRuta filtro, Pagina pagina) {
        return dao.buscar(filtro, pagina).stream().map(this::reconstruir).toList();
    }

    @Override
    public List<ProgramacionRutaDominio> relacionados(FiltroProgramacionRuta filtro) {
        return dao.relacionados(filtro).stream().map(this::reconstruir).toList();
    }

    @Override
    public long contar(FiltroProgramacionRuta filtro) {
        return dao.contar(filtro);
    }
}
