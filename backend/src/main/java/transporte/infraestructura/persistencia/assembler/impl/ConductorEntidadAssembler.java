package transporte.infraestructura.persistencia.assembler.impl;

import transporte.dominio.ConductorDominio;
import transporte.dominio.enums.CategoriaLicencia;
import transporte.dominio.enums.TipoSangre;
import transporte.dominio.valor.Cedula;
import transporte.dominio.valor.Licencia;
import transporte.dominio.valor.Telefono;
import transporte.entidad.ConductorEntidad;
import transporte.infraestructura.persistencia.assembler.EntidadAssembler;

import org.springframework.stereotype.Component;

@Component
public class ConductorEntidadAssembler
        implements EntidadAssembler<ConductorDominio, ConductorEntidad> {
    @Override
    public ConductorDominio convertirADominio(ConductorEntidad conductorEntidad) {
        return new ConductorDominio(
                conductorEntidad.id(),
                new Cedula(conductorEntidad.cedula()),
                conductorEntidad.primerNombre(),
                conductorEntidad.segundoNombre(),
                conductorEntidad.primerApellido(),
                conductorEntidad.segundoApellido(),
                conductorEntidad.fechaNacimiento(),
                new Telefono(conductorEntidad.numeroTelefono()),
                TipoSangre.desdeSimbolo(conductorEntidad.tipoSangre()),
                new Licencia(
                        conductorEntidad.numeroLicencia(),
                        CategoriaLicencia.desdeTexto(conductorEntidad.categoriaLicencia()),
                        conductorEntidad.fechaVencimientoLicencia()),
                conductorEntidad.activo());
    }

    @Override
    public ConductorEntidad convertirAEntidad(ConductorDominio conductor) {
        return new ConductorEntidad(
                conductor.getId(),
                conductor.getCedula().valor(),
                conductor.getPrimerNombre(),
                conductor.getSegundoNombre().orElse(null),
                conductor.getPrimerApellido(),
                conductor.getSegundoApellido().orElse(null),
                conductor.getFechaNacimiento(),
                conductor.getNumeroTelefono().valor(),
                conductor.getTipoSangre().getSimbolo(),
                conductor.getLicencia().numero(),
                conductor.getLicencia().categoria().name(),
                conductor.getLicencia().fechaVencimiento(),
                conductor.isActivo());
    }
}
