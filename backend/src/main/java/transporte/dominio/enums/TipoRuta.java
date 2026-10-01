package transporte.dominio.enums;

import transporte.transversal.mensajes.CatalogoMensajes;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public enum TipoRuta implements ValorCatalogo {
    INTERMUNICIPAL("0546858e-a9ef-57d6-aefc-3002c6ccbe45", "Intermunicipal"),
    URBANA("da662198-1e2f-5810-9943-84c7537f4d7c", "Urbana");
    private final UUID id;
    private final String nombre;

    TipoRuta(String id, String nombre) {
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

    public static TipoRuta desdeId(UUID id) {
        return Arrays.stream(values())
                .filter(valor -> valor.id.equals(id))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        CatalogoMensajes
                                                .EL_IDENTIFICADOR_NO_PERTENECE_AL_CATALOGO_TIPORUTA));
    }

    public static TipoRuta desdeNombre(String nombre) {
        return Arrays.stream(values())
                .filter(valor -> valor.nombre.equals(nombre))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        CatalogoMensajes
                                                .EL_NOMBRE_NO_PERTENECE_AL_CATALOGO_TIPORUTA));
    }

    public static List<TipoRuta> listar() {
        return List.of(values());
    }
}
