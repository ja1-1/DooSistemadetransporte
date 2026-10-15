package transporte.negocio.negocio.impl;

import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.NO_ENCONTRADO;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.ProgramacionRutaDominio;
import transporte.dominio.repositorio.ProgramacionRutaRepositorio;
import transporte.dominio.servicio.ServicioProgramacion;
import transporte.dominio.valor.HorarioProgramado;
import transporte.dominio.valor.Periodo;
import transporte.dominio.valor.Responsable;
import transporte.dominio.valor.ResultadoGeneracion;
import transporte.negocio.negocio.AsignacionNegocio;
import transporte.negocio.negocio.ProgramacionRutaNegocio;
import transporte.negocio.negocio.RutaNegocio;
import transporte.negocio.negocio.UsuarioNegocio;
import transporte.negocio.resultado.CambioProgramacion;
import transporte.negocio.solicitud.Solicitudes.GenerarSalidas;
import transporte.negocio.solicitud.Solicitudes.ModificarProgramacion;
import transporte.negocio.solicitud.Solicitudes.RegistrarProgramacion;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ProgramacionRutaNegocioImpl implements ProgramacionRutaNegocio {
    private final ProgramacionRutaRepositorio programaciones;
    private final ServicioProgramacion servicioProgramacion;
    private final RutaNegocio rutas;
    private final UsuarioNegocio usuarios;
    private final Clock reloj;
    private final AsignacionNegocio asignacionesNegocio;

    public ProgramacionRutaNegocioImpl(
            ProgramacionRutaRepositorio programaciones,
            ServicioProgramacion servicioProgramacion,
            RutaNegocio rutas,
            UsuarioNegocio usuarios,
            Clock reloj,
            AsignacionNegocio asignacionesNegocio) {
        this.programaciones = programaciones;
        this.servicioProgramacion = servicioProgramacion;
        this.rutas = rutas;
        this.usuarios = usuarios;
        this.reloj = reloj;
        this.asignacionesNegocio = asignacionesNegocio;
    }

    @Override
    public ProgramacionRutaDominio registrar(RegistrarProgramacion registrarProgramacion) {
        requerido(registrarProgramacion, "datos");
        return servicioProgramacion.registrarProgramacion(
                rutas.consultar(registrarProgramacion.rutaId()),
                asignacionesNegocio.consultar(registrarProgramacion.asignacionId()),
                horario(
                        registrarProgramacion.horaSalida(),
                        registrarProgramacion.horaUltimaSalida(),
                        registrarProgramacion.intervaloMinutos(),
                        registrarProgramacion.duracionEstimada(),
                        registrarProgramacion.diasSemana()),
                new Periodo(
                        registrarProgramacion.vigenciaDesde(),
                        Optional.ofNullable(registrarProgramacion.vigenciaHasta())),
                hoy());
    }

    @Override
    public CambioProgramacion modificar(
            UUID id, ModificarProgramacion modificarProgramacion, UUID actor) {
        requerido(modificarProgramacion, "datos");
        var programacion = consultar(id);
        var rechazadas =
                servicioProgramacion.modificarProgramacion(
                        programacion,
                        asignacionesNegocio.consultar(modificarProgramacion.asignacionId()),
                        horario(
                                modificarProgramacion.horaSalida(),
                                modificarProgramacion.horaUltimaSalida(),
                                modificarProgramacion.intervaloMinutos(),
                                modificarProgramacion.duracionEstimada(),
                                modificarProgramacion.diasSemana()),
                        modificarProgramacion.reprogramarSalidasGeneradas(),
                        responsable(actor),
                        ahora());
        return new CambioProgramacion(programacion, rechazadas);
    }

    @Override
    public ProgramacionRutaDominio estadoProgramacion(UUID id, boolean activa) {
        var programacion = consultar(id);
        if (activa) {
            servicioProgramacion.reactivarProgramacion(
                    programacion,
                    asignacionesNegocio.consultar(programacion.getAsignacionId()),
                    hoy());
        } else {
            programacion.desactivar();
            programaciones.guardar(programacion);
        }
        return programacion;
    }

    @Override
    public ResultadoGeneracion generar(UUID id, GenerarSalidas generarSalidas) {
        requerido(generarSalidas, "datos");
        return servicioProgramacion.generarSalidas(
                consultar(id), generarSalidas.fechaInicial(), generarSalidas.fechaFinal(), hoy());
    }

    @Override
    public ProgramacionRutaDominio consultar(UUID id) {
        return programaciones
                .buscarPorId(requerido(id, "programacionId"))
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO,
                                        CatalogoMensajes.NO_SE_ENCONTRO_LA_PROGRAMACION));
    }

    private Responsable responsable(UUID actor) {
        return Responsable.desde(usuarios.consultar(actor));
    }

    private LocalDate hoy() {
        return LocalDate.now(reloj);
    }

    private LocalDateTime ahora() {
        return LocalDateTime.now(reloj);
    }

    private HorarioProgramado horario(
            LocalTime inicio,
            LocalTime fin,
            Integer intervalo,
            Integer duracion,
            Set<DayOfWeek> dias) {
        return new HorarioProgramado(
                inicio,
                fin,
                requerido(intervalo, "intervaloMinutos"),
                requerido(duracion, "duracionEstimada"),
                dias);
    }
}
