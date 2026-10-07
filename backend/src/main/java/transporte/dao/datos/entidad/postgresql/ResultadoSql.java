package transporte.dao.datos.entidad.postgresql;

import transporte.transversal.mensajes.CatalogoMensajes;

import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataRetrievalFailureException;

final class ResultadoSql {
    private ResultadoSql() {}

    static <T> T requerido(@Nullable T resultado) {
        if (resultado == null) {
            throw new DataRetrievalFailureException(CatalogoMensajes.RESULTADO_SQL_OBLIGATORIO);
        }
        return resultado;
    }
}
