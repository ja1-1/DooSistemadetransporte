package transporte.infraestructura.persistencia.repositorio.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import transporte.dao.datos.entidad.IncidenciaDAO;
import transporte.dao.datos.entidad.SalidaDAO;
import transporte.dao.factoria.DAOFactory;
import transporte.dominio.SalidaDominio;
import transporte.dominio.consulta.FiltroIncidencia;
import transporte.dominio.consulta.FiltroSalida;
import transporte.dominio.repositorio.SalidaRepositorio;
import transporte.dominio.valor.IncidenciaDeSalida;
import transporte.dominio.valor.Pagina;
import transporte.entidad.SalidaEntidad;
import transporte.infraestructura.persistencia.assembler.impl.IncidenciaEntidadAssembler;
import transporte.infraestructura.persistencia.assembler.impl.SalidaEntidadAssembler;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class SalidaRepositorioImpl implements SalidaRepositorio {
    private final SalidaDAO dao;
    private final IncidenciaDAO incidencias;
    private final SalidaEntidadAssembler assembler;

    public SalidaRepositorioImpl(
            DAOFactory fabrica,
            SalidaEntidadAssembler assembler,
            IncidenciaEntidadAssembler incidencia) {
        dao = fabrica.salida();
        incidencias = fabrica.incidencia();
        this.assembler = assembler;
        this.incidencia = incidencia;
    }

    private SalidaDominio reconstruir(SalidaEntidad entidad) {
        return assembler.convertirADominio(entidad);
    }

    @Override
    @Transactional
    public void guardar(SalidaDominio salida) {
        var registradas =
                Set.copyOf(
                        incidencias.relacionados(
                                FiltroIncidencia.todos().conSalidaIds(Set.of(salida.getId()))));
        // Solo las incidencias nuevas resuelven su identidad antes de escribir el agregado.
        salida.asegurarIdentidadesIncidencias(
                registrada ->
                        registradas.contains(
                                incidencia.convertirAEntidad(registrada, salida.getId())),
                candidata -> incidencias.buscarPorId(candidata).isPresent());
        dao.guardar(assembler.convertirAEntidad(salida));
    }

    @Override
    public Optional<SalidaDominio> buscarPorId(UUID id) {
        return dao.buscarPorId(id).map(this::reconstruir);
    }

    @Override
    public List<SalidaDominio> buscar(FiltroSalida filtro, Pagina pagina) {
        return dao.buscar(filtro, pagina).stream().map(this::reconstruir).toList();
    }

    @Override
    public long contar(FiltroSalida filtro) {
        return dao.contar(filtro);
    }

    @Override
    public List<SalidaDominio> relacionados(FiltroSalida filtro) {
        return dao.relacionados(filtro).stream().map(this::reconstruir).toList();
    }

    @Override
    public long contarIncidencias(FiltroIncidencia filtro) {
        return incidencias.contar(filtro);
    }

    private final IncidenciaEntidadAssembler incidencia;

    @Override
    public Optional<IncidenciaDeSalida> consultarIncidencia(UUID id) {
        return incidencias
                .buscarPorId(id)
                .map(
                        incidenciaEntidad ->
                                new IncidenciaDeSalida(
                                        incidenciaEntidad.salidaId(),
                                        incidencia.convertirADominio(incidenciaEntidad)));
    }

    @Override
    public List<IncidenciaDeSalida> consultarIncidencias(FiltroIncidencia filtro, Pagina pagina) {
        return incidencias.buscar(filtro, pagina).stream()
                .map(
                        incidenciaEntidad ->
                                new IncidenciaDeSalida(
                                        incidenciaEntidad.salidaId(),
                                        incidencia.convertirADominio(incidenciaEntidad)))
                .toList();
    }
}
