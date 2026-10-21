package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.AsignacionDTO;
import transporte.dto.Comandos.Asignar;
import transporte.dto.Comandos.FinalizarAsignacion;
import transporte.dto.Comandos.Relevar;
import transporte.infraestructura.seguridad.UsuarioAutenticado;
import transporte.negocio.fachada.AsignacionFachada;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/asignaciones")
public class AsignacionControlador {
    private final AsignacionFachada fachada;

    public AsignacionControlador(AsignacionFachada operacionFachada) {
        fachada = operacionFachada;
    }

    @PostMapping("")
    public ResponseEntity<Respuesta<AsignacionDTO>> asignar(@RequestBody Asignar asignar) {
        return ResponseEntity.status(201).body(new Respuesta<>(fachada.asignar(asignar)));
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Respuesta<AsignacionDTO>> finalizarAsignacion(
            @PathVariable UUID id, @RequestBody FinalizarAsignacion finalizarAsignacion) {
        return ResponseEntity.status(200)
                .body(new Respuesta<>(fachada.finalizarAsignacion(id, finalizarAsignacion)));
    }

    @PostMapping("/{id}/relevar")
    public ResponseEntity<Respuesta<AsignacionDTO>> relevar(
            @PathVariable UUID id,
            @RequestBody Relevar relevar,
            @AuthenticationPrincipal UsuarioAutenticado actor) {
        return ResponseEntity.status(201)
                .body(new Respuesta<>(fachada.relevar(id, relevar, actor.id())));
    }

    @GetMapping("/disponibles")
    public Respuesta<List<AsignacionDTO>> disponibles(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fin,
            @RequestParam(defaultValue = "1") int capacidadMinima) {
        return new Respuesta<>(fachada.disponibles(inicio, fin, capacidadMinima));
    }
}
