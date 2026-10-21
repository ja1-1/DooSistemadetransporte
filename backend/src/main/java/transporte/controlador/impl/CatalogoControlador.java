package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.CatalogoDTO;
import transporte.negocio.fachada.CatalogoFachada;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CatalogoControlador {
    private final CatalogoFachada fachada;

    public CatalogoControlador(CatalogoFachada fachada) {
        this.fachada = fachada;
    }

    @GetMapping("/{catalogo:roles|estados-salida|tipos-ruta|tipos-incidencia}")
    public Respuesta<List<CatalogoDTO>> consultar(@PathVariable String catalogo) {
        return new Respuesta<>(fachada.consultar(catalogo));
    }

    @GetMapping("/{catalogo:roles|estados-salida|tipos-ruta|tipos-incidencia}/{id}")
    public Respuesta<CatalogoDTO> consultarPorId(
            @PathVariable String catalogo, @PathVariable UUID id) {
        return new Respuesta<>(fachada.consultarPorId(catalogo, id));
    }
}
