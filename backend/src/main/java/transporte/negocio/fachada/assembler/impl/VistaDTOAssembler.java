package transporte.negocio.fachada.assembler.impl;

import transporte.dominio.AsignacionDominio;
import transporte.dominio.ConductorDominio;
import transporte.dominio.Incidencia;
import transporte.dominio.ProgramacionRutaDominio;
import transporte.dominio.RutaDominio;
import transporte.dominio.SalidaDominio;
import transporte.dominio.UsuarioDominio;
import transporte.dominio.VehiculoDominio;
import transporte.dominio.valor.IncidenciaDeSalida;
import transporte.dominio.valor.Telefono;
import transporte.dto.AsignacionDTO;
import transporte.dto.ConductorDTO;
import transporte.dto.IncidenciaDTO;
import transporte.dto.ProgramacionRutaDTO;
import transporte.dto.RutaDTO;
import transporte.dto.SalidaDTO;
import transporte.dto.UsuarioDTO;
import transporte.dto.VehiculoDTO;

public final class VistaDTOAssembler {
    private VistaDTOAssembler() {}

    public static ConductorDTO convertirADTO(ConductorDominio dominio) {
        return new ConductorDTO(
                dominio.getId(),
                dominio.getCedula().valor(),
                dominio.getPrimerNombre(),
                dominio.getSegundoNombre().orElse(null),
                dominio.getPrimerApellido(),
                dominio.getSegundoApellido().orElse(null),
                dominio.getFechaNacimiento(),
                dominio.getNumeroTelefono().valor(),
                dominio.getTipoSangre().getSimbolo(),
                dominio.getLicencia(),
                dominio.isActivo());
    }

    public static VehiculoDTO convertirADTO(VehiculoDominio dominio) {
        return new VehiculoDTO(
                dominio.getId(),
                dominio.getPlaca().valor(),
                dominio.getMarca(),
                dominio.getModelo(),
                dominio.getAnioFabricacion(),
                dominio.getCapacidadPasajeros(),
                dominio.getCategoriaVehiculoId(),
                dominio.isActivo());
    }

    public static UsuarioDTO convertirADTO(UsuarioDominio dominio) {
        return new UsuarioDTO(
                dominio.getId(),
                dominio.getPrimerNombre(),
                dominio.getSegundoNombre().orElse(null),
                dominio.getPrimerApellido(),
                dominio.getSegundoApellido().orElse(null),
                dominio.getCedula().valor(),
                dominio.getCorreo().valor(),
                dominio.getNumeroTelefono().map(Telefono::valor).orElse(null),
                dominio.getRol().getId(),
                dominio.getRol().getNombre(),
                dominio.getConductorId().orElse(null),
                dominio.isActivo(),
                dominio.isDebeCambiarContrasena());
    }

    public static AsignacionDTO convertirADTO(AsignacionDominio dominio) {
        return new AsignacionDTO(
                dominio.getId(),
                dominio.getConductorId(),
                dominio.getVehiculoId(),
                dominio.getPeriodo().inicio(),
                dominio.getPeriodo().fin().orElse(null),
                null);
    }

    public static RutaDTO convertirADTO(RutaDominio dominio) {
        return new RutaDTO(
                dominio.getId(),
                dominio.getNombre(),
                dominio.getDescripcion(),
                dominio.getTipoRuta().getId(),
                dominio.getOrigenId(),
                dominio.getDestinoId(),
                dominio.getViaId(),
                dominio.getDistanciaTotalKm(),
                dominio.isActiva());
    }

    public static ProgramacionRutaDTO convertirADTO(ProgramacionRutaDominio dominio) {
        return new ProgramacionRutaDTO(
                dominio.getId(),
                dominio.getRutaId(),
                dominio.getAsignacionId(),
                dominio.getHorario(),
                dominio.getVigencia().inicio(),
                dominio.getVigencia().fin().orElse(null),
                dominio.isActiva());
    }

    public static SalidaDTO convertirADTO(SalidaDominio dominio) {
        return new SalidaDTO(
                dominio.getId(),
                dominio.getRutaId(),
                dominio.getNombreRuta(),
                dominio.getAsignacionId(),
                dominio.getVentana(),
                dominio.getInstanteSalidaReal().orElse(null),
                dominio.getInstanteLlegadaReal().orElse(null),
                dominio.getEstadoSalida().getId(),
                dominio.getEstadoSalida().getNombre(),
                dominio.getProgramacionRutaId().orElse(null));
    }

    public static IncidenciaDTO convertirADTO(Incidencia dominio) {
        return new IncidenciaDTO(
                dominio.getId(),
                dominio.getTipoIncidencia().getId(),
                dominio.getInstanteOcurrencia(),
                dominio.getInstanteReporte(),
                dominio.getDescripcion(),
                dominio.getResponsable(),
                null);
    }

    public static IncidenciaDTO convertirADTO(IncidenciaDeSalida proyeccion) {
        return convertirADTO(proyeccion.incidencia()).conSalidaId(proyeccion.salidaId());
    }
}
