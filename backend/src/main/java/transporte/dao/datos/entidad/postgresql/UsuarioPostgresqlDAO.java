package transporte.dao.datos.entidad.postgresql;

import transporte.dao.datos.entidad.UsuarioDAO;
import transporte.dominio.consulta.FiltroUsuario;
import transporte.dominio.valor.Pagina;
import transporte.entidad.UsuarioEntidad;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UsuarioPostgresqlDAO implements UsuarioDAO {
    private static final String COLUMNAS =
            "id,primer_nombre,segundo_nombre,primer_apellido,segundo_apellido,cedula,correo,numero_telefono,hash_contrasena,rol_id,conductor_id,activo,debe_cambiar_contrasena";
    private final JdbcTemplate jdbc;

    public UsuarioPostgresqlDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private UsuarioEntidad convertir(ResultSet fila, int numero) throws SQLException {
        return new UsuarioEntidad(
                fila.getObject("id", UUID.class),
                fila.getString("primer_nombre"),
                fila.getString("segundo_nombre"),
                fila.getString("primer_apellido"),
                fila.getString("segundo_apellido"),
                fila.getString("cedula"),
                fila.getString("correo"),
                fila.getString("numero_telefono"),
                fila.getString("hash_contrasena"),
                fila.getObject("rol_id", UUID.class),
                fila.getObject("conductor_id", UUID.class),
                fila.getObject("activo", Boolean.class),
                fila.getObject("debe_cambiar_contrasena", Boolean.class));
    }

    private CondicionesSql condiciones(FiltroUsuario filtros) {
        var condiciones = new CondicionesSql();
        condiciones.igual("id", filtros.id());
        condiciones.igual("primer_nombre", filtros.primerNombre());
        condiciones.igual("segundo_nombre", filtros.segundoNombre());
        condiciones.igual("primer_apellido", filtros.primerApellido());
        condiciones.igual("segundo_apellido", filtros.segundoApellido());
        condiciones.igual("cedula", filtros.cedula());
        condiciones.igual("correo", filtros.correo());
        condiciones.igual("numero_telefono", filtros.numeroTelefono());
        condiciones.igual("rol_id", filtros.rol() == null ? null : filtros.rol().getId());
        condiciones.igual("conductor_id", filtros.conductorId());
        condiciones.igual("activo", filtros.activo());
        condiciones.igual("debe_cambiar_contrasena", filtros.debeCambiarContrasena());
        return condiciones;
    }

    @Override
    public void guardar(UsuarioEntidad entidad) {
        jdbc.update(
                "INSERT INTO"
                    + " usuario(id,primer_nombre,segundo_nombre,primer_apellido,segundo_apellido,cedula,correo,numero_telefono,hash_contrasena,rol_id,conductor_id,activo,debe_cambiar_contrasena)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ON CONFLICT(id) DO UPDATE SET"
                    + " primer_nombre=EXCLUDED.primer_nombre,segundo_nombre=EXCLUDED.segundo_nombre,primer_apellido=EXCLUDED.primer_apellido,segundo_apellido=EXCLUDED.segundo_apellido,cedula=EXCLUDED.cedula,correo=EXCLUDED.correo,numero_telefono=EXCLUDED.numero_telefono,hash_contrasena=EXCLUDED.hash_contrasena,rol_id=EXCLUDED.rol_id,conductor_id=EXCLUDED.conductor_id,activo=EXCLUDED.activo,debe_cambiar_contrasena=EXCLUDED.debe_cambiar_contrasena",
                entidad.id(),
                entidad.primerNombre(),
                entidad.segundoNombre(),
                entidad.primerApellido(),
                entidad.segundoApellido(),
                entidad.cedula(),
                entidad.correo(),
                entidad.numeroTelefono(),
                entidad.hashContrasena(),
                entidad.rolId(),
                entidad.conductorId(),
                entidad.activo(),
                entidad.debeCambiarContrasena());
    }

    @Override
    public Optional<UsuarioEntidad> buscarPorId(UUID id) {
        return relacionados(FiltroUsuario.todos().conId(id)).stream().findFirst();
    }

    @Override
    public List<UsuarioEntidad> buscar(FiltroUsuario filtros, @Nullable Pagina pagina) {
        var condiciones = condiciones(filtros);
        String sql =
                "SELECT "
                        + COLUMNAS
                        + " FROM usuario"
                        + condiciones.texto()
                        + " ORDER BY id"
                        + condiciones.paginar(pagina);
        var registros = jdbc.query(sql, this::convertir, condiciones.parametros());
        return registros;
    }

    @Override
    public List<UsuarioEntidad> relacionados(FiltroUsuario filtros) {
        return buscar(filtros, null);
    }

    @Override
    public long contar(FiltroUsuario filtros) {
        var condiciones = condiciones(filtros);
        return ResultadoSql.requerido(
                jdbc.queryForObject(
                        "SELECT count(*) FROM usuario" + condiciones.texto(),
                        Long.class,
                        condiciones.parametros()));
    }
}
