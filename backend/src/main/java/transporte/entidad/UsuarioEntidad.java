package transporte.entidad;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record UsuarioEntidad(
        UUID id,
        String primerNombre,
        @Nullable String segundoNombre,
        String primerApellido,
        @Nullable String segundoApellido,
        String cedula,
        String correo,
        @Nullable String numeroTelefono,
        String hashContrasena,
        UUID rolId,
        @Nullable UUID conductorId,
        Boolean activo,
        Boolean debeCambiarContrasena) {
    @Override
    public String toString() {
        return "UsuarioEntidad[id=" + id + ", credencial protegida]";
    }
}
