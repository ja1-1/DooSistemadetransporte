package transporte.dominio.valor;

import transporte.dominio.Incidencia;

import java.util.UUID;

public record IncidenciaDeSalida(UUID salidaId, Incidencia incidencia) {}
