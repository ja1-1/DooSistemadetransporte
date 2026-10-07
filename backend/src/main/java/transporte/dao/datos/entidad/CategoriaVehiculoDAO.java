package transporte.dao.datos.entidad;

import transporte.dao.datos.ActualizarDAO;
import transporte.dao.datos.ConsultarDAO;
import transporte.dao.datos.CrearDAO;
import transporte.dao.datos.EliminarDAO;
import transporte.entidad.CategoriaVehiculoEntidad;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaVehiculoDAO
        extends CrearDAO<CategoriaVehiculoEntidad>,
                ConsultarDAO<CategoriaVehiculoEntidad, UUID>,
                ActualizarDAO<CategoriaVehiculoEntidad>,
                EliminarDAO<UUID> {
    UUID nuevoIdentificador();

    @Override
    boolean crear(CategoriaVehiculoEntidad categoria);

    boolean existePorNombre(String nombre);

    @Override
    Optional<CategoriaVehiculoEntidad> buscarPorId(UUID id);

    List<CategoriaVehiculoEntidad> buscar(
            @Nullable String nombre, @Nullable String minima, int limite, long desplazamiento);

    long contar(@Nullable String nombre, @Nullable String minima);

    @Override
    void actualizar(CategoriaVehiculoEntidad categoria);

    @Override
    void eliminar(UUID id);

    boolean estaEnUso(UUID id);
}
