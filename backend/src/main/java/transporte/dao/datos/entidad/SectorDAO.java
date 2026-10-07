package transporte.dao.datos.entidad;

import transporte.dao.datos.ConsultarDAO;
import transporte.dao.datos.EliminarDAO;
import transporte.dominio.valor.Pagina;
import transporte.entidad.SectorEntidad;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SectorDAO extends ConsultarDAO<SectorEntidad, UUID>, EliminarDAO<UUID> {
    void guardar(SectorEntidad entidad);

    @Override
    Optional<SectorEntidad> buscarPorId(UUID id);

    List<SectorEntidad> buscar(
            @Nullable String nombre,
            @Nullable UUID municipioId,
            @Nullable Boolean activo,
            Pagina pagina);

    @Override
    void eliminar(UUID id);

    boolean estaEnUso(UUID id);

    boolean existePorMunicipioYNombre(UUID municipioId, String nombre);
}
