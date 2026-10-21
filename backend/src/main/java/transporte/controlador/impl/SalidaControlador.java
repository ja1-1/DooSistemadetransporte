package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.Comandos.Motivo;
import transporte.dto.Comandos.ProgramarSalida;
import transporte.dto.Comandos.Reemplazar;
import transporte.dto.Comandos.Refuerzo;
import transporte.dto.Comandos.RegistrarIncidencia;
import transporte.dto.Comandos.Reprogramar;
import transporte.dto.IncidenciaDTO;
import transporte.dto.SalidaDTO;
import transporte.infraestructura.seguridad.UsuarioAutenticado;
import transporte.negocio.fachada.SalidaFachada;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/salidas")
public class SalidaControlador {
    private final SalidaFachada fachada;

    public SalidaControlador(SalidaFachada operacionFachada) {
        fachada = operacionFachada;
    }

    @PostMapping("")
    public ResponseEntity<Respuesta<SalidaDTO>> programar(
            @RequestBody ProgramarSalida programarSalida) {
        return ResponseEntity.status(201).body(new Respuesta<>(fachada.programar(programarSalida)));
    }

    @PostMapping("/{id}/iniciar")
    public ResponseEntity<Respuesta<SalidaDTO>> iniciar(@PathVariable UUID id) {
        return ResponseEntity.status(200).body(new Respuesta<>(fachada.iniciar(id)));
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Respuesta<SalidaDTO>> finalizarSalida(@PathVariable UUID id) {
        return ResponseEntity.status(200).body(new Respuesta<>(fachada.finalizarSalida(id)));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Respuesta<SalidaDTO>> cancelar(
            @PathVariable UUID id,
            @RequestBody Motivo motivo,
            @AuthenticationPrincipal UsuarioAutenticado actor) {
        return ResponseEntity.status(200)
                .body(new Respuesta<>(fachada.cancelar(id, motivo, actor.id())));
    }

    @PostMapping("/{id}/reemplazar")
    public ResponseEntity<Respuesta<SalidaDTO>> reemplazar(
            @PathVariable UUID id,
            @RequestBody Reemplazar reemplazar,
            @AuthenticationPrincipal UsuarioAutenticado actor) {
        return ResponseEntity.status(200)
                .body(new Respuesta<>(fachada.reemplazar(id, reemplazar, actor.id())));
    }

    @PostMapping("/{id}/reprogramar")
    public ResponseEntity<Respuesta<SalidaDTO>> reprogramar(
            @PathVariable UUID id,
            @RequestBody Reprogramar reprogramar,
            @AuthenticationPrincipal UsuarioAutenticado actor) {
        return ResponseEntity.status(200)
                .body(new Respuesta<>(fachada.reprogramar(id, reprogramar, actor.id())));
    }

    @PostMapping("/{id}/refuerzo")
    public ResponseEntity<Respuesta<SalidaDTO>> refuerzo(
            @PathVariable UUID id,
            @RequestBody Refuerzo refuerzo,
            @AuthenticationPrincipal UsuarioAutenticado actor) {
        return ResponseEntity.status(201)
                .body(new Respuesta<>(fachada.refuerzo(id, refuerzo, actor.id())));
    }

    @PostMapping("/{id}/incidencias")
    public ResponseEntity<Respuesta<IncidenciaDTO>> incidencia(
            @PathVariable UUID id,
            @RequestBody RegistrarIncidencia registrarIncidencia,
            @AuthenticationPrincipal UsuarioAutenticado actor) {
        return ResponseEntity.status(201)
                .body(new Respuesta<>(fachada.incidencia(id, registrarIncidencia, actor.id())));
    }

    @GetMapping("/{id}/incidencias")
    public Respuesta<List<IncidenciaDTO>> historial(@PathVariable UUID id) {
        return new Respuesta<>(fachada.historial(id));
    }
}
