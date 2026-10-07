package transporte.dao.datos.entidad;

import transporte.dominio.consulta.FiltroAsignacion;
import transporte.dominio.valor.Pagina;
import transporte.entidad.AsignacionEntidad;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AsignacionDAO {
    void guardar(AsignacionEntidad entidad);

    Optional<AsignacionEntidad> buscarPorId(UUID id);

    List<AsignacionEntidad> buscar(FiltroAsignacion filtros, @Nullable Pagina pagina);

    List<AsignacionEntidad> relacionados(FiltroAsignacion filtros);

    long contar(FiltroAsignacion filtros);
}
