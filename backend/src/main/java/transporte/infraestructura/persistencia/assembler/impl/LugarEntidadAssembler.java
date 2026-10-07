package transporte.infraestructura.persistencia.assembler.impl;

import transporte.dominio.LugarDominio;
import transporte.entidad.LugarEntidad;
import transporte.infraestructura.persistencia.assembler.EntidadAssembler;

import org.springframework.stereotype.Component;

@Component
public class LugarEntidadAssembler implements EntidadAssembler<LugarDominio, LugarEntidad> {
    @Override
    public LugarDominio convertirADominio(LugarEntidad lugarEntidad) {
        return new LugarDominio(
                lugarEntidad.id(),
                lugarEntidad.nombre(),
                lugarEntidad.sectorId(),
                lugarEntidad.activo());
    }

    @Override
    public LugarEntidad convertirAEntidad(LugarDominio lugar) {
        return new LugarEntidad(
                lugar.getId(), lugar.getNombre(), lugar.getSectorId(), lugar.isActivo());
    }
}
