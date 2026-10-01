package transporte.dominio.repositorio;

import transporte.dominio.VehiculoDominio;
import transporte.dominio.consulta.FiltroVehiculo;
import transporte.dominio.valor.Pagina;
import transporte.dominio.valor.Placa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehiculoRepositorio {
    long contar(FiltroVehiculo filtro);

    void guardar(VehiculoDominio vehiculo);

    Optional<VehiculoDominio> buscarPorId(UUID id);

    List<VehiculoDominio> buscar(FiltroVehiculo filtro, Pagina pagina);

    List<VehiculoDominio> relacionados(FiltroVehiculo filtro);

    void eliminar(UUID id);

    boolean estaEnUso(UUID id);

    default boolean existePorPlaca(Placa placa) {
        return !relacionados(FiltroVehiculo.todos().conPlaca(placa.valor())).isEmpty();
    }
}
