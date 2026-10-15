package transporte.negocio.resultado;

import transporte.dominio.ProgramacionRutaDominio;
import transporte.dominio.SalidaDominio;

import java.util.List;

public record CambioProgramacion(
        ProgramacionRutaDominio programacion, List<SalidaDominio> salidasSinCambiar) {}
