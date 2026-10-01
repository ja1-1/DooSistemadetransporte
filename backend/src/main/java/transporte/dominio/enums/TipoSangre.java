package transporte.dominio.enums;

import transporte.transversal.mensajes.CatalogoMensajes;

import java.util.Arrays;

public enum TipoSangre {
    A_POSITIVO("A+"),
    A_NEGATIVO("A-"),
    B_POSITIVO("B+"),
    B_NEGATIVO("B-"),
    AB_POSITIVO("AB+"),
    AB_NEGATIVO("AB-"),
    O_POSITIVO("O+"),
    O_NEGATIVO("O-");
    private final String simbolo;

    TipoSangre(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public static TipoSangre desdeSimbolo(String simbolo) {
        return Arrays.stream(values())
                .filter(valor -> valor.simbolo.equals(simbolo))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        CatalogoMensajes.EL_TIPO_DE_SANGRE_NO_ES_VALIDO));
    }
}
