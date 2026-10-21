package transporte.infraestructura.seguridad;

import transporte.dominio.UsuarioDominio;
import transporte.dominio.enums.Rol;
import transporte.dominio.puerto.CifradorContrasena;
import transporte.dominio.repositorio.UsuarioRepositorio;
import transporte.dominio.valor.Correo;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AutenticadorUsuarios implements AuthenticationProvider {
    private final UsuarioRepositorio usuarios;
    private final CifradorContrasena cifrador;
    private final Clock reloj;
    private final String aliasAdmin, aliasOperacion;

    public AutenticadorUsuarios(
            UsuarioRepositorio repositorioUsuario,
            CifradorContrasena cifradorContrasena,
            Clock reloj,
            @Value("${transporte.seguridad.admin.usuario}") String usuarioAdministrador,
            @Value("${transporte.seguridad.operacion.usuario}") String usuarioOperacion) {
        usuarios = repositorioUsuario;
        cifrador = cifradorContrasena;
        this.reloj = reloj;
        aliasAdmin = usuarioAdministrador;
        aliasOperacion = usuarioOperacion;
    }

    @Override
    public Authentication authenticate(Authentication autenticacion) {
        String correo = correoDe(autenticacion.getName());
        try {
            var usuario =
                    usuarios.buscarPorCorreo(new Correo(correo))
                            .orElseThrow(
                                    () ->
                                            new BadCredentialsException(
                                                    CatalogoMensajes
                                                            .LAS_CREDENCIALES_NO_SON_VALIDAS));
            if (!(autenticacion.getCredentials() instanceof String clave)) {
                throw new BadCredentialsException(CatalogoMensajes.LAS_CREDENCIALES_NO_SON_VALIDAS);
            }
            usuario.autenticar(clave, cifrador, LocalDateTime.now(reloj));
            var usuarioAutenticado =
                    new UsuarioAutenticado(
                            usuario.getId(),
                            usuario.getCorreo().valor(),
                            usuario.getRol(),
                            usuario.isDebeCambiarContrasena(),
                            usuario.getHashContrasena());
            return UsernamePasswordAuthenticationToken.authenticated(
                    usuarioAutenticado,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + autoridad(usuario))));
        } catch (IllegalArgumentException | IllegalStateException error) {
            throw new BadCredentialsException(CatalogoMensajes.LAS_CREDENCIALES_NO_SON_VALIDAS);
        }
    }

    private String correoDe(String nombre) {
        if (nombre.equals(aliasAdmin)) {
            return CuentasIniciales.CORREO_ADMINISTRADOR;
        }
        if (nombre.equals(aliasOperacion)) {
            return CuentasIniciales.CORREO_OPERACION;
        }
        return nombre;
    }

    private static String autoridad(UsuarioDominio usuario) {
        if (usuario.isDebeCambiarContrasena()) {
            return "CAMBIAR_CONTRASENA";
        }
        if (usuario.getRol() == Rol.JEFE_DE_OPERACION) {
            return "JEFE_OPERACION";
        }
        return usuario.getRol().name();
    }

    @Override
    public boolean supports(Class<?> tipo) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(tipo);
    }
}
