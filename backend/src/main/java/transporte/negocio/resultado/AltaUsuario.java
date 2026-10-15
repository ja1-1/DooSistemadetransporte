package transporte.negocio.resultado;

import transporte.dominio.UsuarioDominio;

public record AltaUsuario(UsuarioDominio usuario, String contrasenaTemporal) {
    @Override
    public String toString() {
        return "Alta[credencial protegida]";
    }
}
