package transporte.infraestructura.persistencia.assembler.impl;

import transporte.dominio.UsuarioDominio;
import transporte.dominio.enums.Rol;
import transporte.dominio.valor.Cedula;
import transporte.dominio.valor.Correo;
import transporte.dominio.valor.HashContrasena;
import transporte.dominio.valor.Telefono;
import transporte.entidad.UsuarioEntidad;
import transporte.infraestructura.persistencia.assembler.EntidadAssembler;

import org.springframework.stereotype.Component;

@Component
public class UsuarioEntidadAssembler implements EntidadAssembler<UsuarioDominio, UsuarioEntidad> {
    @Override
    public UsuarioDominio convertirADominio(UsuarioEntidad usuarioEntidad) {
        return new UsuarioDominio(
                usuarioEntidad.id(),
                usuarioEntidad.primerNombre(),
                usuarioEntidad.segundoNombre(),
                usuarioEntidad.primerApellido(),
                usuarioEntidad.segundoApellido(),
                new Cedula(usuarioEntidad.cedula()),
                new Correo(usuarioEntidad.correo()),
                usuarioEntidad.numeroTelefono() == null
                        ? null
                        : new Telefono(usuarioEntidad.numeroTelefono()),
                new HashContrasena(usuarioEntidad.hashContrasena()),
                usuarioEntidad.debeCambiarContrasena(),
                Rol.desdeId(usuarioEntidad.rolId()),
                usuarioEntidad.conductorId(),
                usuarioEntidad.activo());
    }

    @Override
    public UsuarioEntidad convertirAEntidad(UsuarioDominio usuario) {
        return new UsuarioEntidad(
                usuario.getId(),
                usuario.getPrimerNombre(),
                usuario.getSegundoNombre().orElse(null),
                usuario.getPrimerApellido(),
                usuario.getSegundoApellido().orElse(null),
                usuario.getCedula().valor(),
                usuario.getCorreo().valor(),
                usuario.getNumeroTelefono().map(Telefono::valor).orElse(null),
                usuario.getHashContrasena().valor(),
                usuario.getRol().getId(),
                usuario.getConductorId().orElse(null),
                usuario.isActivo(),
                usuario.isDebeCambiarContrasena());
    }
}
