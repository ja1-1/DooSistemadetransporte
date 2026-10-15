package transporte.negocio.fachada.impl;

import static transporte.negocio.fachada.assembler.impl.SolicitudAssembler.convertir;
import static transporte.negocio.fachada.assembler.impl.VistaDTOAssembler.convertirADTO;

import transporte.dto.AltaUsuarioDTO;
import transporte.dto.Comandos.CambiarContrasena;
import transporte.dto.Comandos.ModificarPerfil;
import transporte.dto.Comandos.ModificarUsuario;
import transporte.dto.Comandos.RegistrarUsuario;
import transporte.dto.UsuarioDTO;
import transporte.negocio.fachada.Escritura;
import transporte.negocio.fachada.UsuarioFachada;
import transporte.negocio.negocio.UsuarioNegocio;
import transporte.negocio.resultado.AltaUsuario;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UsuarioFachadaImpl implements UsuarioFachada {
    private final UsuarioNegocio negocio;

    public UsuarioFachadaImpl(UsuarioNegocio usuarioNegocio) {
        negocio = usuarioNegocio;
    }

    private AltaUsuarioDTO alta(AltaUsuario alta) {
        return new AltaUsuarioDTO(convertirADTO(alta.usuario()), alta.contrasenaTemporal());
    }

    @Override
    @Escritura
    public AltaUsuarioDTO registrar(RegistrarUsuario registrarUsuario) {
        return alta(negocio.registrar(convertir(registrarUsuario)));
    }

    @Override
    @Escritura
    public UsuarioDTO modificar(UUID id, ModificarUsuario modificarUsuario) {
        return convertirADTO(negocio.modificar(id, convertir(modificarUsuario)));
    }

    @Override
    @Escritura
    public UsuarioDTO perfil(UUID id, ModificarPerfil modificarPerfil) {
        return convertirADTO(negocio.perfil(id, convertir(modificarPerfil)));
    }

    @Override
    @Escritura
    public UsuarioDTO contrasena(UUID id, CambiarContrasena cambiarContrasena) {
        return convertirADTO(negocio.cambiarContrasena(id, convertir(cambiarContrasena)));
    }

    @Override
    @Escritura
    public AltaUsuarioDTO restablecer(UUID id) {
        return alta(negocio.restablecer(id));
    }

    @Override
    @Escritura
    public UsuarioDTO estado(UUID id, boolean activo) {
        return convertirADTO(negocio.estado(id, activo));
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public UsuarioDTO propia(UUID id) {
        return convertirADTO(negocio.consultar(id));
    }
}
