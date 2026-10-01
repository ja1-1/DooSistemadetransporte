package transporte.dominio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.valor.HorarioProgramado;
import transporte.dominio.valor.Periodo;
import transporte.dominio.valor.Recurrencias;
import transporte.transversal.mensajes.CatalogoMensajes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class ProgramacionRutaDominio {
    private final UUID id, rutaId;
    private UUID asignacionId;
    private HorarioProgramado horario;
    private Periodo vigencia;
    private boolean activa;

    public ProgramacionRutaDominio(
            UUID id,
            UUID rutaId,
            UUID asignacionId,
            HorarioProgramado horario,
            Periodo vigencia,
            boolean activa) {
        this.id = requerido(id, "id");
        this.rutaId = requerido(rutaId, "rutaId");
        this.asignacionId = requerido(asignacionId, "asignacionId");
        this.horario = requerido(horario, "horario");
        this.vigencia = requerido(vigencia, "vigencia");
        this.activa = activa;
    }

    public static ProgramacionRutaDominio registrar(
            RutaDominio ruta,
            AsignacionDominio asignacion,
            HorarioProgramado horario,
            Periodo vigencia,
            LocalDate hoy) {
        requerido(ruta, "ruta");
        requerido(asignacion, "asignacion");
        requerido(vigencia, "vigencia");
        requerido(hoy, "hoy");
        estado(ruta.isActiva(), CatalogoMensajes.LA_RUTA_DEBE_ESTAR_ACTIVA);
        estado(
                asignacion.estaVigenteDurante(vigencia),
                CatalogoMensajes.LA_ASIGNACION_NO_CUBRE_TODA_LA_VIGENCIA);
        if (vigencia.inicio().isBefore(hoy)) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.LA_VIGENCIA_NO_PUEDE_EMPEZAR_ANTES_DE);
        }
        return new ProgramacionRutaDominio(
                UUID.randomUUID(), ruta.getId(), asignacion.getId(), horario, vigencia, true);
    }

    public void modificar(AsignacionDominio asignacion, HorarioProgramado horario) {
        requerido(asignacion, "asignacion");
        requerido(horario, "horario");
        estado(
                asignacion.estaVigenteDurante(vigencia),
                CatalogoMensajes.LA_ASIGNACION_NO_CUBRE_TODA_LA_VIGENCIA);
        asignacionId = asignacion.getId();
        this.horario = horario;
    }

    public void desactivar() {
        estado(activa, CatalogoMensajes.LA_PROGRAMACION_DEBE_ESTAR_ACTIVA);
        activa = false;
    }

    public void reactivar() {
        estado(!activa, CatalogoMensajes.LA_PROGRAMACION_DEBE_ESTAR_INACTIVA);
        activa = true;
    }

    public void cerrarVigencia(LocalDate ultimaFecha) {
        vigencia = vigencia.cerrarEn(ultimaFecha);
    }

    public List<SalidaDominio> generarSalidasPara(
            LocalDate fecha, RutaDominio ruta, LocalDateTime ahora) {
        requerido(fecha, "fecha");
        requerido(ruta, "ruta");
        estado(activa, CatalogoMensajes.LA_PROGRAMACION_ESTA_DESACTIVADA);
        estado(
                ruta.getId().equals(rutaId),
                CatalogoMensajes.LA_RUTA_NO_CORRESPONDE_A_LA_PROGRAMACION);
        if (!vigencia.estaVigenteEn(fecha) || !horario.operaEl(fecha)) {
            return List.of();
        }
        return horario.calcularVentanas(fecha).stream()
                .map(
                        ventanaViaje ->
                                SalidaDominio.programar(
                                        ruta, asignacionId, ventanaViaje, id, ahora))
                .toList();
    }

    public boolean seCruzaCon(ProgramacionRutaDominio otra) {
        requerido(otra, "otra");
        return activa
                && otra.activa
                && rutaId.equals(otra.rutaId)
                && Recurrencias.seCruzan(horario, vigencia, otra.horario, otra.vigencia, false);
    }

    public boolean seSolapaEnAgendaCon(ProgramacionRutaDominio otra) {
        requerido(otra, "otra");
        return activa
                && otra.activa
                && asignacionId.equals(otra.asignacionId)
                && Recurrencias.seCruzan(horario, vigencia, otra.horario, otra.vigencia, true);
    }

    public UUID getId() {
        return id;
    }

    public UUID getRutaId() {
        return rutaId;
    }

    public UUID getAsignacionId() {
        return asignacionId;
    }

    public HorarioProgramado getHorario() {
        return horario;
    }

    public Periodo getVigencia() {
        return vigencia;
    }

    public boolean isActiva() {
        return activa;
    }
}
