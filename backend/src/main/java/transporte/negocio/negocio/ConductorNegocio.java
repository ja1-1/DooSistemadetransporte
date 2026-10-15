package transporte.negocio.negocio;

import transporte.dominio.ConductorDominio;
import transporte.negocio.solicitud.Solicitudes.ModificarConductor;
import transporte.negocio.solicitud.Solicitudes.RegistrarConductor;

import java.util.UUID;

public interface ConductorNegocio {
    public ConductorDominio conductor(UUID id);

    public ConductorDominio registrar(RegistrarConductor registrarConductor);

    public ConductorDominio modificar(UUID id, ModificarConductor modificarConductor);

    public ConductorDominio estadoConductor(UUID id, boolean activo);

    public void eliminarConductor(UUID id);
}
