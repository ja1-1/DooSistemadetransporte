package transporte.dao.datos.entidad;

import transporte.dao.datos.ConsultarDAO;
import transporte.dao.datos.EliminarDAO;
import transporte.dominio.valor.Pagina;
import transporte.entidad.LugarEntidad;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LugarDAO extends ConsultarDAO<LugarEntidad, UUID>, EliminarDAO<UUID> {
    void guardar(LugarEntidad entidad);

    @Override
    Optional<LugarEntidad> buscarPorId(UUID id);

    List<LugarEntidad> buscar(
            @Nullable String nombre,
            @Nullable UUID sectorId,
            @Nullable Boolean activo,
            Pagina pagina);

    @Override
    void eliminar(UUID id);

    boolean estaEnUso(UUID id);

    boolean existePorSectorYNombre(UUID sectorId, String nombre);
}
