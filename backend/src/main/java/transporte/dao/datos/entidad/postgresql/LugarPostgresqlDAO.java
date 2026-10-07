package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.LugarDAO;
import transporte.dominio.valor.Pagina;
import transporte.entidad.LugarEntidad;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LugarPostgresqlDAO implements LugarDAO {
    private final JdbcTemplate jdbc;
    private static final RowMapper<LugarEntidad> MAPEAR =
            (resultSet, numero) ->
                    new LugarEntidad(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("nombre"),
                            resultSet.getObject("sector_id", UUID.class),
                            resultSet.getBoolean("activo"));

    public LugarPostgresqlDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void guardar(LugarEntidad lugarEntidad) {
        jdbc.update(
                "INSERT INTO lugar(id,nombre,sector_id,activo) VALUES(?,?,?,?) ON CONFLICT(id) DO"
                    + " UPDATE SET"
                    + " nombre=EXCLUDED.nombre,sector_id=EXCLUDED.sector_id,activo=EXCLUDED.activo",
                lugarEntidad.id(),
                lugarEntidad.nombre(),
                lugarEntidad.sectorId(),
                lugarEntidad.activo());
    }

    @Override
    public Optional<LugarEntidad> buscarPorId(UUID id) {
        return jdbc
                .query("SELECT id,nombre,sector_id,activo FROM lugar WHERE id=?", MAPEAR, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<LugarEntidad> buscar(
            @Nullable String nombre,
            @Nullable UUID sectorId,
            @Nullable Boolean activo,
            Pagina pagina) {
        var parametros = new ArrayList<Object>();
        String sql = "SELECT id,nombre,sector_id,activo FROM lugar WHERE TRUE";
        if (nombre != null) {
            sql += " AND strpos(lower(nombre),lower(?))>0";
            parametros.add(nombre);
        }
        if (sectorId != null) {
            sql += " AND sector_id=?";
            parametros.add(sectorId);
        }
        if (activo != null) {
            sql += " AND activo=?";
            parametros.add(activo);
        }
        sql += " ORDER BY nombre,id LIMIT ? OFFSET ?";
        parametros.add(pagina.tamano());
        parametros.add(pagina.desplazamiento());
        return jdbc.query(sql, MAPEAR, parametros.toArray());
    }

    @Override
    public void eliminar(UUID id) {
        jdbc.update("DELETE FROM lugar WHERE id=?", id);
    }

    @Override
    public boolean estaEnUso(UUID id) {
        return Boolean.TRUE.equals(
                jdbc.queryForObject(
                        "SELECT EXISTS(SELECT 1 FROM ruta WHERE origen_lugar_id = ? OR"
                                + " destino_lugar_id = ?)",
                        Boolean.class,
                        id,
                        id));
    }

    @Override
    public boolean existePorSectorYNombre(UUID sectorId, String nombre) {
        return Boolean.TRUE.equals(
                jdbc.queryForObject(
                        "SELECT EXISTS(SELECT 1 FROM lugar WHERE sector_id = ? AND nombre = ?)",
                        Boolean.class,
                        sectorId,
                        nombre));
    }
}
