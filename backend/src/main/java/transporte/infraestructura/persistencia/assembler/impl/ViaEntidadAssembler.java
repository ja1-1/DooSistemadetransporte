package transporte.infraestructura.persistencia.assembler.impl;

import transporte.dominio.ViaDominio;
import transporte.entidad.ViaEntidad;
import transporte.infraestructura.persistencia.assembler.EntidadAssembler;

import org.springframework.stereotype.Component;

@Component
public class ViaEntidadAssembler implements EntidadAssembler<ViaDominio, ViaEntidad> {
    @Override
    public ViaDominio convertirADominio(ViaEntidad viaEntidad) {
        return new ViaDominio(
                viaEntidad.id(),
                viaEntidad.nombre(),
                viaEntidad.descripcion(),
                viaEntidad.activo());
    }

    @Override
    public ViaEntidad convertirAEntidad(ViaDominio via) {
        return new ViaEntidad(via.getId(), via.getNombre(), via.getDescripcion(), via.isActivo());
    }
}
