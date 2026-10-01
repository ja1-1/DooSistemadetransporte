package transporte.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record GeneracionSalidasDTO(
        List<SalidaDTO> salidasGeneradas,
        List<SalidaDTO> salidasExistentes,
        Map<LocalDateTime, String> rechazos) {}
