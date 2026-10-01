package transporte.dominio.repositorio;

import transporte.dominio.AsignacionDominio;
import transporte.dominio.consulta.FiltroAsignacion;
import transporte.dominio.valor.Pagina;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AsignacionRepositorio {
    long contar(FiltroAsignacion filtro);

    void guardar(AsignacionDominio asignacion);

    Optional<AsignacionDominio> buscarPorId(UUID id);

    List<AsignacionDominio> buscar(FiltroAsignacion filtro, Pagina pagina);

    List<AsignacionDominio> relacionados(FiltroAsignacion filtro);

    default boolean existePorConductorVehiculoYFechaInicio(
            UUID conductorId, UUID vehiculoId, LocalDate fechaInicio) {
        return !relacionados(
                        FiltroAsignacion.todos()
                                .conConductorId(conductorId)
                                .conVehiculoId(vehiculoId)
                                .conFechaInicio(fechaInicio))
                .isEmpty();
    }

    default List<AsignacionDominio> buscarPorConductor(UUID conductorId) {
        return relacionados(FiltroAsignacion.todos().conConductorId(conductorId));
    }

    default List<AsignacionDominio> buscarPorVehiculo(UUID vehiculoId) {
        return relacionados(FiltroAsignacion.todos().conVehiculoId(vehiculoId));
    }

    default List<AsignacionDominio> buscarVigentesEn(LocalDate fecha) {
        return relacionados(FiltroAsignacion.todos()).stream()
                .filter(asignacion -> asignacion.getPeriodo().estaVigenteEn(fecha))
                .toList();
    }
}
