package transporte.negocio.fachada;

import transporte.dto.AltaUsuarioDTO;
import transporte.dto.Comandos.CambiarContrasena;
import transporte.dto.Comandos.ModificarPerfil;
import transporte.dto.Comandos.ModificarUsuario;
import transporte.dto.Comandos.RegistrarUsuario;
import transporte.dto.UsuarioDTO;

import java.util.UUID;

public interface UsuarioFachada {
    public AltaUsuarioDTO registrar(RegistrarUsuario registrarUsuario);

    public UsuarioDTO modificar(UUID id, ModificarUsuario modificarUsuario);

    public UsuarioDTO perfil(UUID id, ModificarPerfil modificarPerfil);

    public UsuarioDTO contrasena(UUID id, CambiarContrasena cambiarContrasena);

    public AltaUsuarioDTO restablecer(UUID id);

    public UsuarioDTO estado(UUID id, boolean activo);

    public UsuarioDTO propia(UUID id);
}
