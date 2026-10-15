package transporte.negocio.solicitud;

import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

public final class Solicitudes {
    private Solicitudes() {}

    public record RegistrarConductor(
            String cedula,
            String primerNombre,
            @Nullable String segundoNombre,
            String primerApellido,
            @Nullable String segundoApellido,
            LocalDate fechaNacimiento,
            String numeroTelefono,
            String tipoSangre,
            String numeroLicencia,
            String categoriaLicencia,
            LocalDate fechaVencimientoLicencia) {}

    public record ModificarConductor(
            String primerNombre,
            @Nullable String segundoNombre,
            String primerApellido,
            @Nullable String segundoApellido,
            String numeroTelefono,
            String tipoSangre,
            String numeroLicencia,
            String categoriaLicencia,
            LocalDate fechaVencimientoLicencia) {}

    public record RegistrarVehiculo(
            String placa,
            String marca,
            String modelo,
            Integer anioFabricacion,
            Integer capacidadPasajeros,
            UUID categoriaVehiculoId) {}

    public record ModificarVehiculo(
            String marca,
            String modelo,
            Integer anioFabricacion,
            Integer capacidadPasajeros,
            UUID categoriaVehiculoId) {}

    public record RegistrarUsuario(
            String primerNombre,
            @Nullable String segundoNombre,
            String primerApellido,
            @Nullable String segundoApellido,
            String cedula,
            String correo,
            @Nullable String numeroTelefono,
            UUID rolId,
            @Nullable UUID conductorId) {}

    public record ModificarUsuario(UUID rolId, @Nullable UUID conductorId) {}

    public record ModificarPerfil(
            String primerNombre,
            @Nullable String segundoNombre,
            String primerApellido,
            @Nullable String segundoApellido,
            @Nullable String numeroTelefono) {}

    public record CambiarContrasena(String contrasenaActual, String contrasenaNueva) {
        @Override
        public String toString() {
            return "CambiarContrasena[credenciales protegidas]";
        }
    }

    public record Asignar(UUID conductorId, UUID vehiculoId, LocalDate fechaInicio) {}

    public record FinalizarAsignacion(LocalDate fechaFin) {}

    public record Relevar(UUID nuevoConductorId, LocalDate fechaRelevo, String motivo) {}

    public record RegistrarRuta(
            String descripcion,
            UUID tipoRutaId,
            UUID origenId,
            UUID destinoId,
            UUID viaId,
            BigDecimal distanciaTotalKm) {}

    public record ModificarRuta(String descripcion) {}

    public record CambiarRecorrido(
            UUID origenId, UUID destinoId, UUID viaId, BigDecimal distanciaTotalKm) {}

    public record RegistrarProgramacion(
            UUID rutaId,
            UUID asignacionId,
            LocalTime horaSalida,
            LocalTime horaUltimaSalida,
            Integer intervaloMinutos,
            Integer duracionEstimada,
            Set<DayOfWeek> diasSemana,
            LocalDate vigenciaDesde,
            @Nullable LocalDate vigenciaHasta) {}

    public record ModificarProgramacion(
            UUID asignacionId,
            LocalTime horaSalida,
            LocalTime horaUltimaSalida,
            Integer intervaloMinutos,
            Integer duracionEstimada,
            Set<DayOfWeek> diasSemana,
            boolean reprogramarSalidasGeneradas) {}

    public record GenerarSalidas(LocalDate fechaInicial, LocalDate fechaFinal) {}

    public record ProgramarSalida(
            UUID rutaId,
            UUID asignacionId,
            LocalDateTime instanteSalidaProgramado,
            LocalDateTime instanteLlegadaEstimado) {}

    public record Motivo(String motivo) {}

    public record Reemplazar(
            UUID nuevaAsignacionId, String motivo, LocalDateTime nuevaLlegadaEstimada) {}

    public record Reprogramar(
            LocalDateTime instanteSalidaProgramado,
            @Nullable LocalDateTime instanteLlegadaEstimado,
            String motivo) {}

    public record Refuerzo(UUID asignacionId, LocalDateTime instanteSalida, String motivo) {}

    public record RegistrarIncidencia(
            UUID tipoIncidenciaId, LocalDateTime instanteOcurrencia, String descripcion) {}

    public record ModificarSector(String nombre) {}

    public record ModificarMunicipio(String nombre, String departamento) {}

    public record ModificarVia(String nombre, String descripcion) {}

    public record RegistrarMunicipio(String nombre, String departamento) {}

    public record ModificarLugar(String nombre) {}

    public record RegistrarVia(String nombre, String descripcion) {}

    public record RegistrarLugar(String nombre, UUID sectorId) {}

    public record RegistrarSector(String nombre, UUID municipioId) {}
}
