package transporte.negocio.fachada;

import transporte.dto.CambioProgramacionDTO;
import transporte.dto.Comandos.GenerarSalidas;
import transporte.dto.Comandos.ModificarProgramacion;
import transporte.dto.Comandos.RegistrarProgramacion;
import transporte.dto.GeneracionSalidasDTO;
import transporte.dto.ProgramacionRutaDTO;

import java.util.UUID;

public interface ProgramacionRutaFachada {
    public ProgramacionRutaDTO registrarProgramacion(RegistrarProgramacion registrarProgramacion);

    public ProgramacionRutaDTO estadoProgramacion(UUID id, boolean activa);

    public GeneracionSalidasDTO generar(UUID id, GenerarSalidas generarSalidas);

    public CambioProgramacionDTO modificarProgramacion(
            UUID id, ModificarProgramacion modificarProgramacion, UUID actor);
}
