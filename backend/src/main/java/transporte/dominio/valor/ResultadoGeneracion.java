package transporte.dominio.valor;

import static transporte.transversal.utilitarios.Validacion.requerido;
import static transporte.transversal.utilitarios.Validacion.texto;

import transporte.dominio.SalidaDominio;
import transporte.transversal.mensajes.CatalogoMensajes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ResultadoGeneracion(
        List<SalidaDominio> salidasGeneradas,
        List<SalidaDominio> salidasExistentes,
        Map<LocalDateTime, String> rechazos) {
    public ResultadoGeneracion {
        salidasGeneradas = List.copyOf(requerido(salidasGeneradas, "salidasGeneradas"));
        salidasExistentes = List.copyOf(requerido(salidasExistentes, "salidasExistentes"));
        requerido(rechazos, "rechazos");
        for (var rechazo : rechazos.entrySet()) {
            requerido(rechazo.getKey(), "instanteRechazo");
            texto(rechazo.getValue(), "motivoRechazo", 1, 1000);
        }
        rechazos = Map.copyOf(requerido(rechazos, "rechazos"));
        var ids = new HashSet<UUID>();
        for (var salida : salidasGeneradas) {
            if (!ids.add(salida.getId())) {
                throw new IllegalArgumentException(CatalogoMensajes.HAY_UNA_SALIDA_REPETIDA);
            }
        }
        for (var salida : salidasExistentes) {
            if (!ids.add(salida.getId())) {
                throw new IllegalArgumentException(
                        CatalogoMensajes.UNA_SALIDA_NO_PUEDE_SER_NUEVA_Y);
            }
        }
        for (var rechazo : rechazos.entrySet()) {
            texto(rechazo.getValue(), "motivoRechazo", 1, 1000);
        }
    }

    public boolean tieneRechazos() {
        return !rechazos.isEmpty();
    }
}
