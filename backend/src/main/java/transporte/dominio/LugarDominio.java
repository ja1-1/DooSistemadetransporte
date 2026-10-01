package transporte.dominio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;
import static transporte.transversal.utilitarios.Validacion.texto;

import transporte.transversal.mensajes.CatalogoMensajes;

import java.util.UUID;

public final class LugarDominio {
    private final UUID id;
    private String nombre;
    private final UUID sectorId;
    private boolean activo;

    public LugarDominio(UUID id, String nombre, UUID sectorId, boolean activo) {
        this.id = requerido(id, "id");
        this.nombre = texto(nombre, "nombre", 1, 120);
        this.sectorId = requerido(sectorId, "sectorId");
        this.activo = activo;
    }

    public static LugarDominio registrar(
            String nombre, SectorDominio sector, MunicipioDominio municipio) {
        requerido(sector, "sector");
        requerido(municipio, "municipio");
        estado(
                sector.getMunicipioId().equals(municipio.getId()),
                CatalogoMensajes.EL_MUNICIPIO_NO_CORRESPONDE_AL_SECTOR);
        estado(
                sector.isActivo() && municipio.isActivo(),
                CatalogoMensajes.EL_SECTOR_Y_SU_MUNICIPIO_DEBEN_ESTAR);
        return new LugarDominio(UUID.randomUUID(), nombre, sector.getId(), true);
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

    public UUID getSectorId() {
        return sectorId;
    }

    public boolean isActivo() {
        return activo;
    }
}
