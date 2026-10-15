package transporte.dominio.servicio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;
import static transporte.transversal.utilitarios.Validacion.texto;

import transporte.dominio.AsignacionDominio;
import transporte.dominio.CategoriaVehiculoDominio;
import transporte.dominio.ConductorDominio;
import transporte.dominio.ProgramacionRutaDominio;
import transporte.dominio.VehiculoDominio;
import transporte.dominio.enums.EstadoSalida;
import transporte.dominio.repositorio.AsignacionRepositorio;
import transporte.dominio.repositorio.CategoriaVehiculoRepositorio;
import transporte.dominio.repositorio.ConductorRepositorio;
import transporte.dominio.repositorio.ProgramacionRutaRepositorio;
import transporte.dominio.repositorio.SalidaRepositorio;
import transporte.dominio.repositorio.VehiculoRepositorio;
import transporte.dominio.valor.Periodo;
import transporte.dominio.valor.Responsable;
import transporte.dominio.valor.VentanaViaje;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.IdentidadDisponible;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class ServicioAsignacion {
    private final Clock reloj;
    private final AsignacionRepositorio asignaciones;
    private final CategoriaVehiculoRepositorio categorias;
    private final SalidaRepositorio salidas;
    private final ProgramacionRutaRepositorio programaciones;
    private final ConductorRepositorio conductores;
    private final VehiculoRepositorio vehiculos;

    public ServicioAsignacion(
            AsignacionRepositorio asignaciones,
            CategoriaVehiculoRepositorio categorias,
            SalidaRepositorio salidas,
            ProgramacionRutaRepositorio programaciones,
            ConductorRepositorio conductores,
            VehiculoRepositorio vehiculos,
            Clock reloj) {
        this.reloj = Objects.requireNonNull(reloj);
        this.asignaciones = Objects.requireNonNull(asignaciones);
        this.categorias = Objects.requireNonNull(categorias);
        this.salidas = Objects.requireNonNull(salidas);
        this.programaciones = Objects.requireNonNull(programaciones);
        this.conductores = Objects.requireNonNull(conductores);
        this.vehiculos = Objects.requireNonNull(vehiculos);
    }

    public AsignacionDominio asignar(
            ConductorDominio conductor,
            VehiculoDominio vehiculo,
            LocalDate fechaInicio) {
        var cat = categorias.buscarPorId(vehiculo.getCategoriaVehiculoId()).orElseThrow();
        AsignacionDominio nueva =
                IdentidadDisponible.crear(
                        () ->
                                AsignacionDominio.asignar(
                                        conductor, vehiculo, cat, fechaInicio),
                        candidato -> asignaciones.buscarPorId(candidato.getId()).isPresent());
        var periodo = nueva.getPeriodo();
        estado(
                asignaciones.buscarPorConductor(conductor.getId()).stream()
                        .noneMatch(asignacion -> asignacion.getPeriodo().seCruzaCon(periodo)),
                CatalogoMensajes.EL_CONDUCTOR_YA_TIENE_UNA_ASIGNACION_QUE);
        estado(
                asignaciones.buscarPorVehiculo(vehiculo.getId()).stream()
                        .noneMatch(
                                asignacionRecibido ->
                                        asignacionRecibido.getPeriodo().seCruzaCon(periodo)),
                CatalogoMensajes.EL_VEHICULO_YA_TIENE_UNA_ASIGNACION_QUE);
        asignaciones.guardar(nueva);
        return nueva;
    }

    public void finalizar(AsignacionDominio asignacion, LocalDate fechaFin) {
        estado(
                salidas.buscarNoCanceladasPorAsignacion(asignacion.getId()).stream()
                        .noneMatch(salida -> !salida.getEstadoSalida().esDefinitivo()),
                CatalogoMensajes.HAY_SALIDAS_PROGRAMADAS_O_EN_CURSO_QUE);
        estado(
                programaciones.buscarActivasPorAsignacion(asignacion.getId()).isEmpty(),
                CatalogoMensajes.HAY_PROGRAMACIONES_ACTIVAS_QUE_DEPENDEN_DE_LA);
        asignacion.finalizar(fechaFin);
        asignaciones.guardar(asignacion);
    }

    public AsignacionDominio relevarConductor(
            AsignacionDominio asignacion,
            ConductorDominio nuevoConductor,
            CategoriaVehiculoDominio categoria,
            LocalDate fechaRelevo,
            String motivo,
            Responsable responsable,
            LocalDate hoy) {
        requerido(fechaRelevo, "fechaRelevo");
        requerido(hoy, "hoy");
        texto(motivo, "motivo", 1, 1000);
        requerido(responsable, "responsable");
        estado(
                asignacion.getPeriodo().fin().isEmpty(),
                CatalogoMensajes.LA_ASIGNACION_YA_TIENE_FECHA_DE_FIN);
        estado(
                fechaRelevo.isAfter(hoy) && fechaRelevo.isAfter(asignacion.getPeriodo().inicio()),
                CatalogoMensajes.EL_RELEVO_DEBE_SER_POSTERIOR_A_HOY);
        estado(
                !nuevoConductor.getId().equals(asignacion.getConductorId()),
                CatalogoMensajes.EL_NUEVO_CONDUCTOR_DEBE_SER_DIFERENTE);
        var vehiculo = vehiculos.buscarPorId(asignacion.getVehiculoId()).orElseThrow();
        AsignacionDominio nueva =
                IdentidadDisponible.crear(
                        () -> AsignacionDominio.asignar(
                                nuevoConductor, vehiculo, categoria, fechaRelevo),
                        candidato -> asignaciones.buscarPorId(candidato.getId()).isPresent());
        var nuevoPeriodo = nueva.getPeriodo();
        estado(
                asignaciones.buscarPorConductor(nuevoConductor.getId()).stream()
                        .noneMatch(existente -> existente.getPeriodo().seCruzaCon(nuevoPeriodo)),
                CatalogoMensajes.EL_NUEVO_CONDUCTOR_YA_TIENE_UNA_ASIGNACION);
        var corte = fechaRelevo.atStartOfDay();
        var viajes = salidas.buscarNoCanceladasPorAsignacion(asignacion.getId());
        for (var salidaAfectada : viajes) {
            if (!salidaAfectada.getEstadoSalida().esDefinitivo()) {
                var ventanaActual = salidaAfectada.getVentana();
                boolean cruza =
                        ventanaActual.instanteSalidaProgramado().isBefore(corte)
                                && ventanaActual.instanteLlegadaEstimado().isAfter(corte);
                estado(
                        !cruza
                                && !(salidaAfectada.getEstadoSalida() == EstadoSalida.EN_CURSO
                                        && salidaAfectada
                                                .getInstanteSalidaReal()
                                                .orElseThrow()
                                                .isBefore(corte)),
                        CatalogoMensajes.UNA_SALIDA_PENDIENTE_CRUZA_LA_MEDIANOCHE_DEL);
            }
        }
        var futuras = salidas.buscarProgramadasPorAsignacionDesde(asignacion.getId(), fechaRelevo);
        for (var salidaAfectada : futuras) {
            estado(
                    nuevoConductor.estaHabilitadoPara(
                            categoria.getCategoriaLicenciaMinima(),
                            salidaAfectada.getVentana().instanteSalidaProgramado().toLocalDate()),
                    CatalogoMensajes.LA_LICENCIA_DEL_NUEVO_CONDUCTOR_NO_CUBRE);
        }
        var cambios = new ArrayList<ProgramacionRutaDominio>();
        var copias = new ArrayList<ProgramacionRutaDominio>();
        var ids = new HashMap<UUID, UUID>();
        for (var programacion : programaciones.buscarActivasPorAsignacion(asignacion.getId())) {
            if (programacion.getVigencia()
                    .fin()
                    .filter(fechaRecibido -> fechaRecibido.isBefore(fechaRelevo))
                    .isPresent()) {
                continue;
            }
            if (programacion.getVigencia().inicio().isBefore(fechaRelevo)) {
                ProgramacionRutaDominio copia =
                        IdentidadDisponible.crear(
                                () ->
                                        new ProgramacionRutaDominio(
                                                UUID.randomUUID(),
                                                programacion.getRutaId(),
                                                nueva.getId(),
                                                programacion.getHorario(),
                                                new Periodo(
                                                        fechaRelevo,
                                                        programacion.getVigencia().fin()),
                                                true),
                                candidato ->
                                        programaciones.buscarPorId(candidato.getId()).isPresent());
                programacion.cerrarVigencia(fechaRelevo.minusDays(1));
                cambios.add(programacion);
                copias.add(copia);
                ids.put(programacion.getId(), copia.getId());
            } else {
                programacion.modificar(nueva, programacion.getHorario());
                cambios.add(programacion);
                ids.put(programacion.getId(), programacion.getId());
            }
        }
        var ahora = LocalDateTime.now(reloj);
        String detalle =
                "; conductor anterior="
                        + asignacion.getConductorId()
                        + "; nuevo="
                        + nuevoConductor.getId();
        for (var salidaAfectada : futuras) {
            salidaAfectada.trasladarPorRelevo(
                    nueva.getId(),
                    salidaAfectada.getProgramacionRutaId()
                            .map(
                                    programacionId ->
                                            ids.getOrDefault(programacionId, programacionId)),
                    motivo,
                    responsable,
                    ahora,
                    detalle);
        }
        asignacion.finalizar(fechaRelevo.minusDays(1));
        asignaciones.guardar(asignacion);
        asignaciones.guardar(nueva);
        for (var programacion : cambios) {
            programaciones.guardar(programacion);
        }
        for (var programacion : copias) {
            programaciones.guardar(programacion);
        }
        for (var salidaAfectada : futuras) {
            salidas.guardar(salidaAfectada);
        }
        return nueva;
    }

    public List<AsignacionDominio> consultarDisponibles(
            VentanaViaje ventana, int capacidadMinima, LocalDateTime ahora) {
        requerido(ventana, "ventana");
        requerido(ahora, "ahora");
        if (capacidadMinima < 1 || ventana.instanteSalidaProgramado().isBefore(ahora)) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.LA_VENTANA_NO_PUEDE_EMPEZAR_ANTES_DE);
        }
        var fecha = ventana.instanteSalidaProgramado().toLocalDate();
        var periodo =
                new Periodo(
                        fecha,
                        Optional.of(ventana.instanteLlegadaEstimado().minusNanos(1).toLocalDate()));
        return asignaciones.buscarVigentesEn(fecha).stream()
                .filter(
                        asignacion -> {
                            var vehiculoAsignado =
                                    vehiculos
                                            .buscarPorId(asignacion.getVehiculoId())
                                            .orElseThrow();
                            var conductorAsignado =
                                    conductores
                                            .buscarPorId(asignacion.getConductorId())
                                            .orElseThrow();
                            var cat =
                                    categorias
                                            .buscarPorId(vehiculoAsignado.getCategoriaVehiculoId())
                                            .orElseThrow();
                            return vehiculoAsignado.isActivo()
                                    && vehiculoAsignado.getCapacidadPasajeros() >= capacidadMinima
                                    && asignacion.estaVigenteDurante(periodo)
                                    && conductorAsignado.estaHabilitadoPara(
                                            cat.getCategoriaLicenciaMinima(), fecha)
                                    && !salidas.existePorAsignacionEInstante(
                                            asignacion.getId(), ventana.instanteSalidaProgramado())
                                    && salidas
                                            .buscarNoCanceladasPorAsignacion(asignacion.getId())
                                            .stream()
                                            .noneMatch(
                                                    salida -> salida.ocupaAgendaEn(ventana, ahora));
                        })
                .sorted(
                        Comparator.comparingInt(
                                        (AsignacionDominio asignacionRecibido) ->
                                                vehiculos
                                                        .buscarPorId(
                                                                asignacionRecibido.getVehiculoId())
                                                        .orElseThrow()
                                                        .getCapacidadPasajeros())
                                .thenComparingLong(
                                        asignacionRecibidoRecibido ->
                                                salidas
                                                        .buscarNoCanceladasPorAsignacion(
                                                                asignacionRecibidoRecibido.getId())
                                                        .stream()
                                                        .filter(
                                                                salidaRecibido ->
                                                                        salidaRecibido
                                                                                .getVentana()
                                                                                .instanteSalidaProgramado()
                                                                                .toLocalDate()
                                                                                .equals(fecha))
                                                        .count())
                                .thenComparing(
                                        asignacionRecibidoRecibidoRecibido ->
                                                vehiculos
                                                        .buscarPorId(
                                                                asignacionRecibidoRecibidoRecibido
                                                                        .getVehiculoId())
                                                        .orElseThrow()
                                                        .getPlaca()
                                                        .valor()))
                .toList();
    }
}
