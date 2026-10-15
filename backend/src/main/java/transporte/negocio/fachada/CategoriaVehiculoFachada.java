package transporte.negocio.fachada;

import transporte.dto.flota.CategoriaVehiculoDTO;
import transporte.dto.flota.ListadoCategoriasDTO;
import transporte.dto.flota.RegistrarCategoriaVehiculoDTO;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface CategoriaVehiculoFachada {
    CategoriaVehiculoDTO registrar(RegistrarCategoriaVehiculoDTO datos);

    CategoriaVehiculoDTO consultarPorId(UUID id);

    ListadoCategoriasDTO consultar(
            @Nullable String nombre, @Nullable String minima, int pagina, int tamano);

    CategoriaVehiculoDTO modificar(UUID id, RegistrarCategoriaVehiculoDTO datos);

    void eliminar(UUID id);
}
