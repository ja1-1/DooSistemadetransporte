package transporte.infraestructura.persistencia.assembler.impl;

import transporte.dominio.VehiculoDominio;
import transporte.dominio.valor.Placa;
import transporte.entidad.VehiculoEntidad;
import transporte.infraestructura.persistencia.assembler.EntidadAssembler;

import org.springframework.stereotype.Component;

@Component
public class VehiculoEntidadAssembler
        implements EntidadAssembler<VehiculoDominio, VehiculoEntidad> {
    @Override
    public VehiculoDominio convertirADominio(VehiculoEntidad vehiculoEntidad) {
        return new VehiculoDominio(
                vehiculoEntidad.id(),
                new Placa(vehiculoEntidad.placa()),
                vehiculoEntidad.marca(),
                vehiculoEntidad.modelo(),
                vehiculoEntidad.anioFabricacion(),
                vehiculoEntidad.capacidadPasajeros(),
                vehiculoEntidad.categoriaVehiculoId(),
                vehiculoEntidad.activo());
    }

    @Override
    public VehiculoEntidad convertirAEntidad(VehiculoDominio vehiculo) {
        return new VehiculoEntidad(
                vehiculo.getId(),
                vehiculo.getPlaca().valor(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getAnioFabricacion(),
                vehiculo.getCapacidadPasajeros(),
                vehiculo.getCategoriaVehiculoId(),
                vehiculo.isActivo());
    }
}
