package transporte.negocio.negocio;

import transporte.dominio.MunicipioDominio;
import transporte.dominio.valor.Pagina;
import transporte.negocio.solicitud.Solicitudes.ModificarMunicipio;
import transporte.negocio.solicitud.Solicitudes.RegistrarMunicipio;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface MunicipioNegocio {
    public MunicipioDominio consultarMunicipio(UUID id);

    public MunicipioDominio registrarMunicipio(RegistrarMunicipio registrarMunicipio);

    public MunicipioDominio modificarMunicipio(UUID id, ModificarMunicipio modificarMunicipio);

    public MunicipioDominio estadoMunicipio(UUID id, boolean activo);

    public void eliminarMunicipio(UUID id);

    public List<MunicipioDominio> buscarMunicipio(
            @Nullable String nombre,
            @Nullable String departamento,
            @Nullable Boolean activo,
            Pagina pagina);
}
