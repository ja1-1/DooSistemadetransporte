package transporte.dominio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;
import static transporte.transversal.utilitarios.Validacion.texto;

import transporte.transversal.mensajes.CatalogoMensajes;

import java.util.UUID;

public final class ViaDominio {
    private final UUID id;
    private String nombre;
    private String descripcion;
    private boolean activo;

    public ViaDominio(UUID id, String nombre, String descripcion, boolean activo) {
        this.id = requerido(id, "id");
        this.nombre = texto(nombre, "nombre", 3, 80);
        this.descripcion = texto(descripcion, "descripcion", 10, 250);
        this.activo = activo;
    }

    public static ViaDominio registrar(String nombre, String descripcion) {
        return new ViaDominio(UUID.randomUUID(), nombre, descripcion, true);
    }

    public void modificar(String nombre, String descripcion) {
        String nuevoNombre = texto(nombre, "nombre", 3, 80);
        String nuevoDetalle = texto(descripcion, "descripcion", 10, 250);
        this.nombre = nuevoNombre;
        this.descripcion = nuevoDetalle;
    }

    public void desactivar() {
        estado(activo, CatalogoMensajes.EL_REGISTRO_DEBE_ESTAR_ACTIVO);
        activo = false;
    }

    public void reactivar() {
        estado(!activo, CatalogoMensajes.EL_REGISTRO_DEBE_ESTAR_INACTIVO);
        activo = true;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isActivo() {
        return activo;
    }
}
