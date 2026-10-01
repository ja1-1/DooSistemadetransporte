package transporte.dominio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;
import static transporte.transversal.utilitarios.Validacion.texto;

import transporte.transversal.mensajes.CatalogoMensajes;

import java.util.UUID;

public final class SectorDominio {
    private final UUID id;
    private String nombre;
    private final UUID municipioId;
    private boolean activo;

    public SectorDominio(UUID id, String nombre, UUID municipioId, boolean activo) {
        this.id = requerido(id, "id");
        this.nombre = texto(nombre, "nombre", 1, 120);
        this.municipioId = requerido(municipioId, "municipioId");
        this.activo = activo;
    }

    public static SectorDominio registrar(String nombre, MunicipioDominio municipio) {
        requerido(municipio, "municipio");
        estado(municipio.isActivo(), CatalogoMensajes.EL_MUNICIPIO_DEBE_ESTAR_ACTIVO);
        return new SectorDominio(UUID.randomUUID(), nombre, municipio.getId(), true);
    }

    public void modificar(String nombre) {
        this.nombre = texto(nombre, "nombre", 1, 120);
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

    public UUID getMunicipioId() {
        return municipioId;
    }

    public boolean isActivo() {
        return activo;
    }
}
