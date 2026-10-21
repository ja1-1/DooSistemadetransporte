package transporte.controlador.impl;

import transporte.controlador.respuesta.Respuesta;
import transporte.dto.PaginaDTO;
import transporte.dto.VistaDTO;
import transporte.negocio.fachada.ConsultaFachada;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(
        "/api/v1/{recurso:conductores|vehiculos|usuarios|asignaciones|rutas|programaciones-ruta|salidas|incidencias}")
public class ConsultaControlador {
    private final ConsultaFachada fachada;

    public ConsultaControlador(ConsultaFachada consultaFachada) {
        fachada = consultaFachada;
    }

    @GetMapping("/{id}")
    public Respuesta<VistaDTO> consultar(@PathVariable String recurso, @PathVariable UUID id) {
        return new Respuesta<>(fachada.consultar(recurso, id));
    }

    @GetMapping
    public PaginaDTO<? extends VistaDTO> buscar(
            @PathVariable String recurso, @RequestParam Map<String, String> filtros) {
        return fachada.buscar(recurso, filtros);
    }
}
