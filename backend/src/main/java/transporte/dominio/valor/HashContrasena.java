package transporte.dominio.valor;

import transporte.transversal.utilitarios.Validacion;

public record HashContrasena(String valor) {
    public HashContrasena {
        valor = Validacion.texto(valor, "HashContrasena", 60, 120);
    }

    @Override
    public String toString() {
        return "HashContrasena[protegido]";
    }
}
