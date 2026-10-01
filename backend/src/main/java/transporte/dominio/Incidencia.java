package transporte.dominio;

import static transporte.transversal.utilitarios.Validacion.instante;
import static transporte.transversal.utilitarios.Validacion.requerido;
import static transporte.transversal.utilitarios.Validacion.texto;

import transporte.dominio.enums.TipoIncidencia;
import transporte.dominio.valor.Responsable;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.IdentidadDisponible;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Predicate;

public final class Incidencia {
    private UUID id;
    private final TipoIncidencia tipoIncidencia;
    private final LocalDateTime instanteOcurrencia, instanteReporte;
    private final String descripcion;
    private final Responsable responsable;

    Incidencia(
            UUID id,
            TipoIncidencia tipo,
            LocalDateTime ocurrencia,
            LocalDateTime reporte,
            String descripcion,
            Responsable responsable) {
        this.id = requerido(id, "id");
        tipoIncidencia = requerido(tipo, "tipoIncidencia");
        instanteOcurrencia = instante(ocurrencia, "instanteOcurrencia");
        instanteReporte = instante(reporte, "instanteReporte");
        if (instanteOcurrencia.isAfter(instanteReporte)) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.LA_OCURRENCIA_NO_PUEDE_SER_POSTERIOR_AL);
        }
        this.descripcion = texto(descripcion, "descripcion", 1, 1000);
        this.responsable = requerido(responsable, "responsable");
    }

    public static String describirCambio(String motivo, String detalle) {
        requerido(detalle, "detalle");
        int disponible = 1000 - detalle.codePointCount(0, detalle.length());
        if (disponible < 1) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.EL_DETALLE_DE_LA_OPERACION_EXCEDE_EL);
        }
        return texto(motivo, "motivo", 1, disponible) + detalle;
    }

    static Incidencia registrar(
            TipoIncidencia tipo,
            LocalDateTime ocurrencia,
            String descripcion,
            Responsable responsable,
            LocalDateTime ahora) {
        return new Incidencia(UUID.randomUUID(), tipo, ocurrencia, ahora, descripcion, responsable);
    }

    void asegurarIdentidadDisponible(Predicate<UUID> ocupada) {
        if (ocupada.test(id)) {
            id = IdentidadDisponible.crear(UUID::randomUUID, ocupada);
        }
    }

    public UUID getId() {
        return id;
    }

    public TipoIncidencia getTipoIncidencia() {
        return tipoIncidencia;
    }

    public LocalDateTime getInstanteOcurrencia() {
        return instanteOcurrencia;
    }

    public LocalDateTime getInstanteReporte() {
        return instanteReporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Responsable getResponsable() {
        return responsable;
    }
}
