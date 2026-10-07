package transporte.dao.datos.entidad;

import transporte.dominio.consulta.FiltroSalida;
import transporte.dominio.valor.Pagina;
import transporte.entidad.SalidaEntidad;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalidaDAO {
    void guardar(SalidaEntidad entidad);

    Optional<SalidaEntidad> buscarPorId(UUID id);

    List<SalidaEntidad> buscar(FiltroSalida filtros, @Nullable Pagina pagina);

    List<SalidaEntidad> relacionados(FiltroSalida filtros);

    long contar(FiltroSalida filtros);
}
