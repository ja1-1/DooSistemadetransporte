package transporte.negocio.fachada;

import transporte.dto.Comandos.CambiarRecorrido;
import transporte.dto.Comandos.ModificarRuta;
import transporte.dto.Comandos.RegistrarRuta;
import transporte.dto.RutaDTO;

import java.util.UUID;

public interface RutaFachada {
    public RutaDTO registrar(RegistrarRuta registrarRuta);

    public RutaDTO modificar(UUID id, ModificarRuta modificarRuta);

    public RutaDTO recorrido(UUID id, CambiarRecorrido cambiarRecorrido);

    public RutaDTO estado(UUID id, boolean activo);

    public void eliminar(UUID id);
}
