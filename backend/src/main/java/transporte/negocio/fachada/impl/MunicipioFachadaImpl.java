package transporte.negocio.fachada.impl;

import static transporte.negocio.fachada.assembler.impl.SolicitudAssembler.convertir;

import transporte.dominio.valor.Pagina;
import transporte.dto.geografia.ModificarMunicipioDTO;
import transporte.dto.geografia.MunicipioDTO;
import transporte.dto.geografia.RegistrarMunicipioDTO;
import transporte.negocio.fachada.Escritura;
import transporte.negocio.fachada.MunicipioFachada;
import transporte.negocio.fachada.assembler.impl.MunicipioDTOAssembler;
import transporte.negocio.negocio.MunicipioNegocio;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MunicipioFachadaImpl implements MunicipioFachada {
    private final MunicipioNegocio negocio;
    private final MunicipioDTOAssembler municipio;

    public MunicipioFachadaImpl(MunicipioNegocio negocio, MunicipioDTOAssembler municipio) {
        this.negocio = negocio;
        this.municipio = municipio;
    }

    @Override
    @Escritura
    public MunicipioDTO registrarMunicipio(RegistrarMunicipioDTO registrarMunicipio) {
        return municipio.convertirADTO(negocio.registrarMunicipio(convertir(registrarMunicipio)));
    }

    @Override
    @Escritura
    public MunicipioDTO modificarMunicipio(UUID id, ModificarMunicipioDTO modificarMunicipio) {
        return municipio.convertirADTO(
                negocio.modificarMunicipio(id, convertir(modificarMunicipio)));
    }

    @Override
    @Escritura
    public MunicipioDTO estadoMunicipio(UUID id, boolean activo) {
        return municipio.convertirADTO(negocio.estadoMunicipio(id, activo));
    }

    @Override
    @Escritura
    public void eliminarMunicipio(UUID id) {
        negocio.eliminarMunicipio(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public MunicipioDTO consultarMunicipio(UUID id) {
        return municipio.convertirADTO(negocio.consultarMunicipio(id));
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<MunicipioDTO> buscarMunicipio(
            @Nullable String nombre,
            @Nullable String departamento,
            @Nullable Boolean activo,
            int numero,
            int tamano) {
        return negocio
                .buscarMunicipio(nombre, departamento, activo, new Pagina(numero, tamano))
                .stream()
                .map(municipio::convertirADTO)
                .toList();
    }
}
