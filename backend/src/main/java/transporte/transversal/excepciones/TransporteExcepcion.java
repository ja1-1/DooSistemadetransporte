package transporte.transversal.excepciones;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

public abstract class TransporteExcepcion extends RuntimeException {
    private final Capa capa;
    private final String mensajeTecnico;

    protected TransporteExcepcion(
            Capa capa, String usuario, String tecnico, @Nullable Throwable causa) {
        super(Objects.requireNonNull(usuario), causa);
        this.capa = Objects.requireNonNull(capa);
        mensajeTecnico = Objects.requireNonNull(tecnico);
    }

    public Capa getCapa() {
        return capa;
    }

    public String getMensajeUsuario() {
        return Objects.requireNonNull(getMessage());
    }

    public String getMensajeTecnico() {
        return mensajeTecnico;
    }
}
