package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.ProgramacionRutaDAO;
import transporte.dominio.consulta.FiltroProgramacionRuta;
import transporte.dominio.valor.Pagina;
import transporte.entidad.ProgramacionRutaEntidad;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ProgramacionRutaPostgresqlDAO implements ProgramacionRutaDAO {
    private static final String COLUMNAS =
            "id,ruta_id,asignacion_id,hora_salida,hora_ultima_salida,intervalo_minutos,duracion_estimada,vigencia_desde,vigencia_hasta,activa";
    private final JdbcTemplate jdbc;

    public ProgramacionRutaPostgresqlDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private ProgramacionRutaEntidad convertir(ResultSet fila, int numero) throws SQLException {
        return new ProgramacionRutaEntidad(
                fila.getObject("id", UUID.class),
                fila.getObject("ruta_id", UUID.class),
                fila.getObject("asignacion_id", UUID.class),
                fila.getObject("hora_salida", LocalTime.class),
                fila.getObject("hora_ultima_salida", LocalTime.class),
                fila.getObject("intervalo_minutos", Integer.class),
                fila.getObject("duracion_estimada", Integer.class),
                fila.getObject("vigencia_desde", LocalDate.class),
                fila.getObject("vigencia_hasta", LocalDate.class),
                fila.getObject("activa", Boolean.class),
                Set.of());
    }

    private CondicionesSql condiciones(FiltroProgramacionRuta filtros) {
        var condiciones = new CondicionesSql();
        condiciones.igual("id", filtros.id());
        condiciones.igual("ruta_id", filtros.rutaId());
        condiciones.igual("asignacion_id", filtros.asignacionId());
        condiciones.igual("hora_salida", filtros.horaSalida());
        condiciones.igual("hora_ultima_salida", filtros.horaUltimaSalida());
        condiciones.igual("intervalo_minutos", filtros.intervaloMinutos());
        condiciones.igual("duracion_estimada", filtros.duracionEstimada());
        condiciones.igual("vigencia_desde", filtros.vigenciaDesde());
        condiciones.igual("vigencia_hasta", filtros.vigenciaHasta());
        condiciones.igual("activa", filtros.activa());
        return condiciones;
    }

    @Override
    public void guardar(ProgramacionRutaEntidad entidad) {
        jdbc.update(
                "INSERT INTO"
                    + " programacion_ruta(id,ruta_id,asignacion_id,hora_salida,hora_ultima_salida,intervalo_minutos,duracion_estimada,vigencia_desde,vigencia_hasta,activa)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?) ON CONFLICT(id) DO UPDATE SET"
                    + " ruta_id=EXCLUDED.ruta_id,asignacion_id=EXCLUDED.asignacion_id,hora_salida=EXCLUDED.hora_salida,hora_ultima_salida=EXCLUDED.hora_ultima_salida,intervalo_minutos=EXCLUDED.intervalo_minutos,duracion_estimada=EXCLUDED.duracion_estimada,vigencia_desde=EXCLUDED.vigencia_desde,vigencia_hasta=EXCLUDED.vigencia_hasta,activa=EXCLUDED.activa",
                entidad.id(),
                entidad.rutaId(),
                entidad.asignacionId(),
                entidad.horaSalida(),
                entidad.horaUltimaSalida(),
                entidad.intervaloMinutos(),
                entidad.duracionEstimada(),
                entidad.vigenciaDesde(),
                entidad.vigenciaHasta(),
                entidad.activa());
        guardarDias(entidad.id(), entidad.dias());
    }

    @Override
    public Optional<ProgramacionRutaEntidad> buscarPorId(UUID id) {
        return relacionados(FiltroProgramacionRuta.todos().conId(id)).stream().findFirst();
    }

    @Override
    public List<ProgramacionRutaEntidad> buscar(
            FiltroProgramacionRuta filtros, @Nullable Pagina pagina) {
        var condiciones = condiciones(filtros);
        String sql =
                "SELECT "
                        + COLUMNAS
                        + " FROM programacion_ruta"
                        + condiciones.texto()
                        + " ORDER BY id"
                        + condiciones.paginar(pagina);
        var registros = jdbc.query(sql, this::convertir, condiciones.parametros());
        if (registros.isEmpty()) {
            return List.of();
        }
        var ids = registros.stream().map(ProgramacionRutaEntidad::id).toList();
        var condicionesDias = new CondicionesSql().en("programacion_ruta_id", ids);
        var dias =
                jdbc
                        .query(
                                "SELECT programacion_ruta_id,dia_semana FROM programacion_ruta_dia"
                                        + condicionesDias.texto(),
                                (fila, numero) ->
                                        new DiaProgramado(
                                                fila.getObject("programacion_ruta_id", UUID.class),
                                                DayOfWeek.of(
                                                        DIAS.indexOf(fila.getString("dia_semana"))
                                                                + 1)),
                                condicionesDias.parametros())
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        DiaProgramado::programacionId,
                                        Collectors.mapping(
                                                DiaProgramado::dia, Collectors.toSet())));
        return registros.stream()
                .map(registro -> registro.conDias(dias.getOrDefault(registro.id(), Set.of())))
                .toList();
    }

    @Override
    public List<ProgramacionRutaEntidad> relacionados(FiltroProgramacionRuta filtros) {
        return buscar(filtros, null);
    }

    @Override
    public long contar(FiltroProgramacionRuta filtros) {
        var condiciones = condiciones(filtros);
        return ResultadoSql.requerido(
                jdbc.queryForObject(
                        "SELECT count(*) FROM programacion_ruta" + condiciones.texto(),
                        Long.class,
                        condiciones.parametros()));
    }

    private static final List<String> DIAS =
            List.of("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");

    private record DiaProgramado(UUID programacionId, DayOfWeek dia) {}

    private void guardarDias(UUID id, Set<DayOfWeek> dias) {
        jdbc.update("DELETE FROM programacion_ruta_dia WHERE programacion_ruta_id=?", id);
        for (var dia : dias) {
            jdbc.update(
                    "INSERT INTO programacion_ruta_dia(programacion_ruta_id,dia_semana) VALUES"
                            + " (?,?)",
                    id,
                    DIAS.get(dia.getValue() - 1));
        }
    }
}
