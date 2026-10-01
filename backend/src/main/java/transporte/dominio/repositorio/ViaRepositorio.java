package transporte.dominio.repositorio;

import transporte.dominio.ViaDominio;
import transporte.dominio.valor.Pagina;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ViaRepositorio {
    void guardar(ViaDominio via);

    Optional<ViaDominio> buscarPorId(UUID id);

    List<ViaDominio> buscar(
            @Nullable String nombre,
            @Nullable String descripcion,
            @Nullable Boolean activo,
            Pagina pagina);

    void eliminar(UUID id);

    boolean estaEnUso(UUID id);

    boolean existePorNombre(String nombre);
}
