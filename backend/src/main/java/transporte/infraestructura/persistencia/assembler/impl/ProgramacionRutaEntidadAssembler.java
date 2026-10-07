package transporte.infraestructura.persistencia.assembler.impl;

import transporte.dominio.ProgramacionRutaDominio;
import transporte.dominio.valor.HorarioProgramado;
import transporte.dominio.valor.Periodo;
import transporte.entidad.ProgramacionRutaEntidad;
import transporte.infraestructura.persistencia.assembler.EntidadAssembler;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProgramacionRutaEntidadAssembler
        implements EntidadAssembler<ProgramacionRutaDominio, ProgramacionRutaEntidad> {
    @Override
    public ProgramacionRutaDominio convertirADominio(
            ProgramacionRutaEntidad programacionRutaEntidad) {
        return new ProgramacionRutaDominio(
                programacionRutaEntidad.id(),
                programacionRutaEntidad.rutaId(),
                programacionRutaEntidad.asignacionId(),
                new HorarioProgramado(
                        programacionRutaEntidad.horaSalida(),
                        programacionRutaEntidad.horaUltimaSalida(),
                        programacionRutaEntidad.intervaloMinutos(),
                        programacionRutaEntidad.duracionEstimada(),
                        programacionRutaEntidad.dias()),
                new Periodo(
                        programacionRutaEntidad.vigenciaDesde(),
                        Optional.ofNullable(programacionRutaEntidad.vigenciaHasta())),
                programacionRutaEntidad.activa());
    }

    @Override
    public ProgramacionRutaEntidad convertirAEntidad(ProgramacionRutaDominio programacionRuta) {
        return new ProgramacionRutaEntidad(
                programacionRuta.getId(),
                programacionRuta.getRutaId(),
                programacionRuta.getAsignacionId(),
                programacionRuta.getHorario().horaSalida(),
                programacionRuta.getHorario().horaUltimaSalida(),
                programacionRuta.getHorario().intervaloMinutos(),
                programacionRuta.getHorario().duracionEstimada(),
                programacionRuta.getVigencia().inicio(),
                programacionRuta.getVigencia().fin().orElse(null),
                programacionRuta.isActiva(),
                programacionRuta.getHorario().diasSemana());
    }
}
