package transporte.dominio.repositorio;

import transporte.dominio.CategoriaVehiculoDominio;
import transporte.dominio.enums.CategoriaLicencia;
import transporte.dominio.valor.Pagina;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaVehiculoRepositorio {
    UUID nuevoIdentificador();

    void guardar(CategoriaVehiculoDominio categoriaVehiculo);

    boolean existePorNombre(String nombre);

    Optional<CategoriaVehiculoDominio> buscarPorId(UUID id);

    List<CategoriaVehiculoDominio> buscar(
            @Nullable String nombre,
            @Nullable CategoriaLicencia categoriaLicenciaMinima,
            Pagina pagina);

    long contar(@Nullable String nombre, @Nullable CategoriaLicencia categoriaLicenciaMinima);

    void eliminar(UUID id);

    boolean estaEnUso(UUID id);
}
