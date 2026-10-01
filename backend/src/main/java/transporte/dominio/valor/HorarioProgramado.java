package transporte.dominio.valor;

import transporte.transversal.mensajes.CatalogoMensajes;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record HorarioProgramado(
        LocalTime horaSalida,
        LocalTime horaUltimaSalida,
        int intervaloMinutos,
        int duracionEstimada,
        Set<DayOfWeek> diasSemana) {
    public HorarioProgramado {
        if (horaSalida != null) {
            horaSalida = horaSalida.truncatedTo(ChronoUnit.MICROS);
        }
        if (horaUltimaSalida != null) {
            horaUltimaSalida = horaUltimaSalida.truncatedTo(ChronoUnit.MICROS);
        }
        if (horaSalida == null
                || horaUltimaSalida == null
                || diasSemana == null
                || diasSemana.isEmpty()
                || diasSemana.stream().anyMatch(Objects::isNull)
                || horaUltimaSalida.isBefore(horaSalida)
                || intervaloMinutos < 1
                || intervaloMinutos > 720
                || duracionEstimada < 1) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.REVISA_LA_FRANJA_EL_INTERVALO_LA_DURACION);
        }
        long franja = Duration.between(horaSalida, horaUltimaSalida).toNanos();
        if (franja % Duration.ofMinutes(intervaloMinutos).toNanos() != 0
                || (franja > 0 && intervaloMinutos < duracionEstimada)) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.LA_ULTIMA_SALIDA_DEBE_PERTENECER_AL_INTERVALO);
        }
        diasSemana = Set.copyOf(diasSemana);
    }

    public boolean operaEl(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException(CatalogoMensajes.LA_FECHA_ES_OBLIGATORIA);
        }
        return diasSemana.contains(fecha.getDayOfWeek());
    }

    public List<VentanaViaje> calcularVentanas(LocalDate fecha) {
        if (!operaEl(fecha)) {
            return List.of();
        }
        var ventanas = new ArrayList<VentanaViaje>();
        long minutos = Duration.between(horaSalida, horaUltimaSalida).toMinutes();
        for (long desplazamiento = 0;
                desplazamiento <= minutos;
                desplazamiento += intervaloMinutos) {
            var salida = fecha.atTime(horaSalida).plusMinutes(desplazamiento);
            ventanas.add(new VentanaViaje(salida, salida.plusMinutes(duracionEstimada)));
        }
        return List.copyOf(ventanas);
    }

    public boolean seSolapaCon(HorarioProgramado otro) {
        if (otro == null) {
            throw new IllegalArgumentException(CatalogoMensajes.EL_HORARIO_ES_OBLIGATORIO);
        }
        if (duracionEstimada >= 7 * 24 * 60 || otro.duracionEstimada >= 7 * 24 * 60) {
            return true;
        }
        var inicioSemana = LocalDate.of(2026, 10, 26);
        var propias = new ArrayList<VentanaViaje>();
        var ajenas = new ArrayList<VentanaViaje>();
        for (int dia = -7; dia < 14; dia++) {
            var fecha = inicioSemana.plusDays(dia);
            ajenas.addAll(otro.calcularVentanas(fecha));
            if (dia >= 0 && dia < 7) {
                propias.addAll(calcularVentanas(fecha));
            }
        }
        int izquierda = 0;
        int derecha = 0;
        while (izquierda < propias.size() && derecha < ajenas.size()) {
            var propia = propias.get(izquierda);
            var ajena = ajenas.get(derecha);
            if (propia.seSolapaCon(ajena)) {
                return true;
            }
            if (!propia.instanteLlegadaEstimado().isAfter(ajena.instanteSalidaProgramado())) {
                izquierda++;
            } else {
                derecha++;
            }
        }
        return false;
    }
}
