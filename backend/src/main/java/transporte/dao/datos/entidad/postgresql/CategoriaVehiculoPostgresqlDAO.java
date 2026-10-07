package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.CategoriaVehiculoDAO;
import transporte.entidad.CategoriaVehiculoEntidad;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoriaVehiculoPostgresqlDAO implements CategoriaVehiculoDAO {
    private static final String CAMPOS = "id, nombre, categoria_licencia_minima";
    private static final RowMapper<CategoriaVehiculoEntidad> MAPEAR =
            (fila, numero) ->
                    new CategoriaVehiculoEntidad(
                            fila.getObject("id", UUID.class),
                            fila.getString("nombre"),
                            fila.getString("categoria_licencia_minima"));
    private final JdbcTemplate jdbc;

    public CategoriaVehiculoPostgresqlDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public UUID nuevoIdentificador() {
        return ResultadoSql.requerido(jdbc.queryForObject("SELECT gen_random_uuid()", UUID.class));
    }

    @Override
    public boolean crear(CategoriaVehiculoEntidad categoria) {
        return jdbc.update(
                        "INSERT INTO categoria_vehiculo ("
                                + CAMPOS
                                + ") VALUES (?, ?, ?) ON CONFLICT (id) DO NOTHING",
                        categoria.id(),
                        categoria.nombre(),
                        categoria.categoriaLicenciaMinima())
                == 1;
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return Boolean.TRUE.equals(
                jdbc.queryForObject(
                        "SELECT EXISTS (SELECT 1 FROM categoria_vehiculo WHERE nombre = ?)",
                        Boolean.class,
                        nombre));
    }

    @Override
    public Optional<CategoriaVehiculoEntidad> buscarPorId(UUID id) {
        return jdbc
                .query("SELECT " + CAMPOS + " FROM categoria_vehiculo WHERE id = ?", MAPEAR, id)
                .stream()
                .findFirst();
    }

    private String filtros(
            @Nullable String nombre, @Nullable String minima, List<Object> parametros) {
        String sql = " WHERE TRUE";
        if (nombre != null) {
            sql += " AND strpos(lower(nombre), lower(?)) > 0";
            parametros.add(nombre);
        }
        if (minima != null) {
            sql += " AND categoria_licencia_minima = ?";
            parametros.add(minima);
        }
        return sql;
    }

    @Override
    public List<CategoriaVehiculoEntidad> buscar(
            @Nullable String nombre, @Nullable String minima, int limite, long desplazamiento) {
        var parametros = new ArrayList<Object>();
        String sql =
                "SELECT "
                        + CAMPOS
                        + " FROM categoria_vehiculo"
                        + filtros(nombre, minima, parametros)
                        + " ORDER BY nombre, id LIMIT ? OFFSET ?";
        parametros.add(limite);
        parametros.add(desplazamiento);
        return jdbc.query(sql, MAPEAR, parametros.toArray());
    }

    @Override
    public long contar(@Nullable String nombre, @Nullable String minima) {
        var parametros = new ArrayList<Object>();
        String sql =
                "SELECT count(*) FROM categoria_vehiculo" + filtros(nombre, minima, parametros);
        return ResultadoSql.requerido(jdbc.queryForObject(sql, Long.class, parametros.toArray()));
    }

    @Override
    public void actualizar(CategoriaVehiculoEntidad categoriaVehiculoEntidad) {
        jdbc.update(
                "UPDATE categoria_vehiculo SET nombre=?,categoria_licencia_minima=? WHERE id=?",
                categoriaVehiculoEntidad.nombre(),
                categoriaVehiculoEntidad.categoriaLicenciaMinima(),
                categoriaVehiculoEntidad.id());
    }

    @Override
    public void eliminar(UUID id) {
        jdbc.update("DELETE FROM categoria_vehiculo WHERE id=?", id);
    }

    @Override
    public boolean estaEnUso(UUID id) {
        return Boolean.TRUE.equals(
                jdbc.queryForObject(
                        "SELECT EXISTS(SELECT 1 FROM vehiculo WHERE categoria_vehiculo_id=?)",
                        Boolean.class,
                        id));
    }
}
