package transporte.dominio.repositorio;

import transporte.dominio.LugarDominio;
import transporte.dominio.valor.Pagina;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LugarRepositorio {
    void guardar(LugarDominio lugar);

    Optional<LugarDominio> buscarPorId(UUID id);

    List<LugarDominio> buscar(
            @Nullable String nombre,
            @Nullable UUID sectorId,
            @Nullable Boolean activo,
            Pagina pagina);

    void eliminar(UUID id);

    boolean estaEnUso(UUID id);

    boolean existePorSectorYNombre(UUID sectorId, String nombre);
}
