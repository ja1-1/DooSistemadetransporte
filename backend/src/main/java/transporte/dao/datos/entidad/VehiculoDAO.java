package transporte.dao.datos.entidad;

import transporte.dominio.consulta.FiltroVehiculo;
import transporte.dominio.valor.Pagina;
import transporte.entidad.VehiculoEntidad;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehiculoDAO {
    void guardar(VehiculoEntidad entidad);

    Optional<VehiculoEntidad> buscarPorId(UUID id);

    List<VehiculoEntidad> buscar(FiltroVehiculo filtros, @Nullable Pagina pagina);

    List<VehiculoEntidad> relacionados(FiltroVehiculo filtros);

    long contar(FiltroVehiculo filtros);

    void eliminar(UUID id);
}
