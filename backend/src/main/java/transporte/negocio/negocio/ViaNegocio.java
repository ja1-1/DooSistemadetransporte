package transporte.negocio.negocio;

import transporte.dominio.ViaDominio;
import transporte.dominio.valor.Pagina;
import transporte.negocio.solicitud.Solicitudes.ModificarVia;
import transporte.negocio.solicitud.Solicitudes.RegistrarVia;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface ViaNegocio {
    public ViaDominio consultarVia(UUID id);

    public ViaDominio registrarVia(RegistrarVia registrarVia);

    public ViaDominio modificarVia(UUID id, ModificarVia modificarVia);

    public ViaDominio estadoVia(UUID id, boolean activo);

    public void eliminarVia(UUID id);

    public List<ViaDominio> buscarVia(
            @Nullable String nombre,
            @Nullable String descripcion,
            @Nullable Boolean activo,
            Pagina pagina);
}
