package transporte.transversal.utilitarios;

import transporte.transversal.excepciones.IdentificadorOcupadoExcepcion;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class IdentidadDisponible {
    private IdentidadDisponible() {}

    public static <T> T crear(Supplier<T> construir, Predicate<T> ocupado) {
        T candidato;
        do {
            candidato = construir.get();
        } while (ocupado.test(candidato));
        return candidato;
    }

    public static <T> T crearYGuardar(
            Supplier<T> construir, Predicate<T> ocupado, Consumer<T> guardar) {
        while (true) {
            T candidato = crear(construir, ocupado);
            try {
                guardar.accept(candidato);
                return candidato;
            } catch (IdentificadorOcupadoExcepcion ignorado) {
            }
        }
    }
}
