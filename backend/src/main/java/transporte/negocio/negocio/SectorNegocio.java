package transporte.negocio.negocio;

import transporte.dominio.SectorDominio;
import transporte.dominio.valor.Pagina;
import transporte.negocio.solicitud.Solicitudes.ModificarSector;
import transporte.negocio.solicitud.Solicitudes.RegistrarSector;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface SectorNegocio {
    public SectorDominio consultarSector(UUID id);

    public SectorDominio registrarSector(RegistrarSector registrarSector);

    public SectorDominio modificarSector(UUID id, ModificarSector modificarSector);

    public SectorDominio estadoSector(UUID id, boolean activo);

    public void eliminarSector(UUID id);

    public List<SectorDominio> buscarSector(
            @Nullable String nombre,
            @Nullable UUID municipioId,
            @Nullable Boolean activo,
            Pagina pagina);
}
