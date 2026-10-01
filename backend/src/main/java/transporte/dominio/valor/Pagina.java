package transporte.dominio.valor;

import transporte.transversal.mensajes.CatalogoMensajes;

public record Pagina(int numero, int tamano) {
    public Pagina {
        if (numero < 1 || tamano < 1 || tamano > 100) {
            throw new IllegalArgumentException(CatalogoMensajes.LA_PAGINA_DEBE_EMPEZAR_EN_1_Y);
        }
    }

    public long desde() {
        return ((long) numero - 1) * tamano + 1;
    }

    public long hasta() {
        return (long) numero * tamano;
    }

    public long desplazamiento() {
        return ((long) numero - 1) * tamano;
    }
}
