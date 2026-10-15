package transporte.negocio.negocio;

import transporte.dominio.LugarDominio;
import transporte.dominio.valor.Pagina;
import transporte.negocio.solicitud.Solicitudes.ModificarLugar;
import transporte.negocio.solicitud.Solicitudes.RegistrarLugar;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface LugarNegocio {
    public LugarDominio consultarLugar(UUID id);

    public LugarDominio registrarLugar(RegistrarLugar registrarLugar);

    public LugarDominio modificarLugar(UUID id, ModificarLugar modificarLugar);

    public LugarDominio estadoLugar(UUID id, boolean activo);

    public void eliminarLugar(UUID id);

    public List<LugarDominio> buscarLugar(
            @Nullable String nombre,
            @Nullable UUID sectorId,
            @Nullable Boolean activo,
            Pagina pagina);
}
