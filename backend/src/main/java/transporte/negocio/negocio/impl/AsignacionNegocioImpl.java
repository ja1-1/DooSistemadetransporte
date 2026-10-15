package transporte.negocio.negocio.impl;

import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.NO_ENCONTRADO;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.AsignacionDominio;
import transporte.dominio.repositorio.AsignacionRepositorio;
import transporte.dominio.repositorio.CategoriaVehiculoRepositorio;
import transporte.dominio.servicio.ServicioAsignacion;
import transporte.dominio.valor.Responsable;
import transporte.dominio.valor.VentanaViaje;
import transporte.negocio.negocio.AsignacionNegocio;
import transporte.negocio.negocio.ConductorNegocio;
import transporte.negocio.negocio.UsuarioNegocio;
import transporte.negocio.negocio.VehiculoNegocio;
import transporte.negocio.solicitud.Solicitudes.Asignar;
import transporte.negocio.solicitud.Solicitudes.FinalizarAsignacion;
import transporte.negocio.solicitud.Solicitudes.Relevar;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AsignacionNegocioImpl implements AsignacionNegocio {
    private final AsignacionRepositorio asignaciones;
    private final CategoriaVehiculoRepositorio categorias;
    private final ServicioAsignacion servicioAsignacion;
    private final UsuarioNegocio usuarios;
    private final Clock reloj;
    private final ConductorNegocio conductoresNegocio;
    private final VehiculoNegocio vehiculosNegocio;

    public AsignacionNegocioImpl(
            AsignacionRepositorio asignaciones,
            CategoriaVehiculoRepositorio categorias,
            ServicioAsignacion servicioAsignacion,
            UsuarioNegocio usuarios,
            Clock reloj,
            ConductorNegocio conductoresNegocio,
            VehiculoNegocio vehiculosNegocio) {
        this.asignaciones = asignaciones;
        this.categorias = categorias;
        this.servicioAsignacion = servicioAsignacion;
        this.usuarios = usuarios;
        this.reloj = reloj;
        this.conductoresNegocio = conductoresNegocio;
        this.vehiculosNegocio = vehiculosNegocio;
    }

    @Override
    public AsignacionDominio asignar(Asignar asignar) {
        requerido(asignar, "datos");
        return servicioAsignacion.asignar(
                conductoresNegocio.conductor(asignar.conductorId()),
                vehiculosNegocio.vehiculo(asignar.vehiculoId()),
                asignar.fechaInicio());
    }

    @Override
    public AsignacionDominio finalizar(UUID id, FinalizarAsignacion finalizarAsignacion) {
        requerido(finalizarAsignacion, "datos");
        var asignacion = consultar(id);
        servicioAsignacion.finalizar(asignacion, finalizarAsignacion.fechaFin());
        return asignacion;
    }

    @Override
    public AsignacionDominio relevar(UUID id, Relevar relevar, UUID actor) {
        requerido(relevar, "datos");
        var asignacion = consultar(id);
        var vehiculoAsignado = vehiculosNegocio.vehiculo(asignacion.getVehiculoId());
        return servicioAsignacion.relevarConductor(
                asignacion,
                conductoresNegocio.conductor(relevar.nuevoConductorId()),
                categorias.buscarPorId(vehiculoAsignado.getCategoriaVehiculoId()).orElseThrow(),
                relevar.fechaRelevo(),
                relevar.motivo(),
                responsable(actor),
                hoy());
    }

    @Override
    public List<AsignacionDominio> disponibles(
            LocalDateTime inicio, LocalDateTime fin, int capacidad) {
        return servicioAsignacion.consultarDisponibles(
                new VentanaViaje(inicio, fin), capacidad, ahora());
    }

    @Override
    public AsignacionDominio consultar(UUID id) {
        return asignaciones
                .buscarPorId(requerido(id, "asignacionId"))
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO,
                                        CatalogoMensajes.NO_SE_ENCONTRO_LA_ASIGNACION));
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
}
