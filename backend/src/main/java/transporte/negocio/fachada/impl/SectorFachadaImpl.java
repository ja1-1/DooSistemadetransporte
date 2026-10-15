package transporte.negocio.fachada.impl;

import static transporte.negocio.fachada.assembler.impl.SolicitudAssembler.convertir;

import transporte.dominio.valor.Pagina;
import transporte.dto.geografia.ModificarSectorDTO;
import transporte.dto.geografia.RegistrarSectorDTO;
import transporte.dto.geografia.SectorDTO;
import transporte.negocio.fachada.Escritura;
import transporte.negocio.fachada.SectorFachada;
import transporte.negocio.fachada.assembler.impl.SectorDTOAssembler;
import transporte.negocio.negocio.SectorNegocio;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SectorFachadaImpl implements SectorFachada {
    private final SectorNegocio negocio;
    private final SectorDTOAssembler sector;

    public SectorFachadaImpl(SectorNegocio negocio, SectorDTOAssembler sector) {
        this.negocio = negocio;
        this.sector = sector;
    }

    @Override
    @Escritura
    public SectorDTO registrarSector(RegistrarSectorDTO registrarSector) {
        return sector.convertirADTO(negocio.registrarSector(convertir(registrarSector)));
    }

    @Override
    @Escritura
    public SectorDTO modificarSector(UUID id, ModificarSectorDTO modificarSector) {
        return sector.convertirADTO(negocio.modificarSector(id, convertir(modificarSector)));
    }

    @Override
    @Escritura
    public SectorDTO estadoSector(UUID id, boolean activo) {
        return sector.convertirADTO(negocio.estadoSector(id, activo));
    }

    @Override
    @Escritura
    public void eliminarSector(UUID id) {
        negocio.eliminarSector(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public SectorDTO consultarSector(UUID id) {
        return sector.convertirADTO(negocio.consultarSector(id));
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<SectorDTO> buscarSector(
            @Nullable String nombre,
            @Nullable UUID municipioId,
            @Nullable Boolean activo,
            int numero,
            int tamano) {
        return negocio
                .buscarSector(nombre, municipioId, activo, new Pagina(numero, tamano))
                .stream()
                .map(sector::convertirADTO)
                .toList();
    }
}
