package transporte.transversal.utilitarios;

import transporte.transversal.mensajes.CatalogoMensajes;

import org.jspecify.annotations.Nullable;
import org.springframework.lang.Contract;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public final class Validacion {
    private Validacion() {}

    @Contract("null, _ -> fail")
    public static <T> T requerido(@Nullable T valor, String campo) {
        if (valor == null) {
            throw new IllegalArgumentException(campo + CatalogoMensajes.ES_OBLIGATORIO);
        }
        return valor;
    }

    public static LocalDateTime instante(@Nullable LocalDateTime valor, String campo) {
        return requerido(valor, campo).truncatedTo(ChronoUnit.MICROS);
    }

    public static String texto(@Nullable String valor, String campo, int minimo, int maximo) {
        String limpio = requerido(valor, campo).strip();
        int longitud = limpio.codePointCount(0, limpio.length());
        if (longitud < minimo
                || longitud > maximo
                || limpio.codePoints().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException(
                    campo
                            + " debe tener entre "
                            + minimo
                            + CatalogoMensajes.Y
                            + maximo
                            + CatalogoMensajes.CARACTERES_VALIDOS);
        }
        return limpio;
    }

    public static String opcional(@Nullable String valor, String campo, int maximo) {
        return texto(valor == null ? "" : valor, campo, 0, maximo);
    }

    public static <T> Optional<T> opcional(@Nullable Optional<T> valor, String campo) {
        return requerido(valor, campo);
    }

    public static int rango(int valor, String campo, int minimo, int maximo) {
        if (valor < minimo || valor > maximo) {
            throw new IllegalArgumentException(campo + CatalogoMensajes.ESTA_FUERA_DE_RANGO);
        }
        return valor;
    }

    public static void estado(boolean permitido, String mensaje) {
        if (!permitido) {
            throw new IllegalStateException(mensaje);
        }
    }
}
