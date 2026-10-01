package transporte.dominio.valor;

import transporte.dominio.enums.CategoriaLicencia;
import transporte.transversal.utilitarios.Validacion;

import java.time.LocalDate;

public record Licencia(String numero, CategoriaLicencia categoria, LocalDate fechaVencimiento) {
    public Licencia {
        numero = Validacion.texto(numero, "El número de licencia", 5, 20);
        Validacion.requerido(categoria, "La categoría de licencia");
        Validacion.requerido(fechaVencimiento, "La fecha de vencimiento");
    }

    public boolean estaVigenteEn(LocalDate fecha) {
        return !Validacion.requerido(fecha, "La fecha").isAfter(fechaVencimiento);
    }

    public boolean cubre(CategoriaLicencia minima) {
        return categoria.cubre(Validacion.requerido(minima, "La licencia mínima"));
    }
}
