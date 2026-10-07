package transporte.dao.datos.entidad.postgresql;

import transporte.dominio.valor.Pagina;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

final class CondicionesSql {
    private final StringBuilder texto = new StringBuilder(" WHERE TRUE");
    private final List<Object> parametros = new ArrayList<>();

    CondicionesSql igual(String columna, @Nullable Object valor) {
        if (valor != null) {
            texto.append(" AND ").append(columna).append(" = ?");
            parametros.add(valor);
        }
        return this;
    }

    CondicionesSql agregar(String condicion, Object... valores) {
        texto.append(" AND ").append(condicion);
        parametros.addAll(Arrays.asList(valores));
        return this;
    }

    CondicionesSql desde(String columna, @Nullable Object valor) {
        if (valor != null) {
            agregar(columna + " >= ?", valor);
        }
        return this;
    }

    CondicionesSql antes(String columna, @Nullable Object valor) {
        if (valor != null) {
            agregar(columna + " < ?", valor);
        }
        return this;
    }

    CondicionesSql en(String columna, @Nullable Collection<UUID> ids) {
        if (ids != null) {
            if (ids.isEmpty()) {
                agregar("FALSE");
            } else {
                texto.append(" AND ")
                        .append(columna)
                        .append(" IN (")
                        .append(String.join(",", Collections.nCopies(ids.size(), "?")))
                        .append(")");
                parametros.addAll(ids);
            }
        }
        return this;
    }

    String texto() {
        return texto.toString();
    }

    Object[] parametros() {
        return parametros.toArray();
    }

    String paginar(@Nullable Pagina pagina) {
        if (pagina == null) {
            return "";
        }
        parametros.add(pagina.tamano());
        parametros.add(pagina.desplazamiento());
        return " LIMIT ? OFFSET ?";
    }
}
