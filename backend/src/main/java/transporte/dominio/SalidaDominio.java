package transporte.dominio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.instante;
import static transporte.transversal.utilitarios.Validacion.opcional;
import static transporte.transversal.utilitarios.Validacion.requerido;
import static transporte.transversal.utilitarios.Validacion.texto;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import transporte.dominio.enums.EstadoSalida;
import transporte.dominio.enums.TipoIncidencia;
import transporte.dominio.puerto.NotificadorSalidas;
import transporte.dominio.valor.Responsable;
import transporte.dominio.valor.VentanaViaje;
import transporte.transversal.mensajes.CatalogoMensajes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public final class SalidaDominio {
    private static final Logger LOG = LoggerFactory.getLogger(SalidaDominio.class);
    private final UUID id, rutaId;
    private final String nombreRuta;
    private UUID asignacionId;
    private @Nullable UUID programacionRutaId;
    private VentanaViaje ventana;
    private @Nullable LocalDateTime instanteSalidaReal, instanteLlegadaReal;
    private EstadoSalida estadoSalida;
    private final List<Incidencia> incidencias;

    public SalidaDominio(
            UUID id,
            UUID rutaId,
            String nombreRuta,
            UUID asignacionId,
            VentanaViaje ventana,
            @Nullable LocalDateTime instanteSalidaReal,
            @Nullable LocalDateTime instanteLlegadaReal,
            EstadoSalida estadoSalida,
            @Nullable UUID programacionRutaId,
            List<Incidencia> incidencias) {
        this.id = requerido(id, "id");
        this.rutaId = requerido(rutaId, "rutaId");
        this.nombreRuta = texto(nombreRuta, "nombreRuta", 1, 600);
        this.asignacionId = requerido(asignacionId, "asignacionId");
        this.ventana = requerido(ventana, "ventana");
        this.estadoSalida = requerido(estadoSalida, "estadoSalida");
        if (instanteSalidaReal != null) {
            instanteSalidaReal = instante(instanteSalidaReal, "instanteSalidaReal");
        }
        if (instanteLlegadaReal != null) {
            instanteLlegadaReal = instante(instanteLlegadaReal, "instanteLlegadaReal");
        }
        if ((estadoSalida == EstadoSalida.PROGRAMADA
                        && (instanteSalidaReal != null || instanteLlegadaReal != null))
                || (estadoSalida == EstadoSalida.EN_CURSO
                        && (instanteSalidaReal == null || instanteLlegadaReal != null))
                || (estadoSalida == EstadoSalida.FINALIZADA
                        && (instanteSalidaReal == null
                                || instanteLlegadaReal == null
                                || !instanteLlegadaReal.isAfter(instanteSalidaReal)))
                || (estadoSalida == EstadoSalida.CANCELADA && instanteLlegadaReal != null)
                || (instanteSalidaReal != null
                        && instanteSalidaReal.isBefore(ventana.instanteSalidaProgramado()))) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.LOS_INSTANTES_REALES_NO_CORRESPONDEN_AL_ESTADO);
        }
        this.instanteSalidaReal = instanteSalidaReal;
        this.instanteLlegadaReal = instanteLlegadaReal;
        this.programacionRutaId = programacionRutaId;
        this.incidencias = new ArrayList<>(List.copyOf(requerido(incidencias, "incidencias")));
        if (this.incidencias.stream().map(Incidencia::getId).distinct().count()
                != this.incidencias.size()) {
            throw new IllegalArgumentException(CatalogoMensajes.UNA_INCIDENCIA_NO_PUEDE_REPETIRSE);
        }
    }

    public static Incidencia reconstruirIncidencia(
            UUID id,
            TipoIncidencia tipoIncidencia,
            LocalDateTime instanteOcurrencia,
            LocalDateTime instanteReporte,
            String descripcion,
            Responsable responsable) {
        return new Incidencia(
                id, tipoIncidencia, instanteOcurrencia, instanteReporte, descripcion, responsable);
    }

    public static SalidaDominio programar(
            RutaDominio ruta,
            UUID asignacionId,
            VentanaViaje ventana,
            @Nullable UUID programacionRutaId,
            LocalDateTime ahora) {
        requerido(ruta, "ruta");
        requerido(ventana, "ventana");
        requerido(ahora, "ahora");
        estado(ruta.isActiva(), CatalogoMensajes.LA_RUTA_DEBE_ESTAR_ACTIVA);
        estado(
                ventana.instanteSalidaProgramado().isAfter(ahora),
                CatalogoMensajes.LA_SALIDA_DEBE_SER_FUTURA);
        return new SalidaDominio(
                UUID.randomUUID(),
                ruta.getId(),
                ruta.getNombre(),
                asignacionId,
                ventana,
                null,
                null,
                EstadoSalida.PROGRAMADA,
                programacionRutaId,
                List.of());
    }

    public void iniciar(LocalDateTime ahora) {
        ahora = instante(ahora, "ahora");
        estado(
                estadoSalida == EstadoSalida.PROGRAMADA
                        && !ahora.isBefore(ventana.instanteSalidaProgramado())
                        && !ahora.isAfter(ventana.instanteLlegadaEstimado()),
                CatalogoMensajes.LA_SALIDA_NO_PUEDE_INICIARSE_FUERA_DE);
        instanteSalidaReal = ahora;
        estadoSalida = EstadoSalida.EN_CURSO;
    }

    public void finalizar(LocalDateTime ahora) {
        ahora = instante(ahora, "ahora");
        estado(
                estadoSalida == EstadoSalida.EN_CURSO,
                CatalogoMensajes.LA_SALIDA_DEBE_ESTAR_EN_CURSO);
        estado(
                ahora.isAfter(instanteSalidaReal),
                CatalogoMensajes.LA_LLEGADA_REAL_DEBE_SER_POSTERIOR_AL);
        instanteLlegadaReal = ahora;
        estadoSalida = EstadoSalida.FINALIZADA;
    }

    public void cancelar(
            String motivo,
            Responsable responsable,
            LocalDateTime ahora,
            NotificadorSalidas notificador) {
        estado(
                !estadoSalida.esDefinitivo(),
                CatalogoMensajes.LA_SALIDA_YA_TIENE_UN_ESTADO_DEFINITIVO);
        requerido(notificador, "notificador");
        registrarIncidencia(TipoIncidencia.CANCELACION, ahora, motivo, responsable, ahora);
        estadoSalida = EstadoSalida.CANCELADA;
        avisar(() -> notificador.notificarCancelacion(this));
    }

    public void reemplazarAsignacion(
            UUID nuevaAsignacionId,
            String motivo,
            Responsable responsable,
            LocalDateTime nuevaLlegadaEstimada,
            LocalDateTime ahora) {
        reemplazarAsignacion(
                nuevaAsignacionId, motivo, responsable, nuevaLlegadaEstimada, ahora, "");
    }

    public void validarOperacion(LocalDateTime ahora) {
        requerido(ahora, "ahora");
        estado(!estadoSalida.esDefinitivo(), CatalogoMensajes.LA_SALIDA_YA_TIENE_ESTADO_DEFINITIVO);
        estado(
                estadoSalida != EstadoSalida.PROGRAMADA
                        || !ahora.isAfter(ventana.instanteLlegadaEstimado()),
                CatalogoMensajes.LA_VENTANA_DE_LA_SALIDA_VENCIO_SOLO);
    }

    public void reemplazarAsignacion(
            UUID nuevaAsignacionId,
            String motivo,
            Responsable responsable,
            LocalDateTime nuevaLlegadaEstimada,
            LocalDateTime ahora,
            String detalleOperativo) {
        requerido(nuevaAsignacionId, "nuevaAsignacionId");
        nuevaLlegadaEstimada = instante(nuevaLlegadaEstimada, "nuevaLlegadaEstimada");
        ahora = instante(ahora, "ahora");
        validarOperacion(ahora);
        estado(
                !nuevaAsignacionId.equals(asignacionId),
                CatalogoMensajes.LA_ASIGNACION_DEBE_SER_DIFERENTE);
        if (estadoSalida == EstadoSalida.PROGRAMADA) {
            estado(
                    nuevaLlegadaEstimada.equals(ventana.instanteLlegadaEstimado()),
                    CatalogoMensajes.UNA_SALIDA_PROGRAMADA_CONSERVA_SU_VENTANA_DURANTE);
        } else {
            estado(
                    nuevaLlegadaEstimada.isAfter(ahora),
                    CatalogoMensajes.LA_NUEVA_LLEGADA_DEBE_SER_FUTURA);
        }
        var nuevaVentana =
                new VentanaViaje(ventana.instanteSalidaProgramado(), nuevaLlegadaEstimada);
        registrarIncidencia(
                TipoIncidencia.REEMPLAZO_DE_VEHICULO,
                ahora,
                Incidencia.describirCambio(
                        motivo,
                        requerido(detalleOperativo, "detalleOperativo")
                                + "; asignación anterior="
                                + asignacionId
                                + "; nueva="
                                + nuevaAsignacionId
                                + "; llegada anterior="
                                + ventana.instanteLlegadaEstimado()
                                + "; nueva="
                                + nuevaLlegadaEstimada),
                responsable,
                ahora);
        asignacionId = nuevaAsignacionId;
        ventana = nuevaVentana;
    }

    public void reprogramar(
            VentanaViaje nuevaVentana,
            String motivo,
            Responsable responsable,
            LocalDateTime ahora,
            NotificadorSalidas notificador) {
        requerido(nuevaVentana, "nuevaVentana");
        ahora = instante(ahora, "ahora");
        requerido(notificador, "notificador");
        validarOperacion(ahora);
        estado(
                estadoSalida == EstadoSalida.PROGRAMADA,
                CatalogoMensajes.SOLO_SE_REPROGRAMA_UNA_SALIDA_PROGRAMADA);
        estado(
                nuevaVentana.instanteSalidaProgramado().isAfter(ahora)
                        && !nuevaVentana.equals(ventana),
                CatalogoMensajes.LA_NUEVA_VENTANA_DEBE_SER_FUTURA_Y);
        estado(
                programacionRutaId == null
                        || nuevaVentana
                                .instanteSalidaProgramado()
                                .toLocalDate()
                                .equals(ventana.instanteSalidaProgramado().toLocalDate()),
                CatalogoMensajes.UNA_SALIDA_DE_PROGRAMACION_DEBE_CONSERVAR_SU);
        registrarIncidencia(
                TipoIncidencia.REPROGRAMACION,
                ahora,
                Incidencia.describirCambio(
                        motivo, "; ventana anterior=" + ventana + "; nueva=" + nuevaVentana),
                responsable,
                ahora);
        var anterior = ventana.instanteSalidaProgramado();
        ventana = nuevaVentana;
        avisar(() -> notificador.notificarReprogramacion(this, anterior));
    }

    public void trasladarPorRelevo(
            UUID nuevaAsignacionId,
            Optional<UUID> nuevaProgramacionRutaId,
            String motivo,
            Responsable responsable,
            LocalDateTime ahora) {
        trasladarPorRelevo(
                nuevaAsignacionId, nuevaProgramacionRutaId, motivo, responsable, ahora, "");
    }

    public void trasladarPorRelevo(
            UUID nuevaAsignacionId,
            Optional<UUID> nuevaProgramacionRutaId,
            String motivo,
            Responsable responsable,
            LocalDateTime ahora,
            String detalleOperativo) {
        requerido(nuevaAsignacionId, "nuevaAsignacionId");
        opcional(nuevaProgramacionRutaId, "nuevaProgramacionRutaId");
        estado(
                estadoSalida == EstadoSalida.PROGRAMADA,
                CatalogoMensajes.SOLO_SE_TRASLADAN_SALIDAS_PROGRAMADAS);
        registrarIncidencia(
                TipoIncidencia.RELEVO_DE_CONDUCTOR,
                ahora,
                Incidencia.describirCambio(
                        motivo,
                        requerido(detalleOperativo, "detalleOperativo")
                                + "; asignación anterior="
                                + asignacionId
                                + "; nueva="
                                + nuevaAsignacionId),
                responsable,
                ahora);
        asignacionId = nuevaAsignacionId;
        programacionRutaId = nuevaProgramacionRutaId.orElse(null);
    }

    public Incidencia registrarIncidencia(
            TipoIncidencia tipoIncidencia,
            LocalDateTime instanteOcurrencia,
            String descripcion,
            Responsable responsable,
            LocalDateTime ahora) {
        var incidencia =
                Incidencia.registrar(
                        tipoIncidencia, instanteOcurrencia, descripcion, responsable, ahora);
        incidencias.add(incidencia);
        return incidencia;
    }

    public void asegurarIdentidadesIncidencias(
            Predicate<Incidencia> registrada, Predicate<UUID> ocupada) {
        requerido(registrada, "registrada");
        requerido(ocupada, "ocupada");
        var usadas = new HashSet<UUID>();
        for (var incidencia : incidencias) {
            if (!registrada.test(incidencia)) {
                incidencia.asegurarIdentidadDisponible(
                        candidata -> usadas.contains(candidata) || ocupada.test(candidata));
            }
            usadas.add(incidencia.getId());
        }
    }

    public List<Incidencia> consultarHistorialIncidencias() {
        return incidencias.stream()
                .sorted(
                        Comparator.comparing(Incidencia::getInstanteOcurrencia)
                                .thenComparing(Incidencia::getInstanteReporte))
                .toList();
    }

    public boolean ocupaAgendaEn(VentanaViaje ventana, LocalDateTime ahora) {
        requerido(ventana, "ventana");
        requerido(ahora, "ahora");
        return estadoSalida == EstadoSalida.EN_CURSO
                ? ventana.instanteLlegadaEstimado().isAfter(instanteSalidaReal)
                : estadoSalida == EstadoSalida.PROGRAMADA
                        && !this.ventana.instanteLlegadaEstimado().isBefore(ahora)
                        && this.ventana.seSolapaCon(ventana);
    }

    private void avisar(Runnable accion) {
        try {
            accion.run();
        } catch (RuntimeException error) {
            LOG.warn(
                    CatalogoMensajes.NO_FUE_POSIBLE_PUBLICAR_EL_AVISO_DE,
                    id,
                    error.getClass().getSimpleName());
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getRutaId() {
        return rutaId;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public UUID getAsignacionId() {
        return asignacionId;
    }

    public Optional<UUID> getProgramacionRutaId() {
        return Optional.ofNullable(programacionRutaId);
    }

    public VentanaViaje getVentana() {
        return ventana;
    }

    public Optional<LocalDateTime> getInstanteSalidaReal() {
        return Optional.ofNullable(instanteSalidaReal);
    }

    public Optional<LocalDateTime> getInstanteLlegadaReal() {
        return Optional.ofNullable(instanteLlegadaReal);
    }

    public EstadoSalida getEstadoSalida() {
        return estadoSalida;
    }
}
