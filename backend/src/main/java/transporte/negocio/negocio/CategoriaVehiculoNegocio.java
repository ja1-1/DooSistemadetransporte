package transporte.negocio.negocio;

import transporte.dominio.CategoriaVehiculoDominio;
import transporte.dominio.valor.Pagina;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface CategoriaVehiculoNegocio {
    CategoriaVehiculoDominio registrar(String nombre, String minima);

    CategoriaVehiculoDominio consultarPorId(UUID id);

    List<CategoriaVehiculoDominio> consultar(
            @Nullable String nombre, @Nullable String minima, Pagina pagina);

    long contar(@Nullable String nombre, @Nullable String minima);

    CategoriaVehiculoDominio modificar(UUID id, String nombre, String minima);

    void eliminar(UUID id);
}
