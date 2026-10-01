package transporte.dominio.valor;

import org.jspecify.annotations.Nullable;

import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public final class Recurrencias {
    private static final LocalDate BASE = LocalDate.of(2026, 10, 26);
    private static final BigInteger DIA = BigInteger.valueOf(86_400_000_000_000L);
    private static final BigInteger NANOS_POR_MINUTO = BigInteger.valueOf(60_000_000_000L);
    private static final long MINUTOS_POR_DIA = 1440;

    private Recurrencias() {}

    private static long piso(BigInteger bigInteger) {
        var cocienteYResto = bigInteger.divideAndRemainder(DIA);
        boolean ajustarHaciaAbajo = bigInteger.signum() < 0 && cocienteYResto[1].signum() != 0;
        return cocienteYResto[0].longValue() + (ajustarHaciaAbajo ? -1 : 0);
    }

    private static long diasQueAbarca(long minutos) {
        return Math.ceilDiv(minutos, MINUTOS_POR_DIA);
    }

    private static long nanosDelDia(LocalDateTime instante) {
        return instante.toLocalTime().toNanoOfDay();
    }

    private static long @Nullable [] limites(Periodo periodo, DayOfWeek dia) {
        var inicio = periodo.inicio().with(TemporalAdjusters.nextOrSame(dia));
        var fin = periodo.fin().orElse(LocalDate.MAX);
        if (inicio.isAfter(fin)) {
            return null;
        }
        fin = fin.with(TemporalAdjusters.previousOrSame(dia));
        return new long[] {inicio.toEpochDay(), fin.toEpochDay()};
    }

    public static boolean seCruzan(
            HorarioProgramado horarioProgramado,
            Periodo pa,
            HorarioProgramado horarioProgramadoRecibido,
            Periodo pb,
            boolean agenda) {
        for (var da : horarioProgramado.diasSemana()) {
            for (var db : horarioProgramadoRecibido.diasSemana()) {
                var la = limites(pa, da);
                var lb = limites(pb, db);
                if (la == null || lb == null) {
                    continue;
                }
                long extensionA = agenda ? diasQueAbarca(horarioProgramado.duracionEstimada()) : 0;
                long extensionB =
                        agenda ? diasQueAbarca(horarioProgramadoRecibido.duracionEstimada()) : 0;
                if (lb[0] > la[1] + extensionA || la[0] > lb[1] + extensionB) {
                    continue;
                }
                var va =
                        horarioProgramado.calcularVentanas(
                                BASE.with(TemporalAdjusters.nextOrSame(da)));
                var vb =
                        horarioProgramadoRecibido.calcularVentanas(
                                BASE.with(TemporalAdjusters.nextOrSame(db)));
                for (var propia : va) {
                    for (var ajena : vb) {
                        long delta =
                                nanosDelDia(propia.instanteSalidaProgramado())
                                        - nanosDelDia(ajena.instanteSalidaProgramado());
                        long inferior = lb[0] - la[1], superior = lb[1] - la[0];
                        if (agenda) {
                            var diferencia = BigInteger.valueOf(delta);
                            var durA =
                                    BigInteger.valueOf(horarioProgramado.duracionEstimada())
                                            .multiply(NANOS_POR_MINUTO);
                            var durB =
                                    BigInteger.valueOf(horarioProgramadoRecibido.duracionEstimada())
                                            .multiply(NANOS_POR_MINUTO);
                            inferior = Math.max(inferior, piso(diferencia.subtract(durB)) + 1);
                            superior =
                                    Math.min(
                                            superior,
                                            piso(diferencia.add(durA).subtract(BigInteger.ONE)));
                        } else {
                            if (delta != 0) {
                                continue;
                            }
                            inferior = Math.max(inferior, 0);
                            superior = Math.min(superior, 0);
                        }
                        long primer = inferior + Math.floorMod((lb[0] - la[0]) - inferior, 7);
                        if (primer <= superior) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
