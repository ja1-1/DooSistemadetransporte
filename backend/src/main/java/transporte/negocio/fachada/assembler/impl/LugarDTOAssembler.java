package transporte.negocio.fachada.assembler.impl;

import transporte.dominio.LugarDominio;
import transporte.dto.geografia.LugarDTO;
import transporte.negocio.fachada.assembler.DTOAssembler;

import org.springframework.stereotype.Component;

@Component
public class LugarDTOAssembler implements DTOAssembler<LugarDominio, LugarDTO> {
    @Override
    public LugarDTO convertirADTO(LugarDominio lugar) {
        return new LugarDTO(
                lugar.getId(), lugar.getNombre(), lugar.getSectorId(), lugar.isActivo());
    }

    @Override
    public LugarDominio convertirADominio(LugarDTO lugar) {
        return new LugarDominio(lugar.id(), lugar.nombre(), lugar.sectorId(), lugar.activo());
    }
}
