package transporte.infraestructura.seguridad;

import static transporte.transversal.utilitarios.Validacion.requerido;

import transporte.dominio.puerto.CifradorContrasena;
import transporte.dominio.valor.HashContrasena;

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CifradorContrasenaImpl implements CifradorContrasena {
    private static final String CARACTERES =
            "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!#%+-";
    private final SecureRandom aleatorio = new SecureRandom();
    private final Pbkdf2PasswordEncoder encoder =
            Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    @Override
    public HashContrasena cifrar(String contrasena) {
        return new HashContrasena(encoder.encode(requerido(contrasena, "contrasena")));
    }

    @Override
    public boolean coincide(String contrasena, HashContrasena hash) {
        return contrasena != null && encoder.matches(contrasena, requerido(hash, "hash").valor());
    }

    @Override
    public String generarContrasenaTemporal() {
        var temporal = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            temporal.append(CARACTERES.charAt(aleatorio.nextInt(CARACTERES.length())));
        }
        return temporal.toString();
    }
}
