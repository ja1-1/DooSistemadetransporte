package transporte.negocio.negocio;

import transporte.dominio.Incidencia;
import transporte.dominio.SalidaDominio;
import transporte.negocio.solicitud.Solicitudes.Motivo;
import transporte.negocio.solicitud.Solicitudes.ProgramarSalida;
import transporte.negocio.solicitud.Solicitudes.Reemplazar;
import transporte.negocio.solicitud.Solicitudes.Refuerzo;
import transporte.negocio.solicitud.Solicitudes.RegistrarIncidencia;
import transporte.negocio.solicitud.Solicitudes.Reprogramar;

import java.util.List;
import java.util.UUID;

public interface SalidaNegocio {
    public SalidaDominio programar(ProgramarSalida programarSalida);

    public SalidaDominio iniciar(UUID id);

    public SalidaDominio finalizarSalida(UUID id);

    public SalidaDominio cancelar(UUID id, Motivo motivo, UUID actor);

    public SalidaDominio reemplazar(UUID id, Reemplazar reemplazar, UUID actor);

    public SalidaDominio reprogramar(UUID id, Reprogramar reprogramar, UUID actor);

    public SalidaDominio refuerzo(UUID id, Refuerzo refuerzo, UUID actor);

    public Incidencia incidencia(UUID id, RegistrarIncidencia registrarIncidencia, UUID actor);

    public List<Incidencia> historial(UUID id);

    public SalidaDominio consultar(UUID id);
}
