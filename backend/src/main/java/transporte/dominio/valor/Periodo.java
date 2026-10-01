package transporte.dominio.valor;

import transporte.transversal.mensajes.CatalogoMensajes;

import java.time.LocalDate;
import java.util.Optional;

public record Periodo(LocalDate inicio, Optional<LocalDate> fin) {
    public Periodo {
        if (inicio == null
                || fin == null
                || fin.filter(fecha -> fecha.isBefore(inicio)).isPresent()) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.EL_PERIODO_REQUIERE_INICIO_Y_UN_FIN);
        }
    }

    public boolean estaVigenteEn(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException(CatalogoMensajes.LA_FECHA_ES_OBLIGATORIA);
        }
        return !fecha.isBefore(inicio) && fin.map(limite -> !fecha.isAfter(limite)).orElse(true);
    }

    public boolean seCruzaCon(Periodo otro) {
        if (otro == null) {
            throw new IllegalArgumentException(CatalogoMensajes.EL_PERIODO_ES_OBLIGATORIO);
        }
        return fin.map(limite -> !otro.inicio.isAfter(limite)).orElse(true)
                && otro.fin.map(limite -> !inicio.isAfter(limite)).orElse(true);
    }

    public boolean contiene(Periodo otro) {
        if (otro == null) {
            throw new IllegalArgumentException(CatalogoMensajes.EL_PERIODO_ES_OBLIGATORIO);
        }
        return !otro.inicio.isBefore(inicio)
                && (fin.isEmpty()
                        || otro.fin
                                .filter(limite -> !limite.isAfter(fin.orElseThrow()))
                                .isPresent());
    }

    public Periodo cerrarEn(LocalDate fechaFin) {
        if (fechaFin == null) {
            throw new IllegalArgumentException(CatalogoMensajes.LA_FECHA_DE_FIN_ES_OBLIGATORIA);
        }
        return new Periodo(inicio, Optional.of(fechaFin));
    }
}
