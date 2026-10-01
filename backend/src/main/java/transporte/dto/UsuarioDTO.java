package transporte.dto;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record UsuarioDTO(
        UUID id,
        String primerNombre,
        @Nullable String segundoNombre,
        String primerApellido,
        @Nullable String segundoApellido,
        String cedula,
        String correo,
        @Nullable String numeroTelefono,
        UUID rolId,
        String rol,
        @Nullable UUID conductorId,
        boolean activo,
        boolean debeCambiarContrasena)
        implements VistaDTO {}
