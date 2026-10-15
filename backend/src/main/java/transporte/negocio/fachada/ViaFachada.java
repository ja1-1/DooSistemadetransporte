package transporte.negocio.fachada;

import transporte.dto.geografia.ModificarViaDTO;
import transporte.dto.geografia.RegistrarViaDTO;
import transporte.dto.geografia.ViaDTO;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface ViaFachada {
    public ViaDTO registrarVia(RegistrarViaDTO registrarVia);

    public ViaDTO modificarVia(UUID id, ModificarViaDTO modificarVia);

    public ViaDTO estadoVia(UUID id, boolean activo);

    public void eliminarVia(UUID id);

    public ViaDTO consultarVia(UUID id);

    public List<ViaDTO> buscarVia(
            @Nullable String nombre,
            @Nullable String descripcion,
            @Nullable Boolean activo,
            int numero,
            int tamano);
}
