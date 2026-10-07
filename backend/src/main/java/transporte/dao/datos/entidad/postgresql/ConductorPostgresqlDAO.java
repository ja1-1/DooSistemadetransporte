package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.ConductorDAO;
import transporte.dominio.consulta.FiltroConductor;
import transporte.dominio.valor.Pagina;
import transporte.entidad.ConductorEntidad;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ConductorPostgresqlDAO implements ConductorDAO {
    private static final String COLUMNAS =
            "id,cedula,primer_nombre,segundo_nombre,primer_apellido,segundo_apellido,fecha_nacimiento,numero_telefono,tipo_sangre,numero_licencia,categoria_licencia,fecha_vencimiento_licencia,activo";
    private final JdbcTemplate jdbc;

    public ConductorPostgresqlDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private ConductorEntidad convertir(ResultSet fila, int numero) throws SQLException {
        return new ConductorEntidad(
                fila.getObject("id", UUID.class),
                fila.getString("cedula"),
                fila.getString("primer_nombre"),
                fila.getString("segundo_nombre"),
                fila.getString("primer_apellido"),
                fila.getString("segundo_apellido"),
                fila.getObject("fecha_nacimiento", LocalDate.class),
                fila.getString("numero_telefono"),
                fila.getString("tipo_sangre"),
                fila.getString("numero_licencia"),
                fila.getString("categoria_licencia"),
                fila.getObject("fecha_vencimiento_licencia", LocalDate.class),
                fila.getObject("activo", Boolean.class));
    }

    private CondicionesSql condiciones(FiltroConductor filtros) {
        var condiciones = new CondicionesSql();
        condiciones.igual("id", filtros.id());
        condiciones.igual("cedula", filtros.cedula());
        condiciones.igual("primer_nombre", filtros.primerNombre());
        condiciones.igual("segundo_nombre", filtros.segundoNombre());
        condiciones.igual("primer_apellido", filtros.primerApellido());
        condiciones.igual("segundo_apellido", filtros.segundoApellido());
        condiciones.igual("fecha_nacimiento", filtros.fechaNacimiento());
        condiciones.igual("numero_telefono", filtros.numeroTelefono());
        condiciones.igual(
                "tipo_sangre",
                filtros.tipoSangre() == null ? null : filtros.tipoSangre().getSimbolo());
        condiciones.igual("numero_licencia", filtros.numeroLicencia());
        condiciones.igual(
                "categoria_licencia",
                filtros.categoriaLicencia() == null ? null : filtros.categoriaLicencia().name());
        condiciones.igual("fecha_vencimiento_licencia", filtros.fechaVencimientoLicencia());
        condiciones.igual("activo", filtros.activo());
        return condiciones;
    }

    @Override
    public void guardar(ConductorEntidad entidad) {
        jdbc.update(
                "INSERT INTO"
                    + " conductor(id,cedula,primer_nombre,segundo_nombre,primer_apellido,segundo_apellido,fecha_nacimiento,numero_telefono,tipo_sangre,numero_licencia,categoria_licencia,fecha_vencimiento_licencia,activo)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ON CONFLICT(id) DO UPDATE SET"
                    + " cedula=EXCLUDED.cedula,primer_nombre=EXCLUDED.primer_nombre,segundo_nombre=EXCLUDED.segundo_nombre,primer_apellido=EXCLUDED.primer_apellido,segundo_apellido=EXCLUDED.segundo_apellido,fecha_nacimiento=EXCLUDED.fecha_nacimiento,numero_telefono=EXCLUDED.numero_telefono,tipo_sangre=EXCLUDED.tipo_sangre,numero_licencia=EXCLUDED.numero_licencia,categoria_licencia=EXCLUDED.categoria_licencia,fecha_vencimiento_licencia=EXCLUDED.fecha_vencimiento_licencia,activo=EXCLUDED.activo",
                entidad.id(),
                entidad.cedula(),
                entidad.primerNombre(),
                entidad.segundoNombre(),
                entidad.primerApellido(),
                entidad.segundoApellido(),
                entidad.fechaNacimiento(),
                entidad.numeroTelefono(),
                entidad.tipoSangre(),
                entidad.numeroLicencia(),
                entidad.categoriaLicencia(),
                entidad.fechaVencimientoLicencia(),
                entidad.activo());
    }

    @Override
    public Optional<ConductorEntidad> buscarPorId(UUID id) {
        return relacionados(FiltroConductor.todos().conId(id)).stream().findFirst();
    }

    @Override
    public List<ConductorEntidad> buscar(FiltroConductor filtros, @Nullable Pagina pagina) {
        var condiciones = condiciones(filtros);
        String sql =
                "SELECT "
                        + COLUMNAS
                        + " FROM conductor"
                        + condiciones.texto()
                        + " ORDER BY id"
                        + condiciones.paginar(pagina);
        var registros = jdbc.query(sql, this::convertir, condiciones.parametros());
        return registros;
    }

    @Override
    public List<ConductorEntidad> relacionados(FiltroConductor filtros) {
        return buscar(filtros, null);
    }

    @Override
    public long contar(FiltroConductor filtros) {
        var condiciones = condiciones(filtros);
        return ResultadoSql.requerido(
                jdbc.queryForObject(
                        "SELECT count(*) FROM conductor" + condiciones.texto(),
                        Long.class,
                        condiciones.parametros()));
    }

    @Override
    public void eliminar(UUID id) {
        jdbc.update("DELETE FROM conductor WHERE id=?", id);
    }
}
