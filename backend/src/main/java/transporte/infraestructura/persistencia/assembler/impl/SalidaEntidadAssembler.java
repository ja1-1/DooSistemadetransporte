package transporte.infraestructura.persistencia.assembler.impl;

import transporte.dominio.SalidaDominio;
import transporte.dominio.enums.EstadoSalida;
import transporte.dominio.valor.VentanaViaje;
import transporte.entidad.SalidaEntidad;
import transporte.infraestructura.persistencia.assembler.EntidadAssembler;

import org.springframework.stereotype.Component;

@Component
public class SalidaEntidadAssembler implements EntidadAssembler<SalidaDominio, SalidaEntidad> {
    private final IncidenciaEntidadAssembler incidencias;

    public SalidaEntidadAssembler(IncidenciaEntidadAssembler incidencias) {
        this.incidencias = incidencias;
    }

    @Override
    public SalidaDominio convertirADominio(SalidaEntidad salidaEntidad) {
        return new SalidaDominio(
                salidaEntidad.id(),
                salidaEntidad.rutaId(),
                salidaEntidad.nombreRuta(),
                salidaEntidad.asignacionId(),
                new VentanaViaje(
                        salidaEntidad.instanteSalidaProgramado(),
                        salidaEntidad.instanteLlegadaEstimado()),
                salidaEntidad.instanteSalidaReal(),
                salidaEntidad.instanteLlegadaReal(),
                EstadoSalida.desdeId(salidaEntidad.estadoSalidaId()),
                salidaEntidad.programacionRutaId(),
                salidaEntidad.incidencias().stream()
                        .map(incidencia -> incidencias.convertirADominio(incidencia))
                        .toList());
    }

    @Override
    public SalidaEntidad convertirAEntidad(SalidaDominio salida) {
        return new SalidaEntidad(
                salida.getId(),
                salida.getRutaId(),
                salida.getNombreRuta(),
                salida.getAsignacionId(),
                salida.getVentana().instanteSalidaProgramado(),
                salida.getVentana().instanteLlegadaEstimado(),
                salida.getInstanteSalidaReal().orElse(null),
                salida.getInstanteLlegadaReal().orElse(null),
                salida.getEstadoSalida().getId(),
                salida.getProgramacionRutaId().orElse(null),
                salida.consultarHistorialIncidencias().stream()
                        .map(
                                incidencia ->
                                        incidencias.convertirAEntidad(incidencia, salida.getId()))
                        .toList());
    }
}
