package transporte.dominio.valor;

import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.Validacion;

public record Correo(String valor) {
    public Correo {
        valor = Validacion.texto(valor, "Correo", 5, 120);
        if (!valor.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            throw new IllegalArgumentException(CatalogoMensajes.EL_FORMATO_DE_CORREO_NO_ES_VALIDO);
        }
    }
}
