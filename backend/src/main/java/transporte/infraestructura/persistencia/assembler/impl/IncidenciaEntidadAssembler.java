package transporte.infraestructura.persistencia.assembler.impl;

import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.Incidencia;
import transporte.dominio.SalidaDominio;
import transporte.dominio.enums.TipoIncidencia;
import transporte.dominio.valor.Responsable;
import transporte.entidad.IncidenciaEntidad;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IncidenciaEntidadAssembler {
    public Incidencia convertirADominio(IncidenciaEntidad incidenciaEntidad) {
        return SalidaDominio.reconstruirIncidencia(
                incidenciaEntidad.id(),
                TipoIncidencia.desdeId(incidenciaEntidad.tipoIncidenciaId()),
                incidenciaEntidad.instanteOcurrencia(),
                incidenciaEntidad.instanteReporte(),
                incidenciaEntidad.descripcion(),
                new Responsable(
                        incidenciaEntidad.responsableId(),
                        incidenciaEntidad.responsableNombre(),
                        incidenciaEntidad.responsableRol()));
    }

    public IncidenciaEntidad convertirAEntidad(Incidencia incidencia, UUID salidaId) {
        return new IncidenciaEntidad(
                incidencia.getId(),
                requerido(salidaId, "salidaId"),
                incidencia.getTipoIncidencia().getId(),
                incidencia.getInstanteOcurrencia(),
                incidencia.getInstanteReporte(),
                incidencia.getDescripcion(),
                incidencia.getResponsable().usuarioId(),
                incidencia.getResponsable().nombre(),
                incidencia.getResponsable().rol());
    }
}
