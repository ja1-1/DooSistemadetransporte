package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.VehiculoDAO;
import transporte.dominio.consulta.FiltroVehiculo;
import transporte.dominio.valor.Pagina;
import transporte.entidad.VehiculoEntidad;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class VehiculoPostgresqlDAO implements VehiculoDAO {
    private static final String COLUMNAS =
            "id,placa,marca,modelo,anio_fabricacion,capacidad_pasajeros,categoria_vehiculo_id,activo";
    private final JdbcTemplate jdbc;

    public VehiculoPostgresqlDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private VehiculoEntidad convertir(ResultSet fila, int numero) throws SQLException {
        return new VehiculoEntidad(
                fila.getObject("id", UUID.class),
                fila.getString("placa"),
                fila.getString("marca"),
                fila.getString("modelo"),
                fila.getObject("anio_fabricacion", Integer.class),
                fila.getObject("capacidad_pasajeros", Integer.class),
                fila.getObject("categoria_vehiculo_id", UUID.class),
                fila.getObject("activo", Boolean.class));
    }

    private CondicionesSql condiciones(FiltroVehiculo filtros) {
        var condiciones = new CondicionesSql();
        condiciones.igual("id", filtros.id());
        condiciones.igual("placa", filtros.placa());
        condiciones.igual("marca", filtros.marca());
        condiciones.igual("modelo", filtros.modelo());
        condiciones.igual("anio_fabricacion", filtros.anioFabricacion());
        condiciones.igual("capacidad_pasajeros", filtros.capacidadPasajeros());
        condiciones.igual("categoria_vehiculo_id", filtros.categoriaVehiculoId());
        condiciones.igual("activo", filtros.activo());
        return condiciones;
    }

    @Override
    public void guardar(VehiculoEntidad entidad) {
        jdbc.update(
                "INSERT INTO"
                    + " vehiculo(id,placa,marca,modelo,anio_fabricacion,capacidad_pasajeros,categoria_vehiculo_id,activo)"
                    + " VALUES(?,?,?,?,?,?,?,?) ON CONFLICT(id) DO UPDATE SET"
                    + " placa=EXCLUDED.placa,marca=EXCLUDED.marca,modelo=EXCLUDED.modelo,anio_fabricacion=EXCLUDED.anio_fabricacion,capacidad_pasajeros=EXCLUDED.capacidad_pasajeros,categoria_vehiculo_id=EXCLUDED.categoria_vehiculo_id,activo=EXCLUDED.activo",
                entidad.id(),
                entidad.placa(),
                entidad.marca(),
                entidad.modelo(),
                entidad.anioFabricacion(),
                entidad.capacidadPasajeros(),
                entidad.categoriaVehiculoId(),
                entidad.activo());
    }

    @Override
    public Optional<VehiculoEntidad> buscarPorId(UUID id) {
        return relacionados(FiltroVehiculo.todos().conId(id)).stream().findFirst();
    }

    @Override
    public List<VehiculoEntidad> buscar(FiltroVehiculo filtros, @Nullable Pagina pagina) {
        var condiciones = condiciones(filtros);
        String sql =
                "SELECT "
                        + COLUMNAS
                        + " FROM vehiculo"
                        + condiciones.texto()
                        + " ORDER BY id"
                        + condiciones.paginar(pagina);
        var registros = jdbc.query(sql, this::convertir, condiciones.parametros());
        return registros;
    }

    @Override
    public List<VehiculoEntidad> relacionados(FiltroVehiculo filtros) {
        return buscar(filtros, null);
    }

    @Override
    public long contar(FiltroVehiculo filtros) {
        var condiciones = condiciones(filtros);
        return ResultadoSql.requerido(
                jdbc.queryForObject(
                        "SELECT count(*) FROM vehiculo" + condiciones.texto(),
                        Long.class,
                        condiciones.parametros()));
    }

    @Override
    public void eliminar(UUID id) {
        jdbc.update("DELETE FROM vehiculo WHERE id=?", id);
    }
}
