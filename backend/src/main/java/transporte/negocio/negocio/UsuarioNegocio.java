package transporte.negocio.negocio;

import transporte.dominio.UsuarioDominio;
import transporte.negocio.resultado.AltaUsuario;
import transporte.negocio.solicitud.Solicitudes.CambiarContrasena;
import transporte.negocio.solicitud.Solicitudes.ModificarPerfil;
import transporte.negocio.solicitud.Solicitudes.ModificarUsuario;
import transporte.negocio.solicitud.Solicitudes.RegistrarUsuario;

import java.util.UUID;

public interface UsuarioNegocio {
    public UsuarioDominio consultar(UUID id);

    public AltaUsuario registrar(RegistrarUsuario registrarUsuario);

    public UsuarioDominio modificar(UUID id, ModificarUsuario modificarUsuario);

    public UsuarioDominio perfil(UUID id, ModificarPerfil modificarPerfil);

    public UsuarioDominio cambiarContrasena(UUID id, CambiarContrasena cambiarContrasena);

    public AltaUsuario restablecer(UUID id);

    public UsuarioDominio estado(UUID id, boolean activo);
}
