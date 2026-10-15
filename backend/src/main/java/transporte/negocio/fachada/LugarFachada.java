package transporte.negocio.fachada;

import transporte.dto.geografia.LugarDTO;
import transporte.dto.geografia.ModificarLugarDTO;
import transporte.dto.geografia.RegistrarLugarDTO;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface LugarFachada {
    public LugarDTO registrarLugar(RegistrarLugarDTO registrarLugar);

    public LugarDTO modificarLugar(UUID id, ModificarLugarDTO modificarLugar);

    public LugarDTO estadoLugar(UUID id, boolean activo);

    public void eliminarLugar(UUID id);

    public LugarDTO consultarLugar(UUID id);

    public List<LugarDTO> buscarLugar(
            @Nullable String nombre,
            @Nullable UUID sectorId,
            @Nullable Boolean activo,
            int numero,
            int tamano);
}
