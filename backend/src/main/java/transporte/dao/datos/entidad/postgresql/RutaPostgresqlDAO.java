package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.RutaDAO;
import transporte.dominio.consulta.FiltroRuta;
import transporte.dominio.valor.Pagina;
import transporte.entidad.RutaEntidad;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RutaPostgresqlDAO implements RutaDAO {
    private static final String COLUMNAS =
            "id,nombre,descripcion,activa,origen_lugar_id,destino_lugar_id,distancia_total_km,tipo_ruta_id,via_id";
    private final JdbcTemplate jdbc;

    public RutaPostgresqlDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private RutaEntidad convertir(ResultSet fila, int numero) throws SQLException {
        return new RutaEntidad(
                fila.getObject("id", UUID.class),
                fila.getString("nombre"),
                fila.getString("descripcion"),
                fila.getObject("activa", Boolean.class),
                fila.getObject("origen_lugar_id", UUID.class),
                fila.getObject("destino_lugar_id", UUID.class),
                fila.getBigDecimal("distancia_total_km"),
                fila.getObject("tipo_ruta_id", UUID.class),
                fila.getObject("via_id", UUID.class));
    }

    private CondicionesSql condiciones(FiltroRuta filtros) {
        var condiciones = new CondicionesSql();
        condiciones.igual("id", filtros.id());
        condiciones.igual("nombre", filtros.nombre());
        condiciones.igual("descripcion", filtros.descripcion());
        condiciones.igual("activa", filtros.activa());
        condiciones.igual("origen_lugar_id", filtros.origenLugarId());
        condiciones.igual("destino_lugar_id", filtros.destinoLugarId());
        condiciones.igual("distancia_total_km", filtros.distanciaTotalKm());
        condiciones.igual(
                "tipo_ruta_id", filtros.tipoRuta() == null ? null : filtros.tipoRuta().getId());
        condiciones.igual("via_id", filtros.viaId());
        if (filtros.municipioId() != null) {
            condiciones.agregar(
                    "EXISTS (SELECT 1 FROM lugar l JOIN sector s ON s.id=l.sector_id WHERE l.id IN"
                            + " (ruta.origen_lugar_id,ruta.destino_lugar_id) AND s.municipio_id=?)",
                    filtros.municipioId());
        }
        return condiciones;
    }

    @Override
    public void guardar(RutaEntidad entidad) {
        jdbc.update(
                "INSERT INTO"
                    + " ruta(id,nombre,descripcion,activa,origen_lugar_id,destino_lugar_id,distancia_total_km,tipo_ruta_id,via_id)"
                    + " VALUES(?,?,?,?,?,?,?,?,?) ON CONFLICT(id) DO UPDATE SET"
                    + " nombre=EXCLUDED.nombre,descripcion=EXCLUDED.descripcion,activa=EXCLUDED.activa,origen_lugar_id=EXCLUDED.origen_lugar_id,destino_lugar_id=EXCLUDED.destino_lugar_id,distancia_total_km=EXCLUDED.distancia_total_km,tipo_ruta_id=EXCLUDED.tipo_ruta_id,via_id=EXCLUDED.via_id",
                entidad.id(),
                entidad.nombre(),
                entidad.descripcion(),
                entidad.activa(),
                entidad.origenLugarId(),
                entidad.destinoLugarId(),
                entidad.distanciaTotalKm(),
                entidad.tipoRutaId(),
                entidad.viaId());
    }

    @Override
    public Optional<RutaEntidad> buscarPorId(UUID id) {
        return relacionados(FiltroRuta.todos().conId(id)).stream().findFirst();
    }

    @Override
    public List<RutaEntidad> buscar(FiltroRuta filtros, @Nullable Pagina pagina) {
        var condiciones = condiciones(filtros);
        String sql =
                "SELECT "
                        + COLUMNAS
                        + " FROM ruta"
                        + condiciones.texto()
                        + " ORDER BY id"
                        + condiciones.paginar(pagina);
        var registros = jdbc.query(sql, this::convertir, condiciones.parametros());
        return registros;
    }

    @Override
    public List<RutaEntidad> relacionados(FiltroRuta filtros) {
        return buscar(filtros, null);
    }

    @Override
    public long contar(FiltroRuta filtros) {
        var condiciones = condiciones(filtros);
        return ResultadoSql.requerido(
                jdbc.queryForObject(
                        "SELECT count(*) FROM ruta" + condiciones.texto(),
                        Long.class,
                        condiciones.parametros()));
    }

    @Override
    public void eliminar(UUID id) {
        jdbc.update("DELETE FROM ruta WHERE id=?", id);
    }
}
