package transporte.negocio.fachada;

import transporte.dto.geografia.ModificarMunicipioDTO;
import transporte.dto.geografia.MunicipioDTO;
import transporte.dto.geografia.RegistrarMunicipioDTO;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface MunicipioFachada {
    public MunicipioDTO registrarMunicipio(RegistrarMunicipioDTO registrarMunicipio);

    public MunicipioDTO modificarMunicipio(UUID id, ModificarMunicipioDTO modificarMunicipio);

    public MunicipioDTO estadoMunicipio(UUID id, boolean activo);

    public void eliminarMunicipio(UUID id);

    public MunicipioDTO consultarMunicipio(UUID id);

    public List<MunicipioDTO> buscarMunicipio(
            @Nullable String nombre,
            @Nullable String departamento,
            @Nullable Boolean activo,
            int numero,
            int tamano);
}
