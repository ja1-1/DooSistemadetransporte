package transporte.negocio.fachada.assembler.impl;

import transporte.dominio.SectorDominio;
import transporte.dto.geografia.SectorDTO;
import transporte.negocio.fachada.assembler.DTOAssembler;

import org.springframework.stereotype.Component;

@Component
public class SectorDTOAssembler implements DTOAssembler<SectorDominio, SectorDTO> {
    @Override
    public SectorDTO convertirADTO(SectorDominio sector) {
        return new SectorDTO(
                sector.getId(), sector.getNombre(), sector.getMunicipioId(), sector.isActivo());
    }

    @Override
    public SectorDominio convertirADominio(SectorDTO sector) {
        return new SectorDominio(
                sector.id(), sector.nombre(), sector.municipioId(), sector.activo());
    }
}
