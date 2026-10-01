package transporte.transversal.excepciones;

public class NegocioExcepcion extends TransporteExcepcion {
    public enum Tipo {
        NO_ENCONTRADO,
        CONFLICTO
    }

    private final Tipo tipo;

    public NegocioExcepcion(Tipo tipo, String mensaje) {
        super(Capa.NEGOCIO, mensaje, mensaje, null);
        this.tipo = tipo;
    }

    public Tipo getTipo() {
        return tipo;
    }
}
