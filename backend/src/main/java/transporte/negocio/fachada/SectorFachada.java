package transporte.negocio.fachada;

import transporte.dto.geografia.ModificarSectorDTO;
import transporte.dto.geografia.RegistrarSectorDTO;
import transporte.dto.geografia.SectorDTO;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface SectorFachada {
    public SectorDTO registrarSector(RegistrarSectorDTO registrarSector);

    public SectorDTO modificarSector(UUID id, ModificarSectorDTO modificarSector);

    public SectorDTO estadoSector(UUID id, boolean activo);

    public void eliminarSector(UUID id);

    public SectorDTO consultarSector(UUID id);

    public List<SectorDTO> buscarSector(
            @Nullable String nombre,
            @Nullable UUID municipioId,
            @Nullable Boolean activo,
            int numero,
            int tamano);
}
