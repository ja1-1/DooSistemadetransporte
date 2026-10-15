package transporte.negocio.negocio;

import transporte.dominio.VehiculoDominio;
import transporte.negocio.solicitud.Solicitudes.ModificarVehiculo;
import transporte.negocio.solicitud.Solicitudes.RegistrarVehiculo;

import java.util.UUID;

public interface VehiculoNegocio {
    public VehiculoDominio vehiculo(UUID id);

    public VehiculoDominio registrar(RegistrarVehiculo registrarVehiculo);

    public VehiculoDominio modificar(UUID id, ModificarVehiculo modificarVehiculo);

    public VehiculoDominio estadoVehiculo(UUID id, boolean activo);

    public void eliminarVehiculo(UUID id);
}
