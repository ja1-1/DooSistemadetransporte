package transporte.dao.datos.entidad;

import transporte.dominio.consulta.FiltroUsuario;
import transporte.dominio.valor.Pagina;
import transporte.entidad.UsuarioEntidad;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioDAO {
    void guardar(UsuarioEntidad entidad);

    Optional<UsuarioEntidad> buscarPorId(UUID id);

    List<UsuarioEntidad> buscar(FiltroUsuario filtros, @Nullable Pagina pagina);

    List<UsuarioEntidad> relacionados(FiltroUsuario filtros);

    long contar(FiltroUsuario filtros);
}
