package transporte.negocio.fachada.impl;

import static transporte.negocio.fachada.assembler.impl.SolicitudAssembler.convertir;

import transporte.dominio.valor.Pagina;
import transporte.dto.geografia.LugarDTO;
import transporte.dto.geografia.ModificarLugarDTO;
import transporte.dto.geografia.RegistrarLugarDTO;
import transporte.negocio.fachada.Escritura;
import transporte.negocio.fachada.LugarFachada;
import transporte.negocio.fachada.assembler.impl.LugarDTOAssembler;
import transporte.negocio.negocio.LugarNegocio;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LugarFachadaImpl implements LugarFachada {
    private final LugarNegocio negocio;
    private final LugarDTOAssembler lugar;

    public LugarFachadaImpl(LugarNegocio negocio, LugarDTOAssembler lugar) {
        this.negocio = negocio;
        this.lugar = lugar;
    }

    @Override
    @Escritura
    public LugarDTO registrarLugar(RegistrarLugarDTO registrarLugar) {
        return lugar.convertirADTO(negocio.registrarLugar(convertir(registrarLugar)));
    }

    @Override
    @Escritura
    public LugarDTO modificarLugar(UUID id, ModificarLugarDTO modificarLugar) {
        return lugar.convertirADTO(negocio.modificarLugar(id, convertir(modificarLugar)));
    }

    @Override
    @Escritura
    public LugarDTO estadoLugar(UUID id, boolean activo) {
        return lugar.convertirADTO(negocio.estadoLugar(id, activo));
    }

    @Override
    @Escritura
    public void eliminarLugar(UUID id) {
        negocio.eliminarLugar(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public LugarDTO consultarLugar(UUID id) {
        return lugar.convertirADTO(negocio.consultarLugar(id));
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<LugarDTO> buscarLugar(
            @Nullable String nombre,
            @Nullable UUID sectorId,
            @Nullable Boolean activo,
            int numero,
            int tamano) {
        return negocio.buscarLugar(nombre, sectorId, activo, new Pagina(numero, tamano)).stream()
                .map(lugar::convertirADTO)
                .toList();
    }
}
