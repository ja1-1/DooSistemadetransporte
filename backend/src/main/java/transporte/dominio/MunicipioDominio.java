package transporte.dominio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;
import static transporte.transversal.utilitarios.Validacion.texto;

import transporte.transversal.mensajes.CatalogoMensajes;

import java.util.UUID;

public final class MunicipioDominio {
    private final UUID id;
    private String nombre;
    private String departamento;
    private boolean activo;

    public MunicipioDominio(UUID id, String nombre, String departamento, boolean activo) {
        this.id = requerido(id, "id");
        this.nombre = texto(nombre, "nombre", 1, 120);
        this.departamento = texto(departamento, "departamento", 1, 80);
        this.activo = activo;
    }

    public static MunicipioDominio registrar(String nombre, String departamento) {
        return new MunicipioDominio(UUID.randomUUID(), nombre, departamento, true);
    }

    public void modificar(String nombre, String departamento) {
        String nuevoNombre = texto(nombre, "nombre", 1, 120);
        String nuevoDetalle = texto(departamento, "departamento", 1, 80);
        this.nombre = nuevoNombre;
        this.departamento = nuevoDetalle;
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

    public String getDepartamento() {
        return departamento;
    }

    public boolean isActivo() {
        return activo;
    }
}
