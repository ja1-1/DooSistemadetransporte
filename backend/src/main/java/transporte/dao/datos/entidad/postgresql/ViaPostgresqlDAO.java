package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.ViaDAO;
import transporte.dominio.valor.Pagina;
import transporte.entidad.ViaEntidad;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ViaPostgresqlDAO implements ViaDAO {
    private final JdbcTemplate jdbc;
    private static final RowMapper<ViaEntidad> MAPEAR =
            (resultSet, numero) ->
                    new ViaEntidad(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("nombre"),
                            resultSet.getString("descripcion"),
                            resultSet.getBoolean("activo"));

    public ViaPostgresqlDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void guardar(ViaEntidad viaEntidad) {
        jdbc.update(
                "INSERT INTO via(id,nombre,descripcion,activo) VALUES(?,?,?,?) ON CONFLICT(id) DO"
                    + " UPDATE SET"
                    + " nombre=EXCLUDED.nombre,descripcion=EXCLUDED.descripcion,activo=EXCLUDED.activo",
                viaEntidad.id(),
                viaEntidad.nombre(),
                viaEntidad.descripcion(),
                viaEntidad.activo());
    }

    @Override
    public Optional<ViaEntidad> buscarPorId(UUID id) {
        return jdbc
                .query("SELECT id,nombre,descripcion,activo FROM via WHERE id=?", MAPEAR, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<ViaEntidad> buscar(
            @Nullable String nombre,
            @Nullable String descripcion,
            @Nullable Boolean activo,
            Pagina pagina) {
        var parametros = new ArrayList<Object>();
        String sql = "SELECT id,nombre,descripcion,activo FROM via WHERE TRUE";
        if (nombre != null) {
            sql += " AND strpos(lower(nombre),lower(?))>0";
            parametros.add(nombre);
        }
        if (descripcion != null) {
            sql += " AND descripcion=?";
            parametros.add(descripcion);
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
        jdbc.update("DELETE FROM via WHERE id=?", id);
    }

    @Override
    public boolean estaEnUso(UUID id) {
        return Boolean.TRUE.equals(
                jdbc.queryForObject(
                        "SELECT EXISTS(SELECT 1 FROM ruta WHERE via_id = ?)", Boolean.class, id));
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return Boolean.TRUE.equals(
                jdbc.queryForObject(
                        "SELECT EXISTS(SELECT 1 FROM via WHERE normalizar_via(nombre) ="
                                + " normalizar_via(?))",
                        Boolean.class,
                        nombre));
    }
}
