package transporte.dominio.valor;

import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.Validacion;

public record Placa(String valor) {
    public Placa {
        valor = Validacion.texto(valor, "Placa", 6, 6);
        if (!valor.matches("[A-Z]{3}[0-9]{3}")) {
            throw new IllegalArgumentException(CatalogoMensajes.EL_FORMATO_DE_PLACA_NO_ES_VALIDO);
        }
    }
}
