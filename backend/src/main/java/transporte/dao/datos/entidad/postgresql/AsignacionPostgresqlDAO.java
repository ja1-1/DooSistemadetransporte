package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.AsignacionDAO;
import transporte.dominio.consulta.FiltroAsignacion;
import transporte.dominio.enums.EstadoAsignacion;
import transporte.dominio.valor.Pagina;
import transporte.entidad.AsignacionEntidad;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AsignacionPostgresqlDAO implements AsignacionDAO {
    private static final String COLUMNAS = "id,conductor_id,vehiculo_id,fecha_inicio,fecha_fin";
    private final JdbcTemplate jdbc;

    public AsignacionPostgresqlDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private AsignacionEntidad convertir(ResultSet fila, int numero) throws SQLException {
        return new AsignacionEntidad(
                fila.getObject("id", UUID.class),
                fila.getObject("conductor_id", UUID.class),
                fila.getObject("vehiculo_id", UUID.class),
                fila.getObject("fecha_inicio", LocalDate.class),
                fila.getObject("fecha_fin", LocalDate.class));
    }

    private CondicionesSql condiciones(FiltroAsignacion filtros) {
        var condiciones = new CondicionesSql();
        condiciones.igual("id", filtros.id());
        condiciones.igual("conductor_id", filtros.conductorId());
        condiciones.igual("vehiculo_id", filtros.vehiculoId());
        condiciones.igual("fecha_inicio", filtros.fechaInicio());
        condiciones.igual("fecha_fin", filtros.fechaFin());
        if (filtros.estado() != null) {
            if (filtros.estado() == EstadoAsignacion.FINALIZADA) {
                condiciones.agregar(
                        "fecha_fin < ?", Objects.requireNonNull(filtros.fechaReferencia()));
            } else {
                condiciones.agregar(
                        "(fecha_fin IS NULL OR fecha_fin >= ?)",
                        Objects.requireNonNull(filtros.fechaReferencia()));
            }
        }
        return condiciones;
    }

    @Override
    public void guardar(AsignacionEntidad entidad) {
        jdbc.update(
                "INSERT INTO asignacion(id,conductor_id,vehiculo_id,fecha_inicio,fecha_fin)"
                    + " VALUES(?,?,?,?,?) ON CONFLICT(id) DO UPDATE SET"
                    + " conductor_id=EXCLUDED.conductor_id,vehiculo_id=EXCLUDED.vehiculo_id,fecha_inicio=EXCLUDED.fecha_inicio,fecha_fin=EXCLUDED.fecha_fin",
                entidad.id(),
                entidad.conductorId(),
                entidad.vehiculoId(),
                entidad.fechaInicio(),
                entidad.fechaFin());
    }

    @Override
    public Optional<AsignacionEntidad> buscarPorId(UUID id) {
        return relacionados(FiltroAsignacion.todos().conId(id)).stream().findFirst();
    }

    @Override
    public List<AsignacionEntidad> buscar(FiltroAsignacion filtros, @Nullable Pagina pagina) {
        var condiciones = condiciones(filtros);
        String sql =
                "SELECT "
                        + COLUMNAS
                        + " FROM asignacion"
                        + condiciones.texto()
                        + " ORDER BY id"
                        + condiciones.paginar(pagina);
        var registros = jdbc.query(sql, this::convertir, condiciones.parametros());
        return registros;
    }

    @Override
    public List<AsignacionEntidad> relacionados(FiltroAsignacion filtros) {
        return buscar(filtros, null);
    }

    @Override
    public long contar(FiltroAsignacion filtros) {
        var condiciones = condiciones(filtros);
        return ResultadoSql.requerido(
                jdbc.queryForObject(
                        "SELECT count(*) FROM asignacion" + condiciones.texto(),
                        Long.class,
                        condiciones.parametros()));
    }
}
