package transporte.dominio.consulta;

import transporte.dominio.enums.CategoriaLicencia;
import transporte.dominio.enums.TipoSangre;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;
import java.util.UUID;

public record FiltroConductor(
        @Nullable UUID id,
        @Nullable String cedula,
        @Nullable String primerNombre,
        @Nullable String segundoNombre,
        @Nullable String primerApellido,
        @Nullable String segundoApellido,
        @Nullable LocalDate fechaNacimiento,
        @Nullable String numeroTelefono,
        @Nullable TipoSangre tipoSangre,
        @Nullable String numeroLicencia,
        @Nullable CategoriaLicencia categoriaLicencia,
        @Nullable LocalDate fechaVencimientoLicencia,
        @Nullable Boolean activo) {
    public static FiltroConductor todos() {
        return new FiltroConductor(
                null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public FiltroConductor conId(@Nullable UUID valor) {
        return new FiltroConductor(
                valor,
                cedula,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                fechaNacimiento,
                numeroTelefono,
                tipoSangre,
                numeroLicencia,
                categoriaLicencia,
                fechaVencimientoLicencia,
                activo);
    }

    public FiltroConductor conCedula(@Nullable String valor) {
        return new FiltroConductor(
                id,
                valor,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                fechaNacimiento,
                numeroTelefono,
                tipoSangre,
                numeroLicencia,
                categoriaLicencia,
                fechaVencimientoLicencia,
                activo);
    }

    public FiltroConductor conPrimerNombre(@Nullable String valor) {
        return new FiltroConductor(
                id,
                cedula,
                valor,
                segundoNombre,
                primerApellido,
                segundoApellido,
                fechaNacimiento,
                numeroTelefono,
                tipoSangre,
                numeroLicencia,
                categoriaLicencia,
                fechaVencimientoLicencia,
                activo);
    }

    public FiltroConductor conSegundoNombre(@Nullable String valor) {
        return new FiltroConductor(
                id,
                cedula,
                primerNombre,
                valor,
                primerApellido,
                segundoApellido,
                fechaNacimiento,
                numeroTelefono,
                tipoSangre,
                numeroLicencia,
                categoriaLicencia,
                fechaVencimientoLicencia,
                activo);
    }

    public FiltroConductor conPrimerApellido(@Nullable String valor) {
        return new FiltroConductor(
                id,
                cedula,
                primerNombre,
                segundoNombre,
                valor,
                segundoApellido,
                fechaNacimiento,
                numeroTelefono,
                tipoSangre,
                numeroLicencia,
                categoriaLicencia,
                fechaVencimientoLicencia,
                activo);
    }

    public FiltroConductor conSegundoApellido(@Nullable String valor) {
        return new FiltroConductor(
                id,
                cedula,
                primerNombre,
                segundoNombre,
                primerApellido,
                valor,
                fechaNacimiento,
                numeroTelefono,
                tipoSangre,
                numeroLicencia,
                categoriaLicencia,
                fechaVencimientoLicencia,
                activo);
    }

    public FiltroConductor conFechaNacimiento(@Nullable LocalDate valor) {
        return new FiltroConductor(
                id,
                cedula,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                valor,
                numeroTelefono,
                tipoSangre,
                numeroLicencia,
                categoriaLicencia,
                fechaVencimientoLicencia,
                activo);
    }

    public FiltroConductor conNumeroTelefono(@Nullable String valor) {
        return new FiltroConductor(
                id,
                cedula,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                fechaNacimiento,
                valor,
                tipoSangre,
                numeroLicencia,
                categoriaLicencia,
                fechaVencimientoLicencia,
                activo);
    }

    public FiltroConductor conTipoSangre(@Nullable TipoSangre valor) {
        return new FiltroConductor(
                id,
                cedula,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                fechaNacimiento,
                numeroTelefono,
                valor,
                numeroLicencia,
                categoriaLicencia,
                fechaVencimientoLicencia,
                activo);
    }

    public FiltroConductor conNumeroLicencia(@Nullable String valor) {
        return new FiltroConductor(
                id,
                cedula,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                fechaNacimiento,
                numeroTelefono,
                tipoSangre,
                valor,
                categoriaLicencia,
                fechaVencimientoLicencia,
                activo);
    }

    public FiltroConductor conCategoriaLicencia(@Nullable CategoriaLicencia valor) {
        return new FiltroConductor(
                id,
                cedula,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                fechaNacimiento,
                numeroTelefono,
                tipoSangre,
                numeroLicencia,
                valor,
                fechaVencimientoLicencia,
                activo);
    }

    public FiltroConductor conFechaVencimientoLicencia(@Nullable LocalDate valor) {
        return new FiltroConductor(
                id,
                cedula,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                fechaNacimiento,
                numeroTelefono,
                tipoSangre,
                numeroLicencia,
                categoriaLicencia,
                valor,
                activo);
    }

    public FiltroConductor conActivo(@Nullable Boolean valor) {
        return new FiltroConductor(
                id,
                cedula,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                fechaNacimiento,
                numeroTelefono,
                tipoSangre,
                numeroLicencia,
                categoriaLicencia,
                fechaVencimientoLicencia,
                valor);
    }
}
