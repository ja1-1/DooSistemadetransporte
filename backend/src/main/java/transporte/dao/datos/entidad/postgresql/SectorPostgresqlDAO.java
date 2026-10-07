package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.SectorDAO;
import transporte.dominio.valor.Pagina;
import transporte.entidad.SectorEntidad;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SectorPostgresqlDAO implements SectorDAO {
    private final JdbcTemplate jdbc;
    private static final RowMapper<SectorEntidad> MAPEAR =
            (resultSet, numero) ->
                    new SectorEntidad(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("nombre"),
                            resultSet.getObject("municipio_id", UUID.class),
                            resultSet.getBoolean("activo"));

    public SectorPostgresqlDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void guardar(SectorEntidad sectorEntidad) {
        jdbc.update(
                "INSERT INTO sector(id,nombre,municipio_id,activo) VALUES(?,?,?,?) ON CONFLICT(id)"
                    + " DO UPDATE SET"
                    + " nombre=EXCLUDED.nombre,municipio_id=EXCLUDED.municipio_id,activo=EXCLUDED.activo",
                sectorEntidad.id(),
                sectorEntidad.nombre(),
                sectorEntidad.municipioId(),
                sectorEntidad.activo());
    }

    @Override
    public Optional<SectorEntidad> buscarPorId(UUID id) {
        return jdbc
                .query("SELECT id,nombre,municipio_id,activo FROM sector WHERE id=?", MAPEAR, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<SectorEntidad> buscar(
            @Nullable String nombre,
            @Nullable UUID municipioId,
            @Nullable Boolean activo,
            Pagina pagina) {
        var parametros = new ArrayList<Object>();
        String sql = "SELECT id,nombre,municipio_id,activo FROM sector WHERE TRUE";
        if (nombre != null) {
            sql += " AND strpos(lower(nombre),lower(?))>0";
            parametros.add(nombre);
        }
        if (municipioId != null) {
            sql += " AND municipio_id=?";
            parametros.add(municipioId);
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
        jdbc.update("DELETE FROM sector WHERE id=?", id);
    }

    @Override
    public boolean estaEnUso(UUID id) {
        return Boolean.TRUE.equals(
                jdbc.queryForObject(
                        "SELECT EXISTS(SELECT 1 FROM lugar WHERE sector_id = ?)",
                        Boolean.class,
                        id));
    }

    @Override
    public boolean existePorMunicipioYNombre(UUID municipioId, String nombre) {
        return Boolean.TRUE.equals(
                jdbc.queryForObject(
                        "SELECT EXISTS(SELECT 1 FROM sector WHERE municipio_id = ? AND nombre = ?)",
                        Boolean.class,
                        municipioId,
                        nombre));
    }
}
