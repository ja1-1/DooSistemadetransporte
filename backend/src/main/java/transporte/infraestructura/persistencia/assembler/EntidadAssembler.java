package transporte.infraestructura.persistencia.assembler;

import java.util.List;

public interface EntidadAssembler<D, E> {
    E convertirAEntidad(D dominio);

    D convertirADominio(E entidad);

    default List<D> convertirADominio(List<E> entidades) {
        return entidades.stream().map(this::convertirADominio).toList();
    }
}
