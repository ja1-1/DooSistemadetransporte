package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.CambioProgramacionDTO;
import transporte.dto.Comandos.GenerarSalidas;
import transporte.dto.Comandos.ModificarProgramacion;
import transporte.dto.Comandos.RegistrarProgramacion;
import transporte.dto.GeneracionSalidasDTO;
import transporte.dto.ProgramacionRutaDTO;
import transporte.infraestructura.seguridad.UsuarioAutenticado;
import transporte.negocio.fachada.ProgramacionRutaFachada;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/programaciones-ruta")
public class ProgramacionRutaControlador {
    private final ProgramacionRutaFachada fachada;

    public ProgramacionRutaControlador(ProgramacionRutaFachada operacionFachada) {
        fachada = operacionFachada;
    }

    @PostMapping("")
    public ResponseEntity<Respuesta<ProgramacionRutaDTO>> registrarProgramacion(
            @RequestBody RegistrarProgramacion registrarProgramacion) {
        return ResponseEntity.status(201)
                .body(new Respuesta<>(fachada.registrarProgramacion(registrarProgramacion)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Respuesta<CambioProgramacionDTO>> modificarProgramacion(
            @PathVariable UUID id,
            @RequestBody ModificarProgramacion modificarProgramacion,
            @AuthenticationPrincipal UsuarioAutenticado actor) {
        return ResponseEntity.status(200)
                .body(
                        new Respuesta<>(
                                fachada.modificarProgramacion(
                                        id, modificarProgramacion, actor.id())));
    }

    @PostMapping("/{id}/generar")
    public ResponseEntity<Respuesta<GeneracionSalidasDTO>> generar(
            @PathVariable UUID id, @RequestBody GenerarSalidas generarSalidas) {
        return ResponseEntity.status(200)
                .body(new Respuesta<>(fachada.generar(id, generarSalidas)));
    }

    @PostMapping("/{id}/desactivar")
    public Respuesta<ProgramacionRutaDTO> desactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoProgramacion(id, false));
    }

    @PostMapping("/{id}/reactivar")
    public Respuesta<ProgramacionRutaDTO> reactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estadoProgramacion(id, true));
    }
}
