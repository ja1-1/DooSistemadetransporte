package transporte.negocio.negocio;

import transporte.dominio.AsignacionDominio;
import transporte.negocio.solicitud.Solicitudes.Asignar;
import transporte.negocio.solicitud.Solicitudes.FinalizarAsignacion;
import transporte.negocio.solicitud.Solicitudes.Relevar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AsignacionNegocio {
    public AsignacionDominio asignar(Asignar asignar);

    public AsignacionDominio finalizar(UUID id, FinalizarAsignacion finalizarAsignacion);

    public AsignacionDominio relevar(UUID id, Relevar relevar, UUID actor);

    public List<AsignacionDominio> disponibles(
            LocalDateTime inicio, LocalDateTime fin, int capacidad);

    public AsignacionDominio consultar(UUID id);
}
