package transporte.dominio.valor;

import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.Validacion;

public record Telefono(String valor) {
    public Telefono {
        valor = Validacion.texto(valor, "Telefono", 10, 10);
        if (!valor.matches("3[0-9]{9}")) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.EL_FORMATO_DE_TELEFONO_NO_ES_VALIDO);
        }
    }
}
