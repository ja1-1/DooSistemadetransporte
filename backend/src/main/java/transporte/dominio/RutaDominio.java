package transporte.dominio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;
import static transporte.transversal.utilitarios.Validacion.texto;

import transporte.dominio.enums.TipoRuta;
import transporte.transversal.mensajes.CatalogoMensajes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;

public final class RutaDominio {
    private final UUID id;
    private final TipoRuta tipoRuta;
    private String nombre, descripcion;
    private UUID origenId, destinoId, viaId;
    private BigDecimal distanciaTotalKm;
    private boolean activa;

    public RutaDominio(
            UUID id,
            String nombre,
            String descripcion,
            TipoRuta tipoRuta,
            UUID origenId,
            UUID destinoId,
            UUID viaId,
            BigDecimal distanciaTotalKm,
            boolean activa) {
        this.id = requerido(id, "id");
        this.tipoRuta = requerido(tipoRuta, "tipoRuta");
        this.nombre = texto(nombre, "nombre", 1, 600);
        this.descripcion = texto(descripcion, "descripcion", 1, 500);
        this.origenId = requerido(origenId, "origen");
        this.destinoId = requerido(destinoId, "destino");
        this.viaId = requerido(viaId, "via");
        this.distanciaTotalKm = distancia(distanciaTotalKm);
        this.activa = activa;
    }

    public static BigDecimal distancia(BigDecimal valor) {
        requerido(valor, "distanciaTotalKm");
        if (valor.compareTo(new BigDecimal("0.005")) < 0
                || valor.compareTo(new BigDecimal("999999.995")) >= 0) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.LA_DISTANCIA_DEBE_SER_POSITIVA_CON_HASTA);
        }
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private static String nombre(
            TipoRuta tipo,
            LugarDominio origen,
            LugarDominio destino,
            ViaDominio via,
            Map<UUID, SectorDominio> sectores,
            Map<UUID, MunicipioDominio> municipios) {
        requerido(tipo, "tipoRuta");
        requerido(origen, "origen");
        requerido(destino, "destino");
        requerido(via, "via");
        requerido(sectores, "sectores");
        requerido(municipios, "municipios");
        var so = requerido(sectores.get(origen.getSectorId()), "sectorOrigen");
        var sd = requerido(sectores.get(destino.getSectorId()), "sectorDestino");
        var mo = requerido(municipios.get(so.getMunicipioId()), "municipioOrigen");
        var md = requerido(municipios.get(sd.getMunicipioId()), "municipioDestino");
        if (!so.getId().equals(origen.getSectorId())
                || !sd.getId().equals(destino.getSectorId())
                || !mo.getId().equals(so.getMunicipioId())
                || !md.getId().equals(sd.getMunicipioId())) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.LAS_RELACIONES_GEOGRAFICAS_NO_SON_COHERENTES);
        }
        if (!(origen.isActivo()
                && destino.isActivo()
                && via.isActivo()
                && so.isActivo()
                && sd.isActivo()
                && mo.isActivo()
                && md.isActivo())) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.LOS_LUGARES_SECTORES_MUNICIPIOS_Y_VIA_DEBEN);
        }
        boolean mismo = mo.getId().equals(md.getId());
        if ((tipo == TipoRuta.URBANA) != mismo) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.EL_RECORRIDO_NO_CORRESPONDE_AL_TIPO_DE);
        }
        return tipo == TipoRuta.URBANA
                ? origen.getNombre()
                        + " ("
                        + so.getNombre()
                        + ") > "
                        + destino.getNombre()
                        + " ("
                        + sd.getNombre()
                        + ") | "
                        + via.getNombre()
                : mo.getNombre()
                        + " ("
                        + mo.getDepartamento()
                        + ") > "
                        + md.getNombre()
                        + " ("
                        + md.getDepartamento()
                        + ") | "
                        + via.getNombre();
    }

    public static RutaDominio registrar(
            String descripcion,
            TipoRuta tipoRuta,
            LugarDominio origen,
            LugarDominio destino,
            ViaDominio via,
            BigDecimal distanciaTotalKm,
            boolean existeRecorrido,
            Map<UUID, SectorDominio> sectoresActivos,
            Map<UUID, MunicipioDominio> municipiosActivos) {
        estado(!existeRecorrido, CatalogoMensajes.YA_EXISTE_ESE_RECORRIDO);
        return new RutaDominio(
                UUID.randomUUID(),
                nombre(tipoRuta, origen, destino, via, sectoresActivos, municipiosActivos),
                descripcion,
                tipoRuta,
                origen.getId(),
                destino.getId(),
                via.getId(),
                distanciaTotalKm,
                true);
    }

    public void modificar(String descripcion) {
        this.descripcion = texto(descripcion, "descripcion", 1, 500);
    }

    public void cambiarRecorrido(
            LugarDominio origen,
            LugarDominio destino,
            ViaDominio via,
            BigDecimal distanciaTotalKm,
            boolean tieneSalidasPendientes,
            boolean existeRecorrido,
            Map<UUID, SectorDominio> sectoresActivos,
            Map<UUID, MunicipioDominio> municipiosActivos) {
        estado(!tieneSalidasPendientes, CatalogoMensajes.LA_RUTA_TIENE_SALIDAS_PENDIENTES);
        estado(!existeRecorrido, CatalogoMensajes.YA_EXISTE_ESE_RECORRIDO);
        String nuevo = nombre(tipoRuta, origen, destino, via, sectoresActivos, municipiosActivos);
        BigDecimal distanciaValidada = distancia(distanciaTotalKm);
        nombre = texto(nuevo, "nombre", 1, 600);
        origenId = origen.getId();
        destinoId = destino.getId();
        viaId = via.getId();
        this.distanciaTotalKm = distanciaValidada;
    }

    public void desactivar() {
        estado(activa, CatalogoMensajes.LA_RUTA_DEBE_ESTAR_ACTIVA);
        activa = false;
    }

    public void reactivar() {
        estado(!activa, CatalogoMensajes.LA_RUTA_DEBE_ESTAR_INACTIVA);
        activa = true;
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

    public TipoRuta getTipoRuta() {
        return tipoRuta;
    }

    public UUID getOrigenId() {
        return origenId;
    }

    public UUID getDestinoId() {
        return destinoId;
    }

    public UUID getViaId() {
        return viaId;
    }

    public BigDecimal getDistanciaTotalKm() {
        return distanciaTotalKm;
    }

    public boolean isActiva() {
        return activa;
    }
}
