package transporte.negocio.negocio;

import transporte.dominio.ProgramacionRutaDominio;
import transporte.dominio.valor.ResultadoGeneracion;
import transporte.negocio.resultado.CambioProgramacion;
import transporte.negocio.solicitud.Solicitudes.GenerarSalidas;
import transporte.negocio.solicitud.Solicitudes.ModificarProgramacion;
import transporte.negocio.solicitud.Solicitudes.RegistrarProgramacion;

import java.util.UUID;

public interface ProgramacionRutaNegocio {
    public ProgramacionRutaDominio registrar(RegistrarProgramacion registrarProgramacion);

    public CambioProgramacion modificar(
            UUID id, ModificarProgramacion modificarProgramacion, UUID actor);

    public ProgramacionRutaDominio estadoProgramacion(UUID id, boolean activa);

    public ResultadoGeneracion generar(UUID id, GenerarSalidas generarSalidas);

    public ProgramacionRutaDominio consultar(UUID id);
}
