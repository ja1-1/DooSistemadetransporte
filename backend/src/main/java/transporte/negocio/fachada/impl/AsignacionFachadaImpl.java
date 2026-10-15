package transporte.negocio.fachada.impl;

import static transporte.negocio.fachada.assembler.impl.SolicitudAssembler.convertir;
import static transporte.negocio.fachada.assembler.impl.VistaDTOAssembler.convertirADTO;

import transporte.dto.AsignacionDTO;
import transporte.dto.Comandos.Asignar;
import transporte.dto.Comandos.FinalizarAsignacion;
import transporte.dto.Comandos.Relevar;
import transporte.negocio.fachada.AsignacionFachada;
import transporte.negocio.fachada.Escritura;
import transporte.negocio.negocio.AsignacionNegocio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AsignacionFachadaImpl implements AsignacionFachada {
    private final AsignacionNegocio negocio;

    public AsignacionFachadaImpl(AsignacionNegocio negocio) {
        this.negocio = negocio;
    }

    @Override
    @Escritura
    public AsignacionDTO asignar(Asignar asignar) {
        return convertirADTO(negocio.asignar(convertir(asignar)));
    }

    @Override
    @Escritura
    public AsignacionDTO finalizarAsignacion(UUID id, FinalizarAsignacion finalizarAsignacion) {
        return convertirADTO(negocio.finalizar(id, convertir(finalizarAsignacion)));
    }

    @Override
    @Escritura
    public AsignacionDTO relevar(UUID id, Relevar relevar, UUID actor) {
        return convertirADTO(negocio.relevar(id, convertir(relevar), actor));
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<AsignacionDTO> disponibles(LocalDateTime inicio, LocalDateTime fin, int capacidad) {
        return negocio.disponibles(inicio, fin, capacidad).stream()
                .map(asignacion -> convertirADTO(asignacion))
                .toList();
    }
}
