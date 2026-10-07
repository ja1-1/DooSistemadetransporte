package transporte.infraestructura.persistencia.assembler.impl;

import transporte.dominio.MunicipioDominio;
import transporte.entidad.MunicipioEntidad;
import transporte.infraestructura.persistencia.assembler.EntidadAssembler;

import org.springframework.stereotype.Component;

@Component
public class MunicipioEntidadAssembler
        implements EntidadAssembler<MunicipioDominio, MunicipioEntidad> {
    @Override
    public MunicipioDominio convertirADominio(MunicipioEntidad municipioEntidad) {
        return new MunicipioDominio(
                municipioEntidad.id(),
                municipioEntidad.nombre(),
                municipioEntidad.departamento(),
                municipioEntidad.activo());
    }

    @Override
    public MunicipioEntidad convertirAEntidad(MunicipioDominio municipio) {
        return new MunicipioEntidad(
                municipio.getId(),
                municipio.getNombre(),
                municipio.getDepartamento(),
                municipio.isActivo());
    }
}
