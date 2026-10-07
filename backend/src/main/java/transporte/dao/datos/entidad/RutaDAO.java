package transporte.dao.datos.entidad;

import transporte.dominio.consulta.FiltroRuta;
import transporte.dominio.valor.Pagina;
import transporte.entidad.RutaEntidad;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RutaDAO {
    void guardar(RutaEntidad entidad);

    Optional<RutaEntidad> buscarPorId(UUID id);

    List<RutaEntidad> buscar(FiltroRuta filtros, @Nullable Pagina pagina);

    List<RutaEntidad> relacionados(FiltroRuta filtros);

    long contar(FiltroRuta filtros);

    void eliminar(UUID id);
}
