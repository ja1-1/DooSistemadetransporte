package transporte.infraestructura.seguridad;

import transporte.dominio.enums.Rol;
import transporte.dominio.valor.HashContrasena;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.Principal;
import java.util.UUID;

public record UsuarioAutenticado(
        UUID id,
        String correo,
        Rol rol,
        boolean debeCambiarContrasena,
        @JsonIgnore HashContrasena credencial)
        implements Principal {
    @Override
    public String getName() {
        return correo;
    }
}
