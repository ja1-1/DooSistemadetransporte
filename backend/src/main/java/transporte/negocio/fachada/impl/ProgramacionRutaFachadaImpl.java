package transporte.negocio.fachada.impl;

import static transporte.negocio.fachada.assembler.impl.SolicitudAssembler.convertir;
import static transporte.negocio.fachada.assembler.impl.VistaDTOAssembler.convertirADTO;

import transporte.dto.CambioProgramacionDTO;
import transporte.dto.Comandos.GenerarSalidas;
import transporte.dto.Comandos.ModificarProgramacion;
import transporte.dto.Comandos.RegistrarProgramacion;
import transporte.dto.GeneracionSalidasDTO;
import transporte.dto.ProgramacionRutaDTO;
import transporte.negocio.fachada.Escritura;
import transporte.negocio.fachada.ProgramacionRutaFachada;
import transporte.negocio.negocio.ProgramacionRutaNegocio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ProgramacionRutaFachadaImpl implements ProgramacionRutaFachada {
    private final ProgramacionRutaNegocio negocio;

    public ProgramacionRutaFachadaImpl(ProgramacionRutaNegocio negocio) {
        this.negocio = negocio;
    }

    @Override
    @Escritura
    public ProgramacionRutaDTO registrarProgramacion(RegistrarProgramacion registrarProgramacion) {
        return convertirADTO(negocio.registrar(convertir(registrarProgramacion)));
    }

    @Override
    @Escritura
    public ProgramacionRutaDTO estadoProgramacion(UUID id, boolean activa) {
        return convertirADTO(negocio.estadoProgramacion(id, activa));
    }

    @Override
    @Escritura
    public GeneracionSalidasDTO generar(UUID id, GenerarSalidas generarSalidas) {
        var resultado = negocio.generar(id, convertir(generarSalidas));
        return new GeneracionSalidasDTO(
                resultado.salidasGeneradas().stream().map(salida -> convertirADTO(salida)).toList(),
                resultado.salidasExistentes().stream()
                        .map(salidaRecibido -> convertirADTO(salidaRecibido))
                        .toList(),
                resultado.rechazos());
    }

    @Override
    @Escritura
    public CambioProgramacionDTO modificarProgramacion(
            UUID id, ModificarProgramacion modificarProgramacion, UUID actor) {
        var resultado = negocio.modificar(id, convertir(modificarProgramacion), actor);
        return new CambioProgramacionDTO(
                convertirADTO(resultado.programacion()),
                resultado.salidasSinCambiar().stream()
                        .map(salida -> convertirADTO(salida))
                        .toList());
    }
}
