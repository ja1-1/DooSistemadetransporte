package transporte.negocio.fachada.assembler.impl;

import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dto.Comandos.Asignar;
import transporte.dto.Comandos.CambiarContrasena;
import transporte.dto.Comandos.CambiarRecorrido;
import transporte.dto.Comandos.FinalizarAsignacion;
import transporte.dto.Comandos.GenerarSalidas;
import transporte.dto.Comandos.ModificarConductor;
import transporte.dto.Comandos.ModificarPerfil;
import transporte.dto.Comandos.ModificarProgramacion;
import transporte.dto.Comandos.ModificarRuta;
import transporte.dto.Comandos.ModificarUsuario;
import transporte.dto.Comandos.ModificarVehiculo;
import transporte.dto.Comandos.Motivo;
import transporte.dto.Comandos.ProgramarSalida;
import transporte.dto.Comandos.Reemplazar;
import transporte.dto.Comandos.Refuerzo;
import transporte.dto.Comandos.RegistrarConductor;
import transporte.dto.Comandos.RegistrarIncidencia;
import transporte.dto.Comandos.RegistrarProgramacion;
import transporte.dto.Comandos.RegistrarRuta;
import transporte.dto.Comandos.RegistrarUsuario;
import transporte.dto.Comandos.RegistrarVehiculo;
import transporte.dto.Comandos.Relevar;
import transporte.dto.Comandos.Reprogramar;
import transporte.dto.geografia.ModificarLugarDTO;
import transporte.dto.geografia.ModificarMunicipioDTO;
import transporte.dto.geografia.ModificarSectorDTO;
import transporte.dto.geografia.ModificarViaDTO;
import transporte.dto.geografia.RegistrarLugarDTO;
import transporte.dto.geografia.RegistrarMunicipioDTO;
import transporte.dto.geografia.RegistrarSectorDTO;
import transporte.dto.geografia.RegistrarViaDTO;
import transporte.negocio.solicitud.Solicitudes;

import org.jspecify.annotations.Nullable;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

public final class SolicitudAssembler {
    private SolicitudAssembler() {}

    private static Set<DayOfWeek> diasValidos(@Nullable Set<@Nullable DayOfWeek> dias) {
        var validos = new HashSet<DayOfWeek>();
        for (var dia : requerido(dias, "diasSemana")) {
            validos.add(requerido(dia, "diaSemana"));
        }
        return Set.copyOf(validos);
    }

    public static Solicitudes.RegistrarConductor convertir(RegistrarConductor datos) {
        requerido(datos, "datos");
        return new Solicitudes.RegistrarConductor(
                requerido(datos.cedula(), "cedula"),
                requerido(datos.primerNombre(), "primerNombre"),
                datos.segundoNombre(),
                requerido(datos.primerApellido(), "primerApellido"),
                datos.segundoApellido(),
                requerido(datos.fechaNacimiento(), "fechaNacimiento"),
                requerido(datos.numeroTelefono(), "numeroTelefono"),
                requerido(datos.tipoSangre(), "tipoSangre"),
                requerido(datos.numeroLicencia(), "numeroLicencia"),
                requerido(datos.categoriaLicencia(), "categoriaLicencia"),
                requerido(datos.fechaVencimientoLicencia(), "fechaVencimientoLicencia"));
    }

    public static Solicitudes.ModificarConductor convertir(ModificarConductor datos) {
        requerido(datos, "datos");
        return new Solicitudes.ModificarConductor(
                requerido(datos.primerNombre(), "primerNombre"),
                datos.segundoNombre(),
                requerido(datos.primerApellido(), "primerApellido"),
                datos.segundoApellido(),
                requerido(datos.numeroTelefono(), "numeroTelefono"),
                requerido(datos.tipoSangre(), "tipoSangre"),
                requerido(datos.numeroLicencia(), "numeroLicencia"),
                requerido(datos.categoriaLicencia(), "categoriaLicencia"),
                requerido(datos.fechaVencimientoLicencia(), "fechaVencimientoLicencia"));
    }

    public static Solicitudes.RegistrarVehiculo convertir(RegistrarVehiculo datos) {
        requerido(datos, "datos");
        return new Solicitudes.RegistrarVehiculo(
                requerido(datos.placa(), "placa"),
                requerido(datos.marca(), "marca"),
                requerido(datos.modelo(), "modelo"),
                requerido(datos.anioFabricacion(), "anioFabricacion"),
                requerido(datos.capacidadPasajeros(), "capacidadPasajeros"),
                requerido(datos.categoriaVehiculoId(), "categoriaVehiculoId"));
    }

    public static Solicitudes.ModificarVehiculo convertir(ModificarVehiculo datos) {
        requerido(datos, "datos");
        return new Solicitudes.ModificarVehiculo(
                requerido(datos.marca(), "marca"),
                requerido(datos.modelo(), "modelo"),
                requerido(datos.anioFabricacion(), "anioFabricacion"),
                requerido(datos.capacidadPasajeros(), "capacidadPasajeros"),
                requerido(datos.categoriaVehiculoId(), "categoriaVehiculoId"));
    }

    public static Solicitudes.RegistrarUsuario convertir(RegistrarUsuario datos) {
        requerido(datos, "datos");
        return new Solicitudes.RegistrarUsuario(
                requerido(datos.primerNombre(), "primerNombre"),
                datos.segundoNombre(),
                requerido(datos.primerApellido(), "primerApellido"),
                datos.segundoApellido(),
                requerido(datos.cedula(), "cedula"),
                requerido(datos.correo(), "correo"),
                datos.numeroTelefono(),
                requerido(datos.rolId(), "rolId"),
                datos.conductorId());
    }

    public static Solicitudes.ModificarUsuario convertir(ModificarUsuario datos) {
        requerido(datos, "datos");
        return new Solicitudes.ModificarUsuario(
                requerido(datos.rolId(), "rolId"), datos.conductorId());
    }

    public static Solicitudes.ModificarPerfil convertir(ModificarPerfil datos) {
        requerido(datos, "datos");
        return new Solicitudes.ModificarPerfil(
                requerido(datos.primerNombre(), "primerNombre"),
                datos.segundoNombre(),
                requerido(datos.primerApellido(), "primerApellido"),
                datos.segundoApellido(),
                datos.numeroTelefono());
    }

    public static Solicitudes.CambiarContrasena convertir(CambiarContrasena datos) {
        requerido(datos, "datos");
        return new Solicitudes.CambiarContrasena(
                requerido(datos.contrasenaActual(), "contrasenaActual"),
                requerido(datos.contrasenaNueva(), "contrasenaNueva"));
    }

    public static Solicitudes.Asignar convertir(Asignar datos) {
        requerido(datos, "datos");
        return new Solicitudes.Asignar(
                requerido(datos.conductorId(), "conductorId"),
                requerido(datos.vehiculoId(), "vehiculoId"),
                requerido(datos.fechaInicio(), "fechaInicio"));
    }

    public static Solicitudes.FinalizarAsignacion convertir(FinalizarAsignacion datos) {
        requerido(datos, "datos");
        return new Solicitudes.FinalizarAsignacion(requerido(datos.fechaFin(), "fechaFin"));
    }

    public static Solicitudes.Relevar convertir(Relevar datos) {
        requerido(datos, "datos");
        return new Solicitudes.Relevar(
                requerido(datos.nuevoConductorId(), "nuevoConductorId"),
                requerido(datos.fechaRelevo(), "fechaRelevo"),
                requerido(datos.motivo(), "motivo"));
    }

    public static Solicitudes.RegistrarRuta convertir(RegistrarRuta datos) {
        requerido(datos, "datos");
        return new Solicitudes.RegistrarRuta(
                requerido(datos.descripcion(), "descripcion"),
                requerido(datos.tipoRutaId(), "tipoRutaId"),
                requerido(datos.origenId(), "origenId"),
                requerido(datos.destinoId(), "destinoId"),
                requerido(datos.viaId(), "viaId"),
                requerido(datos.distanciaTotalKm(), "distanciaTotalKm"));
    }

    public static Solicitudes.ModificarRuta convertir(ModificarRuta datos) {
        requerido(datos, "datos");
        return new Solicitudes.ModificarRuta(requerido(datos.descripcion(), "descripcion"));
    }

    public static Solicitudes.CambiarRecorrido convertir(CambiarRecorrido datos) {
        requerido(datos, "datos");
        return new Solicitudes.CambiarRecorrido(
                requerido(datos.origenId(), "origenId"),
                requerido(datos.destinoId(), "destinoId"),
                requerido(datos.viaId(), "viaId"),
                requerido(datos.distanciaTotalKm(), "distanciaTotalKm"));
    }

    public static Solicitudes.RegistrarProgramacion convertir(RegistrarProgramacion datos) {
        requerido(datos, "datos");
        return new Solicitudes.RegistrarProgramacion(
                requerido(datos.rutaId(), "rutaId"),
                requerido(datos.asignacionId(), "asignacionId"),
                requerido(datos.horaSalida(), "horaSalida"),
                requerido(datos.horaUltimaSalida(), "horaUltimaSalida"),
                requerido(datos.intervaloMinutos(), "intervaloMinutos"),
                requerido(datos.duracionEstimada(), "duracionEstimada"),
                diasValidos(datos.diasSemana()),
                requerido(datos.vigenciaDesde(), "vigenciaDesde"),
                datos.vigenciaHasta());
    }

    public static Solicitudes.ModificarProgramacion convertir(ModificarProgramacion datos) {
        requerido(datos, "datos");
        return new Solicitudes.ModificarProgramacion(
                requerido(datos.asignacionId(), "asignacionId"),
                requerido(datos.horaSalida(), "horaSalida"),
                requerido(datos.horaUltimaSalida(), "horaUltimaSalida"),
                requerido(datos.intervaloMinutos(), "intervaloMinutos"),
                requerido(datos.duracionEstimada(), "duracionEstimada"),
                diasValidos(datos.diasSemana()),
                datos.reprogramarSalidasGeneradas());
    }

    public static Solicitudes.GenerarSalidas convertir(GenerarSalidas datos) {
        requerido(datos, "datos");
        return new Solicitudes.GenerarSalidas(
                requerido(datos.fechaInicial(), "fechaInicial"),
                requerido(datos.fechaFinal(), "fechaFinal"));
    }

    public static Solicitudes.ProgramarSalida convertir(ProgramarSalida datos) {
        requerido(datos, "datos");
        return new Solicitudes.ProgramarSalida(
                requerido(datos.rutaId(), "rutaId"),
                requerido(datos.asignacionId(), "asignacionId"),
                requerido(datos.instanteSalidaProgramado(), "instanteSalidaProgramado"),
                requerido(datos.instanteLlegadaEstimado(), "instanteLlegadaEstimado"));
    }

    public static Solicitudes.Motivo convertir(Motivo datos) {
        requerido(datos, "datos");
        return new Solicitudes.Motivo(requerido(datos.motivo(), "motivo"));
    }

    public static Solicitudes.Reemplazar convertir(Reemplazar datos) {
        requerido(datos, "datos");
        return new Solicitudes.Reemplazar(
                requerido(datos.nuevaAsignacionId(), "nuevaAsignacionId"),
                requerido(datos.motivo(), "motivo"),
                requerido(datos.nuevaLlegadaEstimada(), "nuevaLlegadaEstimada"));
    }

    public static Solicitudes.Reprogramar convertir(Reprogramar datos) {
        requerido(datos, "datos");
        return new Solicitudes.Reprogramar(
                requerido(datos.instanteSalidaProgramado(), "instanteSalidaProgramado"),
                datos.instanteLlegadaEstimado(),
                requerido(datos.motivo(), "motivo"));
    }

    public static Solicitudes.Refuerzo convertir(Refuerzo datos) {
        requerido(datos, "datos");
        return new Solicitudes.Refuerzo(
                requerido(datos.asignacionId(), "asignacionId"),
                requerido(datos.instanteSalida(), "instanteSalida"),
                requerido(datos.motivo(), "motivo"));
    }

    public static Solicitudes.RegistrarIncidencia convertir(RegistrarIncidencia datos) {
        requerido(datos, "datos");
        return new Solicitudes.RegistrarIncidencia(
                requerido(datos.tipoIncidenciaId(), "tipoIncidenciaId"),
                requerido(datos.instanteOcurrencia(), "instanteOcurrencia"),
                requerido(datos.descripcion(), "descripcion"));
    }

    public static Solicitudes.ModificarSector convertir(ModificarSectorDTO datos) {
        requerido(datos, "datos");
        return new Solicitudes.ModificarSector(requerido(datos.nombre(), "nombre"));
    }

    public static Solicitudes.ModificarMunicipio convertir(ModificarMunicipioDTO datos) {
        requerido(datos, "datos");
        return new Solicitudes.ModificarMunicipio(
                requerido(datos.nombre(), "nombre"),
                requerido(datos.departamento(), "departamento"));
    }

    public static Solicitudes.ModificarVia convertir(ModificarViaDTO datos) {
        requerido(datos, "datos");
        return new Solicitudes.ModificarVia(
                requerido(datos.nombre(), "nombre"), requerido(datos.descripcion(), "descripcion"));
    }

    public static Solicitudes.RegistrarMunicipio convertir(RegistrarMunicipioDTO datos) {
        requerido(datos, "datos");
        return new Solicitudes.RegistrarMunicipio(
                requerido(datos.nombre(), "nombre"),
                requerido(datos.departamento(), "departamento"));
    }

    public static Solicitudes.ModificarLugar convertir(ModificarLugarDTO datos) {
        requerido(datos, "datos");
        return new Solicitudes.ModificarLugar(requerido(datos.nombre(), "nombre"));
    }

    public static Solicitudes.RegistrarVia convertir(RegistrarViaDTO datos) {
        requerido(datos, "datos");
        return new Solicitudes.RegistrarVia(
                requerido(datos.nombre(), "nombre"), requerido(datos.descripcion(), "descripcion"));
    }

    public static Solicitudes.RegistrarLugar convertir(RegistrarLugarDTO datos) {
        requerido(datos, "datos");
        return new Solicitudes.RegistrarLugar(
                requerido(datos.nombre(), "nombre"), requerido(datos.sectorId(), "sectorId"));
    }

    public static Solicitudes.RegistrarSector convertir(RegistrarSectorDTO datos) {
        requerido(datos, "datos");
        return new Solicitudes.RegistrarSector(
                requerido(datos.nombre(), "nombre"), requerido(datos.municipioId(), "municipioId"));
    }
}
