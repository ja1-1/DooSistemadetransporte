package transporte.negocio.fachada.assembler.impl;

import transporte.dominio.ViaDominio;
import transporte.dto.geografia.ViaDTO;
import transporte.negocio.fachada.assembler.DTOAssembler;

import org.springframework.stereotype.Component;

@Component
public class ViaDTOAssembler implements DTOAssembler<ViaDominio, ViaDTO> {
    @Override
    public ViaDTO convertirADTO(ViaDominio via) {
        return new ViaDTO(via.getId(), via.getNombre(), via.getDescripcion(), via.isActivo());
    }

    @Override
    public ViaDominio convertirADominio(ViaDTO via) {
        return new ViaDominio(via.id(), via.nombre(), via.descripcion(), via.activo());
    }
}
