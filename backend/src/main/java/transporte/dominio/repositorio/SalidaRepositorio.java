package transporte.dominio.repositorio;

import transporte.dominio.Incidencia;
import transporte.dominio.SalidaDominio;
import transporte.dominio.consulta.FiltroIncidencia;
import transporte.dominio.consulta.FiltroSalida;
import transporte.dominio.enums.EstadoSalida;
import transporte.dominio.valor.IncidenciaDeSalida;
import transporte.dominio.valor.Pagina;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalidaRepositorio {
    void guardar(SalidaDominio salida);

    Optional<SalidaDominio> buscarPorId(UUID id);

    List<SalidaDominio> buscar(FiltroSalida filtro, Pagina pagina);

    List<SalidaDominio> relacionados(FiltroSalida filtro);

    long contar(FiltroSalida filtro);

    default boolean existePorAsignacionEInstante(
            UUID asignacionId, LocalDateTime instanteSalidaProgramado) {
        return !relacionados(
                        FiltroSalida.todos()
                                .conAsignacionId(asignacionId)
                                .conInstanteSalidaProgramado(instanteSalidaProgramado))
                .isEmpty();
    }

    default boolean existePorProgramacionEInstante(
            UUID programacionRutaId, LocalDateTime instanteSalidaProgramado) {
        return !relacionados(
                        FiltroSalida.todos()
                                .conProgramacionRutaId(programacionRutaId)
                                .conInstanteSalidaProgramado(instanteSalidaProgramado))
                .isEmpty();
    }

    default List<SalidaDominio> buscarNoCanceladasPorAsignacion(UUID asignacionId) {
        return relacionados(FiltroSalida.todos().conAsignacionId(asignacionId)).stream()
                .filter(salida -> salida.getEstadoSalida() != EstadoSalida.CANCELADA)
                .toList();
    }

    default List<SalidaDominio> buscarProgramadasPorAsignacionDesde(
            UUID asignacionId, LocalDate fecha) {
        return relacionados(
                        FiltroSalida.todos()
                                .conAsignacionId(asignacionId)
                                .conEstadoSalida(EstadoSalida.PROGRAMADA))
                .stream()
                .filter(
                        salida ->
                                !salida.getVentana()
                                        .instanteSalidaProgramado()
                                        .toLocalDate()
                                        .isBefore(fecha))
                .toList();
    }

    default boolean existenPendientesPorRuta(UUID rutaId) {
        return relacionados(FiltroSalida.todos().conRutaId(rutaId)).stream()
                .anyMatch(salida -> !salida.getEstadoSalida().esDefinitivo());
    }

    long contarIncidencias(FiltroIncidencia filtro);

    Optional<IncidenciaDeSalida> consultarIncidencia(UUID id);

    List<IncidenciaDeSalida> consultarIncidencias(FiltroIncidencia filtro, Pagina pagina);

    default Optional<Incidencia> buscarIncidenciaPorId(UUID id) {
        return consultarIncidencia(id).map(IncidenciaDeSalida::incidencia);
    }

    default List<Incidencia> buscarIncidencias(FiltroIncidencia filtro, Pagina pagina) {
        return consultarIncidencias(filtro, pagina).stream()
                .map(IncidenciaDeSalida::incidencia)
                .toList();
    }
}
