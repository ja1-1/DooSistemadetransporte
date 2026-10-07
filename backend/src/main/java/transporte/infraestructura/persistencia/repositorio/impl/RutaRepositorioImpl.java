package transporte.infraestructura.persistencia.repositorio.impl;

import transporte.dao.datos.entidad.ProgramacionRutaDAO;
import transporte.dao.datos.entidad.RutaDAO;
import transporte.dao.datos.entidad.SalidaDAO;
import transporte.dao.factoria.DAOFactory;
import transporte.dominio.RutaDominio;
import transporte.dominio.consulta.FiltroProgramacionRuta;
import transporte.dominio.consulta.FiltroRuta;
import transporte.dominio.consulta.FiltroSalida;
import transporte.dominio.repositorio.RutaRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.entidad.RutaEntidad;
import transporte.infraestructura.persistencia.assembler.impl.RutaEntidadAssembler;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RutaRepositorioImpl implements RutaRepositorio {
    private final RutaDAO dao;
    private final ProgramacionRutaDAO programaciones;
    private final SalidaDAO salidas;
    private final RutaEntidadAssembler assembler;

    public RutaRepositorioImpl(DAOFactory fabrica, RutaEntidadAssembler assembler) {
        dao = fabrica.ruta();
        programaciones = fabrica.programacionRuta();
        salidas = fabrica.salida();
        this.assembler = assembler;
    }

    private RutaDominio reconstruir(RutaEntidad rutaEntidad) {
        return assembler.convertirADominio(rutaEntidad);
    }

    @Override
    @Transactional
    public void guardar(RutaDominio ruta) {
        dao.guardar(assembler.convertirAEntidad(ruta));
    }

    @Override
    public Optional<RutaDominio> buscarPorId(UUID id) {
        return dao.buscarPorId(id).map(this::reconstruir);
    }

    @Override
    public List<RutaDominio> buscar(FiltroRuta filtro, Pagina pagina) {
        return dao.buscar(filtro, pagina).stream().map(this::reconstruir).toList();
    }

    @Override
    public List<RutaDominio> relacionados(FiltroRuta filtro) {
        return dao.relacionados(filtro).stream().map(this::reconstruir).toList();
    }

    @Override
    public void eliminar(UUID id) {
        dao.eliminar(id);
    }

    @Override
    public boolean estaEnUso(UUID id) {
        return programaciones.contar(FiltroProgramacionRuta.todos().conRutaId(id)) > 0
                || salidas.contar(FiltroSalida.todos().conRutaId(id)) > 0;
    }

    @Override
    public long contar(FiltroRuta filtro) {
        return dao.contar(filtro);
    }
}
