package transporte.dto;

import java.util.List;

public record CambioProgramacionDTO(
        ProgramacionRutaDTO programacion, List<SalidaDTO> salidasSinCambiar) {}
