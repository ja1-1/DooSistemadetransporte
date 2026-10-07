package transporte.infraestructura.persistencia.assembler.impl;

import transporte.dominio.AsignacionDominio;
import transporte.dominio.valor.Periodo;
import transporte.entidad.AsignacionEntidad;
import transporte.infraestructura.persistencia.assembler.EntidadAssembler;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AsignacionEntidadAssembler
        implements EntidadAssembler<AsignacionDominio, AsignacionEntidad> {
    @Override
    public AsignacionDominio convertirADominio(AsignacionEntidad asignacionEntidad) {
        return new AsignacionDominio(
                asignacionEntidad.id(),
                asignacionEntidad.conductorId(),
                asignacionEntidad.vehiculoId(),
                new Periodo(
                        asignacionEntidad.fechaInicio(),
                        Optional.ofNullable(asignacionEntidad.fechaFin())));
    }

    @Override
    public AsignacionEntidad convertirAEntidad(AsignacionDominio asignacion) {
        return new AsignacionEntidad(
                asignacion.getId(),
                asignacion.getConductorId(),
                asignacion.getVehiculoId(),
                asignacion.getPeriodo().inicio(),
                asignacion.getPeriodo().fin().orElse(null));
    }
}
