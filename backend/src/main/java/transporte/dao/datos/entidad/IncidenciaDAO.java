package transporte.dao.datos.entidad;

import transporte.dominio.consulta.FiltroIncidencia;
import transporte.dominio.valor.Pagina;
import transporte.entidad.IncidenciaEntidad;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncidenciaDAO {
    void guardar(IncidenciaEntidad entidad);

    Optional<IncidenciaEntidad> buscarPorId(UUID id);

    List<IncidenciaEntidad> buscar(FiltroIncidencia filtros, @Nullable Pagina pagina);

    List<IncidenciaEntidad> relacionados(FiltroIncidencia filtros);

    long contar(FiltroIncidencia filtros);
}
