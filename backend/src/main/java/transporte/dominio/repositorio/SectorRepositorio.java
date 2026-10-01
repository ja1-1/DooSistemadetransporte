package transporte.dominio.repositorio;

import transporte.dominio.SectorDominio;
import transporte.dominio.valor.Pagina;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SectorRepositorio {
    void guardar(SectorDominio sector);

    Optional<SectorDominio> buscarPorId(UUID id);

    List<SectorDominio> buscar(
            @Nullable String nombre,
            @Nullable UUID municipioId,
            @Nullable Boolean activo,
            Pagina pagina);

    void eliminar(UUID id);

    boolean estaEnUso(UUID id);

    boolean existePorMunicipioYNombre(UUID municipioId, String nombre);
}
