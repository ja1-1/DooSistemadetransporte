package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.IncidenciaDAO;
import transporte.dao.datos.entidad.SalidaDAO;
import transporte.dominio.consulta.FiltroIncidencia;
import transporte.dominio.consulta.FiltroSalida;
import transporte.dominio.valor.Pagina;
import transporte.entidad.IncidenciaEntidad;
import transporte.entidad.SalidaEntidad;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class SalidaPostgresqlDAO implements SalidaDAO {
    private static final String COLUMNAS =
            "id,ruta_id,nombre_ruta,asignacion_id,instante_salida_programado,instante_llegada_estimado,instante_salida_real,instante_llegada_real,estado_salida_id,programacion_ruta_id";
    private final JdbcTemplate jdbc;
    private final IncidenciaDAO incidencias;

    public SalidaPostgresqlDAO(JdbcTemplate jdbc, IncidenciaDAO incidencias) {
        this.jdbc = jdbc;
        this.incidencias = incidencias;
    }

    private SalidaEntidad convertir(ResultSet fila, int numero) throws SQLException {
        return new SalidaEntidad(
                fila.getObject("id", UUID.class),
                fila.getObject("ruta_id", UUID.class),
                fila.getString("nombre_ruta"),
                fila.getObject("asignacion_id", UUID.class),
                fila.getObject("instante_salida_programado", LocalDateTime.class),
                fila.getObject("instante_llegada_estimado", LocalDateTime.class),
                fila.getObject("instante_salida_real", LocalDateTime.class),
                fila.getObject("instante_llegada_real", LocalDateTime.class),
                fila.getObject("estado_salida_id", UUID.class),
                fila.getObject("programacion_ruta_id", UUID.class),
                List.of());
    }

    private CondicionesSql condiciones(FiltroSalida filtros) {
        var condiciones = new CondicionesSql();
        condiciones.igual("id", filtros.id());
        condiciones.igual("ruta_id", filtros.rutaId());
        condiciones.igual("nombre_ruta", filtros.nombreRuta());
        condiciones.igual("asignacion_id", filtros.asignacionId());
        condiciones.igual("instante_salida_programado", filtros.instanteSalidaProgramado());
        condiciones.igual("instante_llegada_estimado", filtros.instanteLlegadaEstimado());
        condiciones.igual("instante_salida_real", filtros.instanteSalidaReal());
        condiciones.igual("instante_llegada_real", filtros.instanteLlegadaReal());
        condiciones.igual(
                "estado_salida_id",
                filtros.estadoSalida() == null ? null : filtros.estadoSalida().getId());
        condiciones.igual("programacion_ruta_id", filtros.programacionRutaId());
        condiciones
                .desde("instante_salida_programado", filtros.salidaDesde())
                .antes("instante_salida_programado", filtros.salidaHasta())
                .en("asignacion_id", filtros.asignacionIds());
        return condiciones;
    }

    @Override
    public void guardar(SalidaEntidad entidad) {
        jdbc.update(
                "INSERT INTO"
                    + " salida(id,ruta_id,nombre_ruta,asignacion_id,instante_salida_programado,instante_llegada_estimado,instante_salida_real,instante_llegada_real,estado_salida_id,programacion_ruta_id)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?) ON CONFLICT(id) DO UPDATE SET"
                    + " ruta_id=EXCLUDED.ruta_id,nombre_ruta=EXCLUDED.nombre_ruta,asignacion_id=EXCLUDED.asignacion_id,instante_salida_programado=EXCLUDED.instante_salida_programado,instante_llegada_estimado=EXCLUDED.instante_llegada_estimado,instante_salida_real=EXCLUDED.instante_salida_real,instante_llegada_real=EXCLUDED.instante_llegada_real,estado_salida_id=EXCLUDED.estado_salida_id,programacion_ruta_id=EXCLUDED.programacion_ruta_id",
                entidad.id(),
                entidad.rutaId(),
                entidad.nombreRuta(),
                entidad.asignacionId(),
                entidad.instanteSalidaProgramado(),
                entidad.instanteLlegadaEstimado(),
                entidad.instanteSalidaReal(),
                entidad.instanteLlegadaReal(),
                entidad.estadoSalidaId(),
                entidad.programacionRutaId());
        for (var incidencia : entidad.incidencias()) {
            incidencias.guardar(incidencia.conSalidaId(entidad.id()));
        }
    }

    @Override
    public Optional<SalidaEntidad> buscarPorId(UUID id) {
        return relacionados(FiltroSalida.todos().conId(id)).stream().findFirst();
    }

    @Override
    public List<SalidaEntidad> buscar(FiltroSalida filtros, @Nullable Pagina pagina) {
        var condiciones = condiciones(filtros);
        String sql =
                "SELECT "
                        + COLUMNAS
                        + " FROM salida"
                        + condiciones.texto()
                        + " ORDER BY id"
                        + condiciones.paginar(pagina);
        var registros = jdbc.query(sql, this::convertir, condiciones.parametros());
        if (registros.isEmpty()) {
            return List.of();
        }
        var ids = registros.stream().map(SalidaEntidad::id).collect(Collectors.toSet());
        var historiales =
                incidencias.relacionados(FiltroIncidencia.todos().conSalidaIds(ids)).stream()
                        .collect(Collectors.groupingBy(IncidenciaEntidad::salidaId));
        return registros.stream()
                .map(
                        registro ->
                                registro.conIncidencias(
                                        historiales.getOrDefault(registro.id(), List.of())))
                .toList();
    }

    @Override
    public List<SalidaEntidad> relacionados(FiltroSalida filtros) {
        return buscar(filtros, null);
    }

    @Override
    public long contar(FiltroSalida filtros) {
        var condiciones = condiciones(filtros);
        return ResultadoSql.requerido(
                jdbc.queryForObject(
                        "SELECT count(*) FROM salida" + condiciones.texto(),
                        Long.class,
                        condiciones.parametros()));
    }
}
