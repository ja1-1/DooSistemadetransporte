package transporte.infraestructura.persistencia.assembler.impl;

import transporte.dominio.RutaDominio;
import transporte.dominio.enums.TipoRuta;
import transporte.entidad.RutaEntidad;
import transporte.infraestructura.persistencia.assembler.EntidadAssembler;

import org.springframework.stereotype.Component;

@Component
public class RutaEntidadAssembler implements EntidadAssembler<RutaDominio, RutaEntidad> {
    @Override
    public RutaDominio convertirADominio(RutaEntidad rutaEntidad) {
        return new RutaDominio(
                rutaEntidad.id(),
                rutaEntidad.nombre(),
                rutaEntidad.descripcion(),
                TipoRuta.desdeId(rutaEntidad.tipoRutaId()),
                rutaEntidad.origenLugarId(),
                rutaEntidad.destinoLugarId(),
                rutaEntidad.viaId(),
                rutaEntidad.distanciaTotalKm(),
                rutaEntidad.activa());
    }

    @Override
    public RutaEntidad convertirAEntidad(RutaDominio ruta) {
        return new RutaEntidad(
                ruta.getId(),
                ruta.getNombre(),
                ruta.getDescripcion(),
                ruta.isActiva(),
                ruta.getOrigenId(),
                ruta.getDestinoId(),
                ruta.getDistanciaTotalKm(),
                ruta.getTipoRuta().getId(),
                ruta.getViaId());
    }
}
