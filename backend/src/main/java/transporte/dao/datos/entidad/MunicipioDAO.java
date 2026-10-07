package transporte.dao.datos.entidad;

import transporte.dao.datos.ConsultarDAO;
import transporte.dao.datos.EliminarDAO;
import transporte.dominio.valor.Pagina;
import transporte.entidad.MunicipioEntidad;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MunicipioDAO extends ConsultarDAO<MunicipioEntidad, UUID>, EliminarDAO<UUID> {
    void guardar(MunicipioEntidad entidad);

    @Override
    Optional<MunicipioEntidad> buscarPorId(UUID id);

    List<MunicipioEntidad> buscar(
            @Nullable String nombre,
            @Nullable String departamento,
            @Nullable Boolean activo,
            Pagina pagina);

    @Override
    void eliminar(UUID id);

    boolean estaEnUso(UUID id);

    boolean existePorNombreYDepartamento(String nombre, String departamento);
}
