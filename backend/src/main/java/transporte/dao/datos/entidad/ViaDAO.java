package transporte.dao.datos.entidad;

import transporte.dao.datos.ConsultarDAO;
import transporte.dao.datos.EliminarDAO;
import transporte.dominio.valor.Pagina;
import transporte.entidad.ViaEntidad;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ViaDAO extends ConsultarDAO<ViaEntidad, UUID>, EliminarDAO<UUID> {
    void guardar(ViaEntidad entidad);

    @Override
    Optional<ViaEntidad> buscarPorId(UUID id);

    List<ViaEntidad> buscar(
            @Nullable String nombre,
            @Nullable String descripcion,
            @Nullable Boolean activo,
            Pagina pagina);

    @Override
    void eliminar(UUID id);

    boolean estaEnUso(UUID id);

    boolean existePorNombre(String nombre);
}
