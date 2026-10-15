package transporte.dominio.servicio;

import static transporte.transversal.utilitarios.Validacion.estado;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.AsignacionDominio;
import transporte.dominio.Incidencia;
import transporte.dominio.ProgramacionRutaDominio;
import transporte.dominio.RutaDominio;
import transporte.dominio.SalidaDominio;
import transporte.dominio.consulta.FiltroSalida;
import transporte.dominio.enums.EstadoSalida;
import transporte.dominio.enums.TipoIncidencia;
import transporte.dominio.puerto.NotificadorSalidas;
import transporte.dominio.repositorio.AsignacionRepositorio;
import transporte.dominio.repositorio.CategoriaVehiculoRepositorio;
import transporte.dominio.repositorio.ConductorRepositorio;
import transporte.dominio.repositorio.ProgramacionRutaRepositorio;
import transporte.dominio.repositorio.RutaRepositorio;
import transporte.dominio.repositorio.SalidaRepositorio;
import transporte.dominio.repositorio.VehiculoRepositorio;
import transporte.dominio.valor.HorarioProgramado;
import transporte.dominio.valor.Periodo;
import transporte.dominio.valor.Responsable;
import transporte.dominio.valor.ResultadoGeneracion;
import transporte.dominio.valor.VentanaViaje;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.IdentidadDisponible;

import org.jspecify.annotations.Nullable;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class ServicioProgramacion {
    private final ProgramacionRutaRepositorio programaciones;
    private final SalidaRepositorio salidas;
    private final RutaRepositorio rutas;
    private final AsignacionRepositorio asignaciones;
    private final ConductorRepositorio conductores;
    private final VehiculoRepositorio vehiculos;
    private final CategoriaVehiculoRepositorio categorias;
    private final NotificadorSalidas notificador;
    private final Clock reloj;

    public ServicioProgramacion(
            ProgramacionRutaRepositorio programaciones,
            SalidaRepositorio salidas,
            RutaRepositorio rutas,
            AsignacionRepositorio asignaciones,
            ConductorRepositorio conductores,
            VehiculoRepositorio vehiculos,
            CategoriaVehiculoRepositorio categorias,
            NotificadorSalidas notificador,
            Clock reloj) {
        this.programaciones = Objects.requireNonNull(programaciones);
        this.salidas = Objects.requireNonNull(salidas);
        this.rutas = Objects.requireNonNull(rutas);
        this.asignaciones = Objects.requireNonNull(asignaciones);
        this.conductores = Objects.requireNonNull(conductores);
        this.vehiculos = Objects.requireNonNull(vehiculos);
        this.categorias = Objects.requireNonNull(categorias);
        this.notificador = Objects.requireNonNull(notificador);
        this.reloj = Objects.requireNonNull(reloj);
    }

    private void cruces(ProgramacionRutaDominio programacionRuta) {
        for (var otra : programaciones.buscarActivasPorRuta(programacionRuta.getRutaId())) {
            if (!otra.getId().equals(programacionRuta.getId())) {
                estado(
                        !programacionRuta.seCruzaCon(otra),
                        CatalogoMensajes.LA_PROGRAMACION_COINCIDE_CON_OTRA_SALIDA_DE);
            }
        }
        for (var otra :
                programaciones.buscarActivasPorAsignacion(programacionRuta.getAsignacionId())) {
            if (!otra.getId().equals(programacionRuta.getId())) {
                estado(
                        !programacionRuta.seSolapaEnAgendaCon(otra),
                        CatalogoMensajes.LA_PROGRAMACION_SOLAPA_LA_AGENDA_DE_SU);
            }
        }
    }

    public ProgramacionRutaDominio registrarProgramacion(
            RutaDominio ruta,
            AsignacionDominio asignacion,
            HorarioProgramado horario,
            Periodo vigencia,
            LocalDate hoy) {
        ProgramacionRutaDominio programacion =
                IdentidadDisponible.crear(
                        () ->
                                ProgramacionRutaDominio.registrar(
                                        ruta,
                                        asignacion,
                                        horario,
                                        vigencia,
                                        hoy),
                        candidato -> programaciones.buscarPorId(candidato.getId()).isPresent());
        cruces(programacion);
        programaciones.guardar(programacion);
        return programacion;
    }

    public List<SalidaDominio> modificarProgramacion(
            ProgramacionRutaDominio programacion,
            AsignacionDominio asignacion,
            HorarioProgramado horario,
            boolean reprogramarSalidasGeneradas,
            Responsable responsable,
            LocalDateTime ahora) {
        var anterior = programacion.getHorario();
        var candidata =
                new ProgramacionRutaDominio(
                        programacion.getId(),
                        programacion.getRutaId(),
                        asignacion.getId(),
                        horario,
                        programacion.getVigencia(),
                        programacion.isActiva());
        estado(
                asignacion.estaVigenteDurante(programacion.getVigencia()),
                CatalogoMensajes.LA_ASIGNACION_NO_CUBRE_TODA_LA_VIGENCIA);
        cruces(candidata);
        var rechazadas = new ArrayList<SalidaDominio>();
        programacion.modificar(asignacion, horario);
        programaciones.guardar(programacion);
        if (reprogramarSalidasGeneradas
                && anterior.horaSalida().equals(anterior.horaUltimaSalida())
                && horario.horaSalida().equals(horario.horaUltimaSalida())) {
            for (var salidaDominio :
                    salidas.relacionados(
                            FiltroSalida.todos()
                                    .conProgramacionRutaId(programacion.getId())
                                    .conEstadoSalida(EstadoSalida.PROGRAMADA))) {
                var inicio =
                        salidaDominio.getVentana()
                                .instanteSalidaProgramado()
                                .toLocalDate()
                                .atTime(horario.horaSalida());
                var ventana =
                        new VentanaViaje(
                                inicio, inicio.plusMinutes(horario.duracionEstimada()));
                if (ventana.equals(salidaDominio.getVentana())) {
                    continue;
                }
                try {
                    reprogramarSalida(
                            salidaDominio,
                            ventana,
                            "Cambio de horario de la programación",
                            responsable,
                            ahora);
                } catch (IllegalArgumentException | IllegalStateException error) {
                    rechazadas.add(salidaDominio);
                }
            }
        }
        return List.copyOf(rechazadas);
    }

    public ResultadoGeneracion generarSalidas(
            ProgramacionRutaDominio programacion,
            LocalDate fechaInicial,
            LocalDate fechaFinal,
            LocalDate hoy) {
        requerido(fechaInicial, "fechaInicial");
        requerido(fechaFinal, "fechaFinal");
        requerido(hoy, "hoy");
        estado(
                !fechaInicial.isBefore(hoy)
                        && !fechaFinal.isBefore(fechaInicial)
                        && !fechaFinal.isAfter(hoy.plusDays(7)),
                CatalogoMensajes.EL_RANGO_DEBE_ESTAR_ENTRE_HOY_Y);
        estado(programacion.isActiva(), CatalogoMensajes.LA_PROGRAMACION_ESTA_DESACTIVADA);
        var nuevas = new ArrayList<SalidaDominio>();
        var existentes = new ArrayList<SalidaDominio>();
        var rechazos = new LinkedHashMap<LocalDateTime, String>();
        var ruta = rutas.buscarPorId(programacion.getRutaId()).orElseThrow();
        var asignacionDeSalida =
                asignaciones.buscarPorId(programacion.getAsignacionId()).orElseThrow();
        var ahora = LocalDateTime.now(reloj);
        for (var fecha = fechaInicial; !fecha.isAfter(fechaFinal); fecha = fecha.plusDays(1)) {
            if (!programacion.getVigencia().estaVigenteEn(fecha)) {
                continue;
            }
            for (var ventana : programacion.getHorario().calcularVentanas(fecha)) {
                var iguales =
                        salidas.relacionados(
                                FiltroSalida.todos()
                                        .conProgramacionRutaId(programacion.getId())
                                        .conInstanteSalidaProgramado(
                                                ventana.instanteSalidaProgramado()));
                if (!iguales.isEmpty()) {
                    existentes.add(iguales.getFirst());
                    continue;
                }
                try {
                    validarVentana(asignacionDeSalida, ventana, null, ahora);
                    SalidaDominio salida =
                            IdentidadDisponible.crear(
                                    () ->
                                            SalidaDominio.programar(
                                                    ruta,
                                                    asignacionDeSalida.getId(),
                                                    ventana,
                                                    programacion.getId(),
                                                    ahora),
                                    candidato ->
                                            salidas.buscarPorId(candidato.getId()).isPresent());
                    salidas.guardar(salida);
                    nuevas.add(salida);
                } catch (IllegalArgumentException | IllegalStateException error) {
                    rechazos.put(ventana.instanteSalidaProgramado(), error.getMessage());
                }
            }
        }
        return new ResultadoGeneracion(nuevas, existentes, rechazos);
    }

    private void validarVentana(
            AsignacionDominio asignacion,
            VentanaViaje ventanaViaje,
            @Nullable UUID excluir,
            LocalDateTime ahora) {
        requerido(asignacion, "asignacion");
        requerido(ventanaViaje, "ventana");
        var periodo =
                new Periodo(
                        ventanaViaje.instanteSalidaProgramado().toLocalDate(),
                        Optional.of(
                                ventanaViaje
                                        .instanteLlegadaEstimado()
                                        .minusNanos(1)
                                        .toLocalDate()));
        estado(
                asignacion.estaVigenteDurante(periodo),
                CatalogoMensajes.LA_ASIGNACION_NO_ESTA_VIGENTE_DURANTE_TODA);
        var vehiculo = vehiculos.buscarPorId(asignacion.getVehiculoId()).orElseThrow();
        var conductor = conductores.buscarPorId(asignacion.getConductorId()).orElseThrow();
        var categoria = categorias.buscarPorId(vehiculo.getCategoriaVehiculoId()).orElseThrow();
        estado(
                vehiculo.isActivo()
                        && conductor.estaHabilitadoPara(
                                categoria.getCategoriaLicenciaMinima(),
                                ventanaViaje.instanteSalidaProgramado().toLocalDate()),
                CatalogoMensajes.EL_CONDUCTOR_SU_LICENCIA_O_EL_VEHICULO);
        for (var salidaDominio :
                salidas.relacionados(FiltroSalida.todos().conAsignacionId(asignacion.getId()))) {
            if (!salidaDominio.getId().equals(excluir)) {
                estado(
                        !salidaDominio.getVentana()
                                .instanteSalidaProgramado()
                                .equals(ventanaViaje.instanteSalidaProgramado()),
                        CatalogoMensajes.YA_EXISTE_UNA_SALIDA_DE_LA_ASIGNACION);
                estado(
                        !salidaDominio.ocupaAgendaEn(ventanaViaje, ahora),
                        CatalogoMensajes.LA_AGENDA_DE_LA_ASIGNACION_ESTA_OCUPADA);
            }
        }
    }

    public SalidaDominio programarSalida(
            RutaDominio ruta,
            AsignacionDominio asignacion,
            VentanaViaje ventana,
            LocalDateTime ahora) {
        validarVentana(asignacion, ventana, null, ahora);
        SalidaDominio salidaDominio =
                IdentidadDisponible.crear(
                        () ->
                                SalidaDominio.programar(
                                        ruta, asignacion.getId(), ventana, null, ahora),
                        candidato -> salidas.buscarPorId(candidato.getId()).isPresent());
        salidas.guardar(salidaDominio);
        return salidaDominio;
    }

    public void iniciarSalida(SalidaDominio salida, LocalDateTime ahora) {
        var asignacionDeSalida = asignaciones.buscarPorId(salida.getAsignacionId()).orElseThrow();
        var recursos = new HashSet<UUID>();
        recursos.add(asignacionDeSalida.getId());
        asignaciones
                .buscarPorConductor(asignacionDeSalida.getConductorId())
                .forEach(otra -> recursos.add(otra.getId()));
        asignaciones
                .buscarPorVehiculo(asignacionDeSalida.getVehiculoId())
                .forEach(otra -> recursos.add(otra.getId()));
        estado(
                salidas
                        .relacionados(
                                FiltroSalida.todos()
                                        .conAsignacionIds(recursos)
                                        .conEstadoSalida(EstadoSalida.EN_CURSO))
                        .stream()
                        .noneMatch(otra -> !otra.getId().equals(salida.getId())),
                CatalogoMensajes.EL_CONDUCTOR_O_EL_VEHICULO_YA_TIENEN);
        validarVentana(asignacionDeSalida, salida.getVentana(), salida.getId(), ahora);
        salida.iniciar(ahora);
        salidas.guardar(salida);
    }

    public void reemplazarAsignacion(
            SalidaDominio salida,
            AsignacionDominio nuevaAsignacion,
            String motivo,
            Responsable responsable,
            LocalDateTime nuevaLlegadaEstimada,
            LocalDateTime ahora) {
        estado(
                !salida.getEstadoSalida().esDefinitivo(),
                CatalogoMensajes.LA_SALIDA_YA_TIENE_ESTADO_DEFINITIVO);
        estado(
                salidas
                        .relacionados(
                                FiltroSalida.todos()
                                        .conAsignacionId(nuevaAsignacion.getId())
                                        .conInstanteSalidaProgramado(
                                                salida
                                                        .getVentana()
                                                        .instanteSalidaProgramado()))
                        .stream()
                        .allMatch(otra -> otra.getId().equals(salida.getId())),
                CatalogoMensajes.YA_EXISTE_UNA_SALIDA_DE_LA_NUEVA);
        var anterior = asignaciones.buscarPorId(salida.getAsignacionId()).orElseThrow();
        var vAnterior = vehiculos.buscarPorId(anterior.getVehiculoId()).orElseThrow();
        var vNueva = vehiculos.buscarPorId(nuevaAsignacion.getVehiculoId()).orElseThrow();
        estado(
                vNueva.getCapacidadPasajeros() >= vAnterior.getCapacidadPasajeros(),
                CatalogoMensajes.LA_CAPACIDAD_DEL_NUEVO_VEHICULO_ES_INSUFICIENTE);
        var ventana =
                salida.getEstadoSalida() == EstadoSalida.PROGRAMADA
                        ? salida.getVentana()
                        : new VentanaViaje(ahora, requerido(nuevaLlegadaEstimada, "nuevaLlegadaEstimada"));
        validarVentana(nuevaAsignacion, ventana, salida.getId(), ahora);
        String detalle =
                "; conductor anterior="
                        + anterior.getConductorId()
                        + "; vehículo anterior="
                        + vAnterior.getPlaca().valor()
                        + "; nuevo conductor="
                        + nuevaAsignacion.getConductorId()
                        + "; vehículo nuevo="
                        + vNueva.getPlaca().valor();
        salida.reemplazarAsignacion(
                nuevaAsignacion.getId(), motivo, responsable, nuevaLlegadaEstimada, ahora, detalle);
        salidas.guardar(salida);
    }

    public void reprogramarSalida(
            SalidaDominio salida,
            VentanaViaje nuevaVentana,
            String motivo,
            Responsable responsable,
            LocalDateTime ahora) {
        var asignacionDeSalida =
                asignaciones.buscarPorId(salida.getAsignacionId()).orElseThrow();
        validarVentana(asignacionDeSalida, nuevaVentana, salida.getId(), ahora);
        if (salida.getProgramacionRutaId().isPresent()) {
            estado(
                    salidas
                            .relacionados(
                                    FiltroSalida.todos()
                                            .conProgramacionRutaId(
                                                    salida.getProgramacionRutaId().get())
                                            .conInstanteSalidaProgramado(
                                                    nuevaVentana.instanteSalidaProgramado()))
                            .stream()
                            .allMatch(otra -> otra.getId().equals(salida.getId())),
                    CatalogoMensajes.YA_EXISTE_OTRA_SALIDA_DE_ESA_PROGRAMACION);
        }
        salida.reprogramar(nuevaVentana, motivo, responsable, ahora, notificador);
        salidas.guardar(salida);
    }

    public SalidaDominio programarRefuerzo(
            SalidaDominio salidaReforzada,
            AsignacionDominio asignacion,
            LocalDateTime instanteSalida,
            String motivo,
            Responsable responsable,
            LocalDateTime ahora) {
        salidaReforzada.validarOperacion(ahora);
        requerido(responsable, "responsable");
        var duracion =
                Duration.between(
                        salidaReforzada.getVentana().instanteSalidaProgramado(),
                        salidaReforzada.getVentana().instanteLlegadaEstimado());
        var ruta = rutas.buscarPorId(salidaReforzada.getRutaId()).orElseThrow();
        var refuerzo =
                programarSalida(
                        ruta, asignacion, new VentanaViaje(
                                instanteSalida, instanteSalida.plus(duracion)), ahora);
        salidaReforzada.registrarIncidencia(
                TipoIncidencia.REFUERZO,
                ahora,
                Incidencia.describirCambio(motivo, "; salida de refuerzo=" + refuerzo.getId()),
                responsable,
                ahora);
        salidas.guardar(salidaReforzada);
        return refuerzo;
    }

    public void reactivarProgramacion(
            ProgramacionRutaDominio programacion, AsignacionDominio asignacion, LocalDate hoy) {
        estado(!programacion.isActiva(), CatalogoMensajes.LA_PROGRAMACION_DEBE_ESTAR_INACTIVA);
        estado(
                programacion
                                .getVigencia()
                                .fin()
                                .filter(fechaRecibido -> fechaRecibido.isBefore(hoy))
                                .isEmpty()
                        && asignacion.estaVigenteDurante(programacion.getVigencia()),
                CatalogoMensajes.LA_VIGENCIA_TERMINO_O_LA_ASIGNACION_NO);
        var candidata =
                new ProgramacionRutaDominio(
                        programacion.getId(),
                        programacion.getRutaId(),
                        asignacion.getId(),
                        programacion.getHorario(),
                        programacion.getVigencia(),
                        true);
        cruces(candidata);
        programacion.reactivar();
        programaciones.guardar(programacion);
    }
}
