package transporte.dto;

import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

public final class Comandos {
    private Comandos() {}

    public record RegistrarConductor(
            @Nullable String cedula,
            @Nullable String primerNombre,
            @Nullable String segundoNombre,
            @Nullable String primerApellido,
            @Nullable String segundoApellido,
            @Nullable LocalDate fechaNacimiento,
            @Nullable String numeroTelefono,
            @Nullable String tipoSangre,
            @Nullable String numeroLicencia,
            @Nullable String categoriaLicencia,
            @Nullable LocalDate fechaVencimientoLicencia) {}

    public record ModificarConductor(
            @Nullable String primerNombre,
            @Nullable String segundoNombre,
            @Nullable String primerApellido,
            @Nullable String segundoApellido,
            @Nullable String numeroTelefono,
            @Nullable String tipoSangre,
            @Nullable String numeroLicencia,
            @Nullable String categoriaLicencia,
            @Nullable LocalDate fechaVencimientoLicencia) {}

    public record RegistrarVehiculo(
            @Nullable String placa,
            @Nullable String marca,
            @Nullable String modelo,
            @Nullable Integer anioFabricacion,
            @Nullable Integer capacidadPasajeros,
            @Nullable UUID categoriaVehiculoId) {}

    public record ModificarVehiculo(
            @Nullable String marca,
            @Nullable String modelo,
            @Nullable Integer anioFabricacion,
            @Nullable Integer capacidadPasajeros,
            @Nullable UUID categoriaVehiculoId) {}

    public record RegistrarUsuario(
            @Nullable String primerNombre,
            @Nullable String segundoNombre,
            @Nullable String primerApellido,
            @Nullable String segundoApellido,
            @Nullable String cedula,
            @Nullable String correo,
            @Nullable String numeroTelefono,
            @Nullable UUID rolId,
            @Nullable UUID conductorId) {}

    public record ModificarUsuario(@Nullable UUID rolId, @Nullable UUID conductorId) {}

    public record ModificarPerfil(
            @Nullable String primerNombre,
            @Nullable String segundoNombre,
            @Nullable String primerApellido,
            @Nullable String segundoApellido,
            @Nullable String numeroTelefono) {}

    public record CambiarContrasena(
            @Nullable String contrasenaActual, @Nullable String contrasenaNueva) {
        @Override
        public String toString() {
            return "CambiarContrasena[credenciales protegidas]";
        }
    }

    public record Asignar(
            @Nullable UUID conductorId,
            @Nullable UUID vehiculoId,
            @Nullable LocalDate fechaInicio) {}

    public record FinalizarAsignacion(@Nullable LocalDate fechaFin) {}

    public record Relevar(
            @Nullable UUID nuevoConductorId,
            @Nullable LocalDate fechaRelevo,
            @Nullable String motivo) {}

    public record RegistrarRuta(
            @Nullable String descripcion,
            @Nullable UUID tipoRutaId,
            @Nullable UUID origenId,
            @Nullable UUID destinoId,
            @Nullable UUID viaId,
            @Nullable BigDecimal distanciaTotalKm) {}

    public record ModificarRuta(@Nullable String descripcion) {}

    public record CambiarRecorrido(
            @Nullable UUID origenId,
            @Nullable UUID destinoId,
            @Nullable UUID viaId,
            @Nullable BigDecimal distanciaTotalKm) {}

    public record RegistrarProgramacion(
            @Nullable UUID rutaId,
            @Nullable UUID asignacionId,
            @Nullable LocalTime horaSalida,
            @Nullable LocalTime horaUltimaSalida,
            @Nullable Integer intervaloMinutos,
            @Nullable Integer duracionEstimada,
            @Nullable Set<@Nullable DayOfWeek> diasSemana,
            @Nullable LocalDate vigenciaDesde,
            @Nullable LocalDate vigenciaHasta) {}

    public record ModificarProgramacion(
            @Nullable UUID asignacionId,
            @Nullable LocalTime horaSalida,
            @Nullable LocalTime horaUltimaSalida,
            @Nullable Integer intervaloMinutos,
            @Nullable Integer duracionEstimada,
            @Nullable Set<@Nullable DayOfWeek> diasSemana,
            boolean reprogramarSalidasGeneradas) {}

    public record GenerarSalidas(
            @Nullable LocalDate fechaInicial, @Nullable LocalDate fechaFinal) {}

    public record ProgramarSalida(
            @Nullable UUID rutaId,
            @Nullable UUID asignacionId,
            @Nullable LocalDateTime instanteSalidaProgramado,
            @Nullable LocalDateTime instanteLlegadaEstimado) {}

    public record Motivo(@Nullable String motivo) {}

    public record Reemplazar(
            @Nullable UUID nuevaAsignacionId,
            @Nullable String motivo,
            @Nullable LocalDateTime nuevaLlegadaEstimada) {}

    public record Reprogramar(
            @Nullable LocalDateTime instanteSalidaProgramado,
            @Nullable LocalDateTime instanteLlegadaEstimado,
            @Nullable String motivo) {}

    public record Refuerzo(
            @Nullable UUID asignacionId,
            @Nullable LocalDateTime instanteSalida,
            @Nullable String motivo) {}

    public record RegistrarIncidencia(
            @Nullable UUID tipoIncidenciaId,
            @Nullable LocalDateTime instanteOcurrencia,
            @Nullable String descripcion) {}
}
