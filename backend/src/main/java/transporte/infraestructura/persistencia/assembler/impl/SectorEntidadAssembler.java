package transporte.infraestructura.persistencia.assembler.impl;

import transporte.dominio.SectorDominio;
import transporte.entidad.SectorEntidad;
import transporte.infraestructura.persistencia.assembler.EntidadAssembler;

import org.springframework.stereotype.Component;

@Component
public class SectorEntidadAssembler implements EntidadAssembler<SectorDominio, SectorEntidad> {
    @Override
    public SectorDominio convertirADominio(SectorEntidad sectorEntidad) {
        return new SectorDominio(
                sectorEntidad.id(),
                sectorEntidad.nombre(),
                sectorEntidad.municipioId(),
                sectorEntidad.activo());
    }

    @Override
    public SectorEntidad convertirAEntidad(SectorDominio sector) {
        return new SectorEntidad(
                sector.getId(), sector.getNombre(), sector.getMunicipioId(), sector.isActivo());
    }
}
