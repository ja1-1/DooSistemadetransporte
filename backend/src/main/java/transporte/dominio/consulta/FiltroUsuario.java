package transporte.dominio.consulta;

import transporte.dominio.enums.Rol;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record FiltroUsuario(
        @Nullable UUID id,
        @Nullable String primerNombre,
        @Nullable String segundoNombre,
        @Nullable String primerApellido,
        @Nullable String segundoApellido,
        @Nullable String cedula,
        @Nullable String correo,
        @Nullable String numeroTelefono,
        @Nullable Rol rol,
        @Nullable UUID conductorId,
        @Nullable Boolean activo,
        @Nullable Boolean debeCambiarContrasena) {
    public static FiltroUsuario todos() {
        return new FiltroUsuario(
                null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public FiltroUsuario conId(@Nullable UUID valor) {
        return new FiltroUsuario(
                valor,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                cedula,
                correo,
                numeroTelefono,
                rol,
                conductorId,
                activo,
                debeCambiarContrasena);
    }

    public FiltroUsuario conPrimerNombre(@Nullable String valor) {
        return new FiltroUsuario(
                id,
                valor,
                segundoNombre,
                primerApellido,
                segundoApellido,
                cedula,
                correo,
                numeroTelefono,
                rol,
                conductorId,
                activo,
                debeCambiarContrasena);
    }

    public FiltroUsuario conSegundoNombre(@Nullable String valor) {
        return new FiltroUsuario(
                id,
                primerNombre,
                valor,
                primerApellido,
                segundoApellido,
                cedula,
                correo,
                numeroTelefono,
                rol,
                conductorId,
                activo,
                debeCambiarContrasena);
    }

    public FiltroUsuario conPrimerApellido(@Nullable String valor) {
        return new FiltroUsuario(
                id,
                primerNombre,
                segundoNombre,
                valor,
                segundoApellido,
                cedula,
                correo,
                numeroTelefono,
                rol,
                conductorId,
                activo,
                debeCambiarContrasena);
    }

    public FiltroUsuario conSegundoApellido(@Nullable String valor) {
        return new FiltroUsuario(
                id,
                primerNombre,
                segundoNombre,
                primerApellido,
                valor,
                cedula,
                correo,
                numeroTelefono,
                rol,
                conductorId,
                activo,
                debeCambiarContrasena);
    }

    public FiltroUsuario conCedula(@Nullable String valor) {
        return new FiltroUsuario(
                id,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                valor,
                correo,
                numeroTelefono,
                rol,
                conductorId,
                activo,
                debeCambiarContrasena);
    }

    public FiltroUsuario conCorreo(@Nullable String valor) {
        return new FiltroUsuario(
                id,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                cedula,
                valor,
                numeroTelefono,
                rol,
                conductorId,
                activo,
                debeCambiarContrasena);
    }

    public FiltroUsuario conNumeroTelefono(@Nullable String valor) {
        return new FiltroUsuario(
                id,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                cedula,
                correo,
                valor,
                rol,
                conductorId,
                activo,
                debeCambiarContrasena);
    }

    public FiltroUsuario conRol(@Nullable Rol valor) {
        return new FiltroUsuario(
                id,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                cedula,
                correo,
                numeroTelefono,
                valor,
                conductorId,
                activo,
                debeCambiarContrasena);
    }

    public FiltroUsuario conConductorId(@Nullable UUID valor) {
        return new FiltroUsuario(
                id,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                cedula,
                correo,
                numeroTelefono,
                rol,
                valor,
                activo,
                debeCambiarContrasena);
    }

    public FiltroUsuario conActivo(@Nullable Boolean valor) {
        return new FiltroUsuario(
                id,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                cedula,
                correo,
                numeroTelefono,
                rol,
                conductorId,
                valor,
                debeCambiarContrasena);
    }

    public FiltroUsuario conDebeCambiarContrasena(@Nullable Boolean valor) {
        return new FiltroUsuario(
                id,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                cedula,
                correo,
                numeroTelefono,
                rol,
                conductorId,
                activo,
                valor);
    }
}
