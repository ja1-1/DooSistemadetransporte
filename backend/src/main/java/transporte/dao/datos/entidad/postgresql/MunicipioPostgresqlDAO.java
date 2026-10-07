package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.MunicipioDAO;
import transporte.dominio.valor.Pagina;
import transporte.entidad.MunicipioEntidad;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MunicipioPostgresqlDAO implements MunicipioDAO {
    private final JdbcTemplate jdbc;
    private static final RowMapper<MunicipioEntidad> MAPEAR =
            (resultSet, numero) ->
                    new MunicipioEntidad(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("nombre"),
                            resultSet.getString("departamento"),
                            resultSet.getBoolean("activo"));

    public MunicipioPostgresqlDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void guardar(MunicipioEntidad municipioEntidad) {
        jdbc.update(
                "INSERT INTO municipio(id,nombre,departamento,activo) VALUES(?,?,?,?) ON"
                    + " CONFLICT(id) DO UPDATE SET"
                    + " nombre=EXCLUDED.nombre,departamento=EXCLUDED.departamento,activo=EXCLUDED.activo",
                municipioEntidad.id(),
                municipioEntidad.nombre(),
                municipioEntidad.departamento(),
                municipioEntidad.activo());
    }

    @Override
    public Optional<MunicipioEntidad> buscarPorId(UUID id) {
        return jdbc
                .query("SELECT id,nombre,departamento,activo FROM municipio WHERE id=?", MAPEAR, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<MunicipioEntidad> buscar(
            @Nullable String nombre,
            @Nullable String departamento,
            @Nullable Boolean activo,
            Pagina pagina) {
        var parametros = new ArrayList<Object>();
        String sql = "SELECT id,nombre,departamento,activo FROM municipio WHERE TRUE";
        if (nombre != null) {
            sql += " AND strpos(lower(nombre),lower(?))>0";
            parametros.add(nombre);
        }
        if (departamento != null) {
            sql += " AND departamento=?";
            parametros.add(departamento);
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
        jdbc.update("DELETE FROM municipio WHERE id=?", id);
    }

    @Override
    public boolean estaEnUso(UUID id) {
        return Boolean.TRUE.equals(
                jdbc.queryForObject(
                        "SELECT EXISTS(SELECT 1 FROM sector WHERE municipio_id = ?)",
                        Boolean.class,
                        id));
    }

    @Override
    public boolean existePorNombreYDepartamento(String nombre, String departamento) {
        return Boolean.TRUE.equals(
                jdbc.queryForObject(
                        "SELECT EXISTS(SELECT 1 FROM municipio WHERE nombre = ? AND departamento ="
                                + " ?)",
                        Boolean.class,
                        nombre,
                        departamento));
    }
}
