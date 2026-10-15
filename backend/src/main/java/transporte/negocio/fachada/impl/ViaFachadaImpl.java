package transporte.negocio.fachada.impl;

import static transporte.negocio.fachada.assembler.impl.SolicitudAssembler.convertir;

import transporte.dominio.valor.Pagina;
import transporte.dto.geografia.ModificarViaDTO;
import transporte.dto.geografia.RegistrarViaDTO;
import transporte.dto.geografia.ViaDTO;
import transporte.negocio.fachada.Escritura;
import transporte.negocio.fachada.ViaFachada;
import transporte.negocio.fachada.assembler.impl.ViaDTOAssembler;
import transporte.negocio.negocio.ViaNegocio;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ViaFachadaImpl implements ViaFachada {
    private final ViaNegocio negocio;
    private final ViaDTOAssembler via;

    public ViaFachadaImpl(ViaNegocio negocio, ViaDTOAssembler via) {
        this.negocio = negocio;
        this.via = via;
    }

    @Override
    @Escritura
    public ViaDTO registrarVia(RegistrarViaDTO registrarVia) {
        return via.convertirADTO(negocio.registrarVia(convertir(registrarVia)));
    }

    @Override
    @Escritura
    public ViaDTO modificarVia(UUID id, ModificarViaDTO modificarVia) {
        return via.convertirADTO(negocio.modificarVia(id, convertir(modificarVia)));
    }

    @Override
    @Escritura
    public ViaDTO estadoVia(UUID id, boolean activo) {
        return via.convertirADTO(negocio.estadoVia(id, activo));
    }

    @Override
    @Escritura
    public void eliminarVia(UUID id) {
        negocio.eliminarVia(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public ViaDTO consultarVia(UUID id) {
        return via.convertirADTO(negocio.consultarVia(id));
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<ViaDTO> buscarVia(
            @Nullable String nombre,
            @Nullable String descripcion,
            @Nullable Boolean activo,
            int numero,
            int tamano) {
        return negocio.buscarVia(nombre, descripcion, activo, new Pagina(numero, tamano)).stream()
                .map(via::convertirADTO)
                .toList();
    }
}
