package transporte.dao.datos;

import java.util.Optional;

public interface ConsultarDAO<E, ID> {
    Optional<E> buscarPorId(ID id);
}
