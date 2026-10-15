package transporte.negocio.fachada;

import transporte.dto.Comandos.Motivo;
import transporte.dto.Comandos.ProgramarSalida;
import transporte.dto.Comandos.Reemplazar;
import transporte.dto.Comandos.Refuerzo;
import transporte.dto.Comandos.RegistrarIncidencia;
import transporte.dto.Comandos.Reprogramar;
import transporte.dto.IncidenciaDTO;
import transporte.dto.SalidaDTO;

import java.util.List;
import java.util.UUID;

public interface SalidaFachada {
    public SalidaDTO programar(ProgramarSalida programarSalida);

    public SalidaDTO iniciar(UUID id);

    public SalidaDTO finalizarSalida(UUID id);

    public SalidaDTO cancelar(UUID id, Motivo motivo, UUID actor);

    public SalidaDTO reemplazar(UUID id, Reemplazar reemplazar, UUID actor);

    public SalidaDTO reprogramar(UUID id, Reprogramar reprogramar, UUID actor);

    public SalidaDTO refuerzo(UUID id, Refuerzo refuerzo, UUID actor);

    public IncidenciaDTO incidencia(UUID id, RegistrarIncidencia registrarIncidencia, UUID actor);

    public List<IncidenciaDTO> historial(UUID id);
}
