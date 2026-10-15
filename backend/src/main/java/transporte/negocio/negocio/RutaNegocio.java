package transporte.negocio.negocio;

import transporte.dominio.RutaDominio;
import transporte.negocio.solicitud.Solicitudes.CambiarRecorrido;
import transporte.negocio.solicitud.Solicitudes.ModificarRuta;
import transporte.negocio.solicitud.Solicitudes.RegistrarRuta;

import java.util.UUID;

public interface RutaNegocio {
    public RutaDominio consultar(UUID id);

    public RutaDominio registrar(RegistrarRuta registrarRuta);

    public RutaDominio modificar(UUID id, ModificarRuta modificarRuta);

    public RutaDominio recorrido(UUID id, CambiarRecorrido cambiarRecorrido);

    public RutaDominio estadoRuta(UUID id, boolean activa);

    public void eliminar(UUID id);
}
