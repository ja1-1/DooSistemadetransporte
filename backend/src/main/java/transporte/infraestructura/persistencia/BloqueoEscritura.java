package transporte.infraestructura.persistencia;

import transporte.transversal.mensajes.CatalogoMensajes;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class BloqueoEscritura {
    private final JdbcTemplate jdbc;

    public BloqueoEscritura(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void adquirir() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException(CatalogoMensajes.LA_ESCRITURA_NECESITA_UNA_TRANSACCION);
        }
        jdbc.execute("SELECT pg_advisory_xact_lock(849201425)");
    }
}
