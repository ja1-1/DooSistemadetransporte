package transporte.negocio.reglas;

@FunctionalInterface
public interface Rule<T> {
    void ejecutar(T datos);
}
