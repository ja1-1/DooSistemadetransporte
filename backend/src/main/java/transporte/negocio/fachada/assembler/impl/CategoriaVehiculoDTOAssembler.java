package transporte.negocio.fachada.assembler.impl;

import transporte.dominio.CategoriaVehiculoDominio;
import transporte.dto.flota.CategoriaVehiculoDTO;

import org.springframework.stereotype.Component;

@Component
public class CategoriaVehiculoDTOAssembler {
    public CategoriaVehiculoDTO convertirADTO(CategoriaVehiculoDominio dominio) {
        return new CategoriaVehiculoDTO(
                dominio.getId(), dominio.getNombre(), dominio.getCategoriaLicenciaMinima().name());
    }
}
