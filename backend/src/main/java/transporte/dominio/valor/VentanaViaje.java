package transporte.dominio.valor;

import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.Validacion;

import java.time.LocalDateTime;

public record VentanaViaje(
        LocalDateTime instanteSalidaProgramado, LocalDateTime instanteLlegadaEstimado) {
    public VentanaViaje {
        instanteSalidaProgramado =
                Validacion.instante(instanteSalidaProgramado, "instanteSalidaProgramado");
        instanteLlegadaEstimado =
                Validacion.instante(instanteLlegadaEstimado, "instanteLlegadaEstimado");
        if (instanteSalidaProgramado == null
                || instanteLlegadaEstimado == null
                || !instanteLlegadaEstimado.isAfter(instanteSalidaProgramado)) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.LA_VENTANA_REQUIERE_UNA_LLEGADA_POSTERIOR_A);
        }
    }

    public boolean seSolapaCon(VentanaViaje otra) {
        if (otra == null) {
            throw new IllegalArgumentException(CatalogoMensajes.LA_VENTANA_ES_OBLIGATORIA);
        }
        return instanteSalidaProgramado.isBefore(otra.instanteLlegadaEstimado)
                && otra.instanteSalidaProgramado.isBefore(instanteLlegadaEstimado);
    }
}
