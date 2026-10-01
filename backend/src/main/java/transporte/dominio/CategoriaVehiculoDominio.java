package transporte.dominio;

import transporte.dominio.enums.CategoriaLicencia;
import transporte.transversal.mensajes.CatalogoMensajes;
import transporte.transversal.utilitarios.Validacion;

import java.util.UUID;

public final class CategoriaVehiculoDominio {
    private final UUID id;
    private String nombre;
    private CategoriaLicencia categoriaLicenciaMinima;

    public CategoriaVehiculoDominio(
            UUID id, String nombre, CategoriaLicencia categoriaLicenciaMinima) {
        this.id = Validacion.requerido(id, "El identificador es obligatorio.");
        this.nombre = normalizarNombre(nombre);
        this.categoriaLicenciaMinima =
                Validacion.requerido(categoriaLicenciaMinima, "La licencia mínima es obligatoria.");
    }

    public static String normalizarNombre(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.EL_NOMBRE_DE_LA_CATEGORIA_ES_OBLIGATORIO);
        }
        String valor = nombre.strip();
        int longitud = valor.codePointCount(0, valor.length());
        if (longitud < 1 || longitud > 40) {
            throw new IllegalArgumentException(CatalogoMensajes.EL_NOMBRE_DEBE_TENER_ENTRE_1_Y);
        }
        if (valor.codePoints().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.EL_NOMBRE_CONTIENE_CARACTERES_DE_CONTROL_NO);
        }
        return valor;
    }

    public static CategoriaVehiculoDominio registrar(
            String nombre, CategoriaLicencia categoriaLicenciaMinima) {
        return new CategoriaVehiculoDominio(UUID.randomUUID(), nombre, categoriaLicenciaMinima);
    }

    public void modificar(String nombre, CategoriaLicencia categoriaLicenciaMinima) {
        String nombreNormalizado = normalizarNombre(nombre);
        Validacion.requerido(categoriaLicenciaMinima, "categoriaLicenciaMinima");
        this.nombre = nombreNormalizado;
        this.categoriaLicenciaMinima = categoriaLicenciaMinima;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public CategoriaLicencia getCategoriaLicenciaMinima() {
        return categoriaLicenciaMinima;
    }
}
