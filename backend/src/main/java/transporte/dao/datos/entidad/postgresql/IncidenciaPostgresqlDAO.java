package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.IncidenciaDAO;
import transporte.dominio.consulta.FiltroIncidencia;
import transporte.dominio.valor.Pagina;
import transporte.entidad.IncidenciaEntidad;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class IncidenciaPostgresqlDAO implements IncidenciaDAO {
    private static final String COLUMNAS =
            "id,salida_id,tipo_incidencia_id,instante_ocurrencia,instante_reporte,descripcion,responsable_id,responsable_nombre,responsable_rol";
    private final JdbcTemplate jdbc;

    public IncidenciaPostgresqlDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private IncidenciaEntidad convertir(ResultSet fila, int numero) throws SQLException {
        return new IncidenciaEntidad(
                fila.getObject("id", UUID.class),
                fila.getObject("salida_id", UUID.class),
                fila.getObject("tipo_incidencia_id", UUID.class),
                fila.getObject("instante_ocurrencia", LocalDateTime.class),
                fila.getObject("instante_reporte", LocalDateTime.class),
                fila.getString("descripcion"),
                fila.getObject("responsable_id", UUID.class),
                fila.getString("responsable_nombre"),
                fila.getString("responsable_rol"));
    }

    private CondicionesSql condiciones(FiltroIncidencia filtros) {
        var condiciones = new CondicionesSql();
        condiciones.igual("id", filtros.id());
        condiciones.igual("salida_id", filtros.salidaId());
        condiciones.igual(
                "tipo_incidencia_id",
                filtros.tipoIncidencia() == null ? null : filtros.tipoIncidencia().getId());
        condiciones.igual("instante_ocurrencia", filtros.instanteOcurrencia());
        condiciones.igual("instante_reporte", filtros.instanteReporte());
        condiciones.igual("descripcion", filtros.descripcion());
        condiciones.igual("responsable_id", filtros.responsableId());
        condiciones.igual("responsable_nombre", filtros.responsableNombre());
        condiciones.igual("responsable_rol", filtros.responsableRol());
        condiciones
                .desde("instante_ocurrencia", filtros.ocurrenciaDesde())
                .antes("instante_ocurrencia", filtros.ocurrenciaHasta())
                .en("salida_id", filtros.salidaIds());
        return condiciones;
    }

    @Override
    public void guardar(IncidenciaEntidad entidad) {
        int insertadas =
                jdbc.update(
                        "INSERT INTO"
                            + " incidencia(id,salida_id,tipo_incidencia_id,instante_ocurrencia,instante_reporte,descripcion,responsable_id,responsable_nombre,responsable_rol)"
                            + " VALUES(?,?,?,?,?,?,?,?,?) ON CONFLICT(id) DO NOTHING",
                        entidad.id(),
                        entidad.salidaId(),
                        entidad.tipoIncidenciaId(),
                        entidad.instanteOcurrencia(),
                        entidad.instanteReporte(),
                        entidad.descripcion(),
                        entidad.responsableId(),
                        entidad.responsableNombre(),
                        entidad.responsableRol());
        if (insertadas == 0 && !buscarPorId(entidad.id()).equals(Optional.of(entidad))) {
            throw new DataIntegrityViolationException(CatalogoMensajes.INCIDENCIA_INMUTABLE);
        }
    }

    @Override
    public Optional<IncidenciaEntidad> buscarPorId(UUID id) {
        return relacionados(FiltroIncidencia.todos().conId(id)).stream().findFirst();
    }

    @Override
    public List<IncidenciaEntidad> buscar(FiltroIncidencia filtros, @Nullable Pagina pagina) {
        var condiciones = condiciones(filtros);
        String sql =
                "SELECT "
                        + COLUMNAS
                        + " FROM incidencia"
                        + condiciones.texto()
                        + " ORDER BY id"
                        + condiciones.paginar(pagina);
        var registros = jdbc.query(sql, this::convertir, condiciones.parametros());
        return registros;
    }

    @Override
    public List<IncidenciaEntidad> relacionados(FiltroIncidencia filtros) {
        return buscar(filtros, null);
    }

    @Override
    public long contar(FiltroIncidencia filtros) {
        var condiciones = condiciones(filtros);
        return ResultadoSql.requerido(
                jdbc.queryForObject(
                        "SELECT count(*) FROM incidencia" + condiciones.texto(),
                        Long.class,
                        condiciones.parametros()));
    }
}
