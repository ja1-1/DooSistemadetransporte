package transporte.negocio.fachada.impl;

import static transporte.negocio.fachada.assembler.impl.SolicitudAssembler.convertir;
import static transporte.negocio.fachada.assembler.impl.VistaDTOAssembler.convertirADTO;

import transporte.dominio.valor.IncidenciaDeSalida;
import transporte.dto.Comandos.Motivo;
import transporte.dto.Comandos.ProgramarSalida;
import transporte.dto.Comandos.Reemplazar;
import transporte.dto.Comandos.Refuerzo;
import transporte.dto.Comandos.RegistrarIncidencia;
import transporte.dto.Comandos.Reprogramar;
import transporte.dto.IncidenciaDTO;
import transporte.dto.SalidaDTO;
import transporte.negocio.fachada.Escritura;
import transporte.negocio.fachada.SalidaFachada;
import transporte.negocio.negocio.SalidaNegocio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SalidaFachadaImpl implements SalidaFachada {
    private final SalidaNegocio negocio;

    public SalidaFachadaImpl(SalidaNegocio negocio) {
        this.negocio = negocio;
    }

    @Override
    @Escritura
    public SalidaDTO programar(ProgramarSalida programarSalida) {
        return convertirADTO(negocio.programar(convertir(programarSalida)));
    }

    @Override
    @Escritura
    public SalidaDTO iniciar(UUID id) {
        return convertirADTO(negocio.iniciar(id));
    }

    @Override
    @Escritura
    public SalidaDTO finalizarSalida(UUID id) {
        return convertirADTO(negocio.finalizarSalida(id));
    }

    @Override
    @Escritura
    public SalidaDTO cancelar(UUID id, Motivo motivo, UUID actor) {
        return convertirADTO(negocio.cancelar(id, convertir(motivo), actor));
    }

    @Override
    @Escritura
    public SalidaDTO reemplazar(UUID id, Reemplazar reemplazar, UUID actor) {
        return convertirADTO(negocio.reemplazar(id, convertir(reemplazar), actor));
    }

    @Override
    @Escritura
    public SalidaDTO reprogramar(UUID id, Reprogramar reprogramar, UUID actor) {
        return convertirADTO(negocio.reprogramar(id, convertir(reprogramar), actor));
    }

    @Override
    @Escritura
    public SalidaDTO refuerzo(UUID id, Refuerzo refuerzo, UUID actor) {
        return convertirADTO(negocio.refuerzo(id, convertir(refuerzo), actor));
    }

    @Override
    @Escritura
    public IncidenciaDTO incidencia(UUID id, RegistrarIncidencia registrarIncidencia, UUID actor) {
        return convertirADTO(
                new IncidenciaDeSalida(
                        id, negocio.incidencia(id, convertir(registrarIncidencia), actor)));
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<IncidenciaDTO> historial(UUID id) {
        return negocio.historial(id).stream()
                .map(incidencia -> convertirADTO(new IncidenciaDeSalida(id, incidencia)))
                .toList();
    }
}
