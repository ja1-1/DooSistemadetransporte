package transporte.negocio.fachada;

import transporte.dto.AsignacionDTO;
import transporte.dto.Comandos.Asignar;
import transporte.dto.Comandos.FinalizarAsignacion;
import transporte.dto.Comandos.Relevar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AsignacionFachada {
    public AsignacionDTO asignar(Asignar asignar);

    public AsignacionDTO finalizarAsignacion(UUID id, FinalizarAsignacion finalizarAsignacion);

    public AsignacionDTO relevar(UUID id, Relevar relevar, UUID actor);

    public List<AsignacionDTO> disponibles(LocalDateTime inicio, LocalDateTime fin, int capacidad);
}
