package transporte.negocio.fachada.impl;

import static transporte.negocio.fachada.assembler.impl.SolicitudAssembler.convertir;
import static transporte.negocio.fachada.assembler.impl.VistaDTOAssembler.convertirADTO;

import transporte.dto.Comandos.ModificarConductor;
import transporte.dto.Comandos.RegistrarConductor;
import transporte.dto.ConductorDTO;
import transporte.negocio.fachada.ConductorFachada;
import transporte.negocio.fachada.Escritura;
import transporte.negocio.negocio.ConductorNegocio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ConductorFachadaImpl implements ConductorFachada {
    private final ConductorNegocio negocio;

    public ConductorFachadaImpl(ConductorNegocio negocio) {
        this.negocio = negocio;
    }

    @Override
    @Escritura
    public ConductorDTO registrar(RegistrarConductor registrarConductor) {
        return convertirADTO(negocio.registrar(convertir(registrarConductor)));
    }

    @Override
    @Escritura
    public ConductorDTO modificarConductor(UUID id, ModificarConductor modificarConductor) {
        return convertirADTO(negocio.modificar(id, convertir(modificarConductor)));
    }

    @Override
    @Escritura
    public ConductorDTO estadoConductor(UUID id, boolean activo) {
        return convertirADTO(negocio.estadoConductor(id, activo));
    }

    @Override
    @Escritura
    public void eliminarConductor(UUID id) {
        negocio.eliminarConductor(id);
    }
}
