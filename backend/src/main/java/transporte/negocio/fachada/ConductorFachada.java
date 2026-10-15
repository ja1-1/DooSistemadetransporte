package transporte.negocio.fachada;

import transporte.dto.Comandos.ModificarConductor;
import transporte.dto.Comandos.RegistrarConductor;
import transporte.dto.ConductorDTO;

import java.util.UUID;

public interface ConductorFachada {
    public ConductorDTO registrar(RegistrarConductor registrarConductor);

    public ConductorDTO modificarConductor(UUID id, ModificarConductor modificarConductor);

    public ConductorDTO estadoConductor(UUID id, boolean activo);

    public void eliminarConductor(UUID id);
}
