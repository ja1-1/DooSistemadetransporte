package transporte.dominio.enums;

import transporte.transversal.mensajes.CatalogoMensajes;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public enum Rol implements ValorCatalogo {
    ADMINISTRADOR("32cea724-fe09-5e9e-925d-6f2290a1c018", "Administrador"),
    JEFE_DE_OPERACION("f7bfe424-c3a7-520d-ab73-336fbfd1e286", "Jefe de operación"),
    CONDUCTOR("4e4fe28b-c84c-5c85-b935-2c0a99fec851", "Conductor");
    private final UUID id;
    private final String nombre;

    Rol(String id, String nombre) {
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

    public static Rol desdeId(UUID id) {
        return Arrays.stream(values())
                .filter(valor -> valor.id.equals(id))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        CatalogoMensajes
                                                .EL_IDENTIFICADOR_NO_PERTENECE_AL_CATALOGO_ROL));
    }

    public static Rol desdeNombre(String nombre) {
        return Arrays.stream(values())
                .filter(valor -> valor.nombre.equals(nombre))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        CatalogoMensajes.EL_NOMBRE_NO_PERTENECE_AL_CATALOGO_ROL));
    }

    public static List<Rol> listar() {
        return List.of(values());
    }
}
