package transporte.dominio.enums;

import transporte.transversal.mensajes.CatalogoMensajes;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public enum EstadoSalida implements ValorCatalogo {
    PROGRAMADA("581accea-3852-54fd-b783-ce22cb23887c", "Programada"),
    EN_CURSO("e8b053b0-0044-545c-afee-9653c6d3fab3", "En curso"),
    FINALIZADA("fdac84c6-19b0-5bc5-92d9-d4823edec4f5", "Finalizada"),
    CANCELADA("c8c28d15-f60c-550f-86d9-f079e11061d6", "Cancelada");
    private final UUID id;
    private final String nombre;

    EstadoSalida(String id, String nombre) {
        this.id = UUID.fromString(id);
        this.nombre = nombre;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    public static EstadoSalida desdeId(UUID id) {
        return Arrays.stream(values())
                .filter(valor -> valor.id.equals(id))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        CatalogoMensajes
                                                .EL_IDENTIFICADOR_NO_PERTENECE_AL_CATALOGO_ESTADOSALIDA));
    }

    public static EstadoSalida desdeNombre(String nombre) {
        return Arrays.stream(values())
                .filter(valor -> valor.nombre.equals(nombre))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        CatalogoMensajes
                                                .EL_NOMBRE_NO_PERTENECE_AL_CATALOGO_ESTADOSALIDA));
    }

    public static List<EstadoSalida> listar() {
        return List.of(values());
    }

    public boolean esDefinitivo() {
        return this == FINALIZADA || this == CANCELADA;
    }
}
