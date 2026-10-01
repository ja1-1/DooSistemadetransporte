package transporte.dominio.valor;

import transporte.transversal.utilitarios.Validacion;

public record Cedula(String valor) {
    public Cedula {
        valor = Validacion.texto(valor, "Cedula", 5, 20);
    }
}
