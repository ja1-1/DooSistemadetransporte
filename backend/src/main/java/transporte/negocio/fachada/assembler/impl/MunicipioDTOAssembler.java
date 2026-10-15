package transporte.negocio.fachada.assembler.impl;

import transporte.dominio.MunicipioDominio;
import transporte.dto.geografia.MunicipioDTO;
import transporte.negocio.fachada.assembler.DTOAssembler;

import org.springframework.stereotype.Component;

@Component
public class MunicipioDTOAssembler implements DTOAssembler<MunicipioDominio, MunicipioDTO> {
    @Override
    public MunicipioDTO convertirADTO(MunicipioDominio municipio) {
        return new MunicipioDTO(
                municipio.getId(),
                municipio.getNombre(),
                municipio.getDepartamento(),
                municipio.isActivo());
    }

    @Override
    public MunicipioDominio convertirADominio(MunicipioDTO municipio) {
        return new MunicipioDominio(
                municipio.id(), municipio.nombre(), municipio.departamento(), municipio.activo());
    }
}
