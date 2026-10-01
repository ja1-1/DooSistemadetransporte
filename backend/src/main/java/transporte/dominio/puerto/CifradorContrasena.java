package transporte.dominio.puerto;

import transporte.dominio.valor.HashContrasena;

public interface CifradorContrasena {
    HashContrasena cifrar(String contrasena);

    boolean coincide(String contrasena, HashContrasena hash);

    String generarContrasenaTemporal();
}
