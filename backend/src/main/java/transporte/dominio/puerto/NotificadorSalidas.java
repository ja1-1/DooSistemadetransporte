package transporte.dominio.puerto;

import transporte.dominio.SalidaDominio;

import java.time.LocalDateTime;

public interface NotificadorSalidas {
    void notificarCancelacion(SalidaDominio salida);

    void notificarReprogramacion(SalidaDominio salida, LocalDateTime instanteAnterior);
}
