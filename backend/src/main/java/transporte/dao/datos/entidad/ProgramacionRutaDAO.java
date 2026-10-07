package transporte.dao.datos.entidad;

import transporte.dominio.consulta.FiltroProgramacionRuta;
import transporte.dominio.valor.Pagina;
import transporte.entidad.ProgramacionRutaEntidad;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProgramacionRutaDAO {
    void guardar(ProgramacionRutaEntidad entidad);

    Optional<ProgramacionRutaEntidad> buscarPorId(UUID id);

    List<ProgramacionRutaEntidad> buscar(FiltroProgramacionRuta filtros, @Nullable Pagina pagina);

    List<ProgramacionRutaEntidad> relacionados(FiltroProgramacionRuta filtros);

    long contar(FiltroProgramacionRuta filtros);
}
