package transporte.negocio.fachada.impl;

import static transporte.negocio.fachada.assembler.impl.SolicitudAssembler.convertir;
import static transporte.negocio.fachada.assembler.impl.VistaDTOAssembler.convertirADTO;

import transporte.dto.Comandos.ModificarVehiculo;
import transporte.dto.Comandos.RegistrarVehiculo;
import transporte.dto.VehiculoDTO;
import transporte.negocio.fachada.Escritura;
import transporte.negocio.fachada.VehiculoFachada;
import transporte.negocio.negocio.VehiculoNegocio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class VehiculoFachadaImpl implements VehiculoFachada {
    private final VehiculoNegocio negocio;

    public VehiculoFachadaImpl(VehiculoNegocio negocio) {
        this.negocio = negocio;
    }

    @Override
    @Escritura
    public VehiculoDTO registrar(RegistrarVehiculo registrarVehiculo) {
        return convertirADTO(negocio.registrar(convertir(registrarVehiculo)));
    }

    @Override
    @Escritura
    public VehiculoDTO modificarVehiculo(UUID id, ModificarVehiculo modificarVehiculo) {
        return convertirADTO(negocio.modificar(id, convertir(modificarVehiculo)));
    }

    @Override
    @Escritura
    public VehiculoDTO estadoVehiculo(UUID id, boolean activo) {
        return convertirADTO(negocio.estadoVehiculo(id, activo));
    }

    @Override
    @Escritura
    public void eliminarVehiculo(UUID id) {
        negocio.eliminarVehiculo(id);
    }
}
