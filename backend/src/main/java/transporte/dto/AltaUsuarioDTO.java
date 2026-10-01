package transporte.dto;

public record AltaUsuarioDTO(UsuarioDTO usuario, String contrasenaTemporal) {
    @Override
    public String toString() {
        return "AltaUsuarioDTO[usuario=" + usuario + ", contrasenaTemporal=PROTEGIDA]";
    }
}
