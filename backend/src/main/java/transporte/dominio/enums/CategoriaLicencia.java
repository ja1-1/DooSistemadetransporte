package transporte.dominio.enums;

import transporte.transversal.mensajes.CatalogoMensajes;

public enum CategoriaLicencia {
    C1,
    C2,
    C3;

    public boolean cubre(CategoriaLicencia minima) {
        return compareTo(minima) >= 0;
    }

    public static CategoriaLicencia desdeTexto(String texto) {
        try {
            return valueOf(texto == null ? "" : texto.strip());
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException(CatalogoMensajes.LA_LICENCIA_MINIMA_DEBE_SER_C1_C2);
        }
    }
}
