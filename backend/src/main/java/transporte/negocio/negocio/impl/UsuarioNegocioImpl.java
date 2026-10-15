package transporte.negocio.negocio.impl;

import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.NO_ENCONTRADO;
import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.ConductorDominio;
import transporte.dominio.UsuarioDominio;
import transporte.dominio.enums.Rol;
import transporte.dominio.puerto.CifradorContrasena;
import transporte.dominio.repositorio.ConductorRepositorio;
import transporte.dominio.repositorio.UsuarioRepositorio;
import transporte.dominio.valor.Cedula;
import transporte.dominio.valor.Correo;
import transporte.dominio.valor.Telefono;
import transporte.negocio.negocio.UsuarioNegocio;
import transporte.negocio.resultado.AltaUsuario;
import transporte.negocio.solicitud.Solicitudes.CambiarContrasena;
import transporte.negocio.solicitud.Solicitudes.ModificarPerfil;
import transporte.negocio.solicitud.Solicitudes.ModificarUsuario;
import transporte.negocio.solicitud.Solicitudes.RegistrarUsuario;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.IdentidadDisponible;
import transporte.transversal.utilitarios.Validacion;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioNegocioImpl implements UsuarioNegocio {
    private final UsuarioRepositorio usuarios;
    private final ConductorRepositorio conductores;
    private final CifradorContrasena cifrador;

    public UsuarioNegocioImpl(
            UsuarioRepositorio repositorioUsuario,
            ConductorRepositorio repositorioConductor,
            CifradorContrasena cifrador) {
        usuarios = repositorioUsuario;
        conductores = repositorioConductor;
        this.cifrador = cifrador;
    }

    @Override
    public UsuarioDominio consultar(UUID id) {
        return usuarios.buscarPorId(requerido(id, "id"))
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO, CatalogoMensajes.NO_SE_ENCONTRO_EL_USUARIO));
    }

    private Optional<ConductorDominio> conductor(@Nullable UUID id) {
        return id == null
                ? Optional.empty()
                : Optional.of(
                        conductores
                                .buscarPorId(id)
                                .orElseThrow(
                                        () ->
                                                new NegocioExcepcion(
                                                        NO_ENCONTRADO,
                                                        CatalogoMensajes
                                                                .NO_SE_ENCONTRO_EL_CONDUCTOR)));
    }

    @Override
    public AltaUsuario registrar(RegistrarUsuario registrarUsuario) {
        requerido(registrarUsuario, "datos");
        var cedula = new Cedula(registrarUsuario.cedula());
        var correo = new Correo(registrarUsuario.correo());
        Validacion.estado(!usuarios.existePorCedula(cedula), "La cédula ya está registrada.");
        Validacion.estado(!usuarios.existePorCorreo(correo), "El correo ya está registrado.");
        String temporal = cifrador.generarContrasenaTemporal();
        UsuarioDominio usuario =
                IdentidadDisponible.crear(
                        () ->
                                UsuarioDominio.registrar(
                                        registrarUsuario.primerNombre(),
                                        Optional.ofNullable(registrarUsuario.segundoNombre()),
                                        registrarUsuario.primerApellido(),
                                        Optional.ofNullable(registrarUsuario.segundoApellido()),
                                        cedula,
                                        correo,
                                        Optional.ofNullable(registrarUsuario.numeroTelefono())
                                                .map(Telefono::new),
                                        Rol.desdeId(registrarUsuario.rolId()),
                                        conductor(registrarUsuario.conductorId()),
                                        temporal,
                                        cifrador),
                        candidato -> usuarios.buscarPorId(candidato.getId()).isPresent());
        usuarios.guardar(usuario);
        return new AltaUsuario(usuario, temporal);
    }

    @Override
    public UsuarioDominio modificar(UUID id, ModificarUsuario modificarUsuario) {
        requerido(modificarUsuario, "datos");
        var usuario = consultar(id);
        usuario.modificar(
                Rol.desdeId(modificarUsuario.rolId()), conductor(modificarUsuario.conductorId()));
        usuarios.guardar(usuario);
        return usuario;
    }

    @Override
    public UsuarioDominio perfil(UUID id, ModificarPerfil modificarPerfil) {
        requerido(modificarPerfil, "datos");
        var usuario = consultar(id);
        usuario.modificarInformacionPropia(
                modificarPerfil.primerNombre(),
                Optional.ofNullable(modificarPerfil.segundoNombre()),
                modificarPerfil.primerApellido(),
                Optional.ofNullable(modificarPerfil.segundoApellido()),
                Optional.ofNullable(modificarPerfil.numeroTelefono()).map(Telefono::new));
        usuarios.guardar(usuario);
        return usuario;
    }

    @Override
    public UsuarioDominio cambiarContrasena(UUID id, CambiarContrasena cambiarContrasena) {
        requerido(cambiarContrasena, "datos");
        var usuario = consultar(id);
        usuario.cambiarContrasena(
                cambiarContrasena.contrasenaActual(),
                cambiarContrasena.contrasenaNueva(),
                cifrador);
        usuarios.guardar(usuario);
        return usuario;
    }

    @Override
    public AltaUsuario restablecer(UUID id) {
        var usuario = consultar(id);
        String contrasenaTemporal = cifrador.generarContrasenaTemporal();
        usuario.restablecerContrasena(contrasenaTemporal, cifrador);
        usuarios.guardar(usuario);
        return new AltaUsuario(usuario, contrasenaTemporal);
    }

    @Override
    public UsuarioDominio estado(UUID id, boolean activo) {
        var usuario = consultar(id);
        if (activo) {
            usuario.reactivar();
        } else {
            usuario.desactivar();
        }
        usuarios.guardar(usuario);
        return usuario;
    }
}
