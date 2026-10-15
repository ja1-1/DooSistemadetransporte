package transporte.negocio.fachada.assembler;

import java.util.List;

public interface DTOAssembler<D, T> {
    T convertirADTO(D dominio);

    D convertirADominio(T dto);

    default List<T> convertirADTO(List<D> dominios) {
        return dominios.stream().map(this::convertirADTO).toList();
    }
}
