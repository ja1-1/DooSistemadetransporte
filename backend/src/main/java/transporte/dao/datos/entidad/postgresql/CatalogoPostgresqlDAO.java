package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.CatalogoDAO;
import transporte.dominio.enums.EstadoSalida;
import transporte.dominio.enums.Rol;
import transporte.dominio.enums.TipoIncidencia;
import transporte.dominio.enums.TipoRuta;
import transporte.dominio.enums.ValorCatalogo;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CatalogoPostgresqlDAO implements CatalogoDAO {
    private final JdbcTemplate jdbc;

    public CatalogoPostgresqlDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<ValorCatalogo> consultar(String catalogo) {
        return switch (catalogo) {
            case "roles" ->
                    jdbc.query(
                            "SELECT id FROM rol ORDER BY nombre",
                            (fila, numero) -> Rol.desdeId(fila.getObject("id", UUID.class)));
            case "estados-salida" ->
                    jdbc.query(
                            "SELECT id FROM estado_salida ORDER BY nombre",
                            (fila, numero) ->
                                    EstadoSalida.desdeId(fila.getObject("id", UUID.class)));
            case "tipos-ruta" ->
                    jdbc.query(
                            "SELECT id FROM tipo_ruta ORDER BY nombre",
                            (fila, numero) -> TipoRuta.desdeId(fila.getObject("id", UUID.class)));
            case "tipos-incidencia" ->
                    jdbc.query(
                            "SELECT id FROM tipo_incidencia ORDER BY nombre",
                            (fila, numero) ->
                                    TipoIncidencia.desdeId(fila.getObject("id", UUID.class)));
            default ->
                    throw new IllegalArgumentException(
                            CatalogoMensajes.EL_CATALOGO_NO_ESTA_DISPONIBLE);
        };
    }
}
