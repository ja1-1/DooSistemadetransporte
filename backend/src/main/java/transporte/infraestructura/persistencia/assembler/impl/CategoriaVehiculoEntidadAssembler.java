package transporte.infraestructura.persistencia.assembler.impl;

import transporte.dominio.CategoriaVehiculoDominio;
import transporte.dominio.enums.CategoriaLicencia;
import transporte.entidad.CategoriaVehiculoEntidad;

import org.springframework.stereotype.Component;

@Component
public class CategoriaVehiculoEntidadAssembler {
    public CategoriaVehiculoEntidad convertirAEntidad(CategoriaVehiculoDominio dominio) {
        return new CategoriaVehiculoEntidad(
                dominio.getId(), dominio.getNombre(), dominio.getCategoriaLicenciaMinima().name());
    }

    public CategoriaVehiculoDominio convertirADominio(CategoriaVehiculoEntidad entidad) {
        return new CategoriaVehiculoDominio(
                entidad.id(),
                entidad.nombre(),
                CategoriaLicencia.desdeTexto(entidad.categoriaLicenciaMinima()));
    }
}
