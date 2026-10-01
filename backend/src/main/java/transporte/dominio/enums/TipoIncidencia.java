package transporte.dominio.enums;

import transporte.transversal.mensajes.CatalogoMensajes;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public enum TipoIncidencia implements ValorCatalogo {
    RETRASO("23ac0422-5acc-56a1-928f-539aa5f983a7", "Retraso"),
    AVERIA("062a719f-c134-5376-a5fb-401acd4c57b4", "Avería"),
    REEMPLAZO_DE_VEHICULO("0d1e1111-6dda-5258-83c2-3e3465f71120", "Reemplazo de vehículo"),
    CANCELACION("533a1de0-820c-52d4-80bb-7a0554a86916", "Cancelación"),
    REPROGRAMACION("df627dce-fa63-5af4-a65a-69d9e4f0f8e8", "Reprogramación"),
    REFUERZO("a7321fe4-2fa1-53d4-8bd9-f16bc724424a", "Refuerzo"),
    RELEVO_DE_CONDUCTOR("47911191-5a10-5ecd-b1ec-5f8c82e1679f", "Relevo de conductor"),
    OTRO("634d7621-03c9-5d87-8f34-0e80a6438c8c", "Otro");
    private final UUID id;
    private final String nombre;

    TipoIncidencia(String id, String nombre) {
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

    public static TipoIncidencia desdeId(UUID id) {
        return Arrays.stream(values())
                .filter(valor -> valor.id.equals(id))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        CatalogoMensajes
                                                .EL_IDENTIFICADOR_NO_PERTENECE_AL_CATALOGO_TIPOINCIDENCIA));
    }

    public static TipoIncidencia desdeNombre(String nombre) {
        return Arrays.stream(values())
                .filter(valor -> valor.nombre.equals(nombre))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        CatalogoMensajes
                                                .EL_NOMBRE_NO_PERTENECE_AL_CATALOGO_TIPOINCIDENCIA));
    }

    public static List<TipoIncidencia> listar() {
        return List.of(values());
    }
}
