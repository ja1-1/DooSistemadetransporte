package transporte.negocio.fachada;

import transporte.dto.Comandos.ModificarVehiculo;
import transporte.dto.Comandos.RegistrarVehiculo;
import transporte.dto.VehiculoDTO;

import java.util.UUID;

public interface VehiculoFachada {
    public VehiculoDTO registrar(RegistrarVehiculo registrarVehiculo);

    public VehiculoDTO modificarVehiculo(UUID id, ModificarVehiculo modificarVehiculo);

    public VehiculoDTO estadoVehiculo(UUID id, boolean activo);

    public void eliminarVehiculo(UUID id);
}
