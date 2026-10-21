package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.AltaUsuarioDTO;
import transporte.dto.Comandos.CambiarContrasena;
import transporte.dto.Comandos.ModificarPerfil;
import transporte.dto.Comandos.ModificarUsuario;
import transporte.dto.Comandos.RegistrarUsuario;
import transporte.dto.UsuarioDTO;
import transporte.infraestructura.seguridad.UsuarioAutenticado;
import transporte.negocio.fachada.UsuarioFachada;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioControlador {
    private final UsuarioFachada fachada;

    public UsuarioControlador(UsuarioFachada usuarioFachada) {
        fachada = usuarioFachada;
    }

    @PostMapping
    public ResponseEntity<Respuesta<AltaUsuarioDTO>> registrar(
            @RequestBody RegistrarUsuario registrarUsuario) {
        return ResponseEntity.status(201)
                .cacheControl(CacheControl.noStore())
                .body(new Respuesta<>(fachada.registrar(registrarUsuario)));
    }

    @PutMapping("/{id}")
    public Respuesta<UsuarioDTO> modificar(
            @PathVariable UUID id, @RequestBody ModificarUsuario modificarUsuario) {
        return new Respuesta<>(fachada.modificar(id, modificarUsuario));
    }

    @PostMapping("/{id}/restablecer-contrasena")
    public ResponseEntity<Respuesta<AltaUsuarioDTO>> restablecer(@PathVariable UUID id) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(new Respuesta<>(fachada.restablecer(id)));
    }

    @PostMapping("/{id}/desactivar")
    public Respuesta<UsuarioDTO> desactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estado(id, false));
    }

    @PostMapping("/{id}/reactivar")
    public Respuesta<UsuarioDTO> reactivar(@PathVariable UUID id) {
        return new Respuesta<>(fachada.estado(id, true));
    }

    @GetMapping("/me")
    public Respuesta<UsuarioDTO> propia(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado) {
        return new Respuesta<>(fachada.propia(usuarioAutenticado.id()));
    }

    @PutMapping("/me")
    public Respuesta<UsuarioDTO> perfil(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @RequestBody ModificarPerfil modificarPerfil) {
        return new Respuesta<>(fachada.perfil(usuarioAutenticado.id(), modificarPerfil));
    }

    @PostMapping("/me/contrasena")
    public Respuesta<UsuarioDTO> contrasena(
            @AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado,
            @RequestBody CambiarContrasena cambiarContrasena) {
        return new Respuesta<>(fachada.contrasena(usuarioAutenticado.id(), cambiarContrasena));
    }
}
