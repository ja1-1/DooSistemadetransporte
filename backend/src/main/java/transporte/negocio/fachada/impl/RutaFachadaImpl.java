package transporte.negocio.fachada.impl;

import static transporte.negocio.fachada.assembler.impl.SolicitudAssembler.convertir;
import static transporte.negocio.fachada.assembler.impl.VistaDTOAssembler.convertirADTO;

import transporte.dto.Comandos.CambiarRecorrido;
import transporte.dto.Comandos.ModificarRuta;
import transporte.dto.Comandos.RegistrarRuta;
import transporte.dto.RutaDTO;
import transporte.negocio.fachada.Escritura;
import transporte.negocio.fachada.RutaFachada;
import transporte.negocio.negocio.RutaNegocio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class RutaFachadaImpl implements RutaFachada {
    private final RutaNegocio negocio;

    public RutaFachadaImpl(RutaNegocio rutaNegocio) {
        negocio = rutaNegocio;
    }

    @Override
    @Escritura
    public RutaDTO registrar(RegistrarRuta registrarRuta) {
        return convertirADTO(negocio.registrar(convertir(registrarRuta)));
    }

    @Override
    @Escritura
    public RutaDTO modificar(UUID id, ModificarRuta modificarRuta) {
        return convertirADTO(negocio.modificar(id, convertir(modificarRuta)));
    }

    @Override
    @Escritura
    public RutaDTO recorrido(UUID id, CambiarRecorrido cambiarRecorrido) {
        return convertirADTO(negocio.recorrido(id, convertir(cambiarRecorrido)));
    }

    @Override
    @Escritura
    public RutaDTO estado(UUID id, boolean activo) {
        return convertirADTO(negocio.estadoRuta(id, activo));
    }

    @Override
    @Escritura
    public void eliminar(UUID id) {
        negocio.eliminar(id);
    }
}
