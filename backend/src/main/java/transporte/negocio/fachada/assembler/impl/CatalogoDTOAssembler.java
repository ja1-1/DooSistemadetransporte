package transporte.negocio.fachada.assembler.impl;

import transporte.dominio.enums.EstadoSalida;
import transporte.dominio.enums.Rol;
import transporte.dominio.enums.TipoIncidencia;
import transporte.dominio.enums.TipoRuta;
import transporte.dominio.enums.ValorCatalogo;
import transporte.dto.CatalogoDTO;
import transporte.negocio.fachada.assembler.DTOAssembler;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class CatalogoDTOAssembler implements DTOAssembler<ValorCatalogo, CatalogoDTO> {
    @Override
    public CatalogoDTO convertirADTO(ValorCatalogo dominio) {
        return new CatalogoDTO(dominio.getId(), dominio.getNombre());
    }

    @Override
    public ValorCatalogo convertirADominio(CatalogoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.EL_VALOR_DE_CATALOGO_ES_OBLIGATORIO);
        }
        return Stream.of(
                        Rol.values(),
                        EstadoSalida.values(),
                        TipoRuta.values(),
                        TipoIncidencia.values())
                .flatMap(Stream::of)
                .filter(
                        valor ->
                                valor.getId().equals(dto.id())
                                        && valor.getNombre().equals(dto.nombre()))
                .findFirst()
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        CatalogoMensajes.EL_VALOR_NO_CORRESPONDE_A_LOS_CATALOGOS));
    }
}
