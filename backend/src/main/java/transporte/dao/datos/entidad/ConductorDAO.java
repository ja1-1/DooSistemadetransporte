package transporte.dao.datos.entidad;

import transporte.dominio.consulta.FiltroConductor;
import transporte.dominio.valor.Pagina;
import transporte.entidad.ConductorEntidad;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConductorDAO {
    void guardar(ConductorEntidad entidad);

    Optional<ConductorEntidad> buscarPorId(UUID id);

    List<ConductorEntidad> buscar(FiltroConductor filtros, @Nullable Pagina pagina);

    List<ConductorEntidad> relacionados(FiltroConductor filtros);

    long contar(FiltroConductor filtros);

    void eliminar(UUID id);
}
