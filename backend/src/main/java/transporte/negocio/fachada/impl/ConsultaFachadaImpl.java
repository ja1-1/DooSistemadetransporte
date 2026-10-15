package transporte.negocio.fachada.impl;

import static transporte.negocio.fachada.assembler.impl.VistaDTOAssembler.convertirADTO;
import static transporte.transversal.excepciones.NegocioExcepcion.Tipo.NO_ENCONTRADO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import transporte.dominio.repositorio.AsignacionRepositorio;
import transporte.dominio.repositorio.ConductorRepositorio;
import transporte.dominio.repositorio.ProgramacionRutaRepositorio;
import transporte.dominio.repositorio.RutaRepositorio;
import transporte.dominio.repositorio.SalidaRepositorio;
import transporte.dominio.repositorio.UsuarioRepositorio;
import transporte.dominio.repositorio.VehiculoRepositorio;
import transporte.dominio.valor.Pagina;
import transporte.dto.PaginaDTO;
import transporte.dto.VistaDTO;
import transporte.negocio.fachada.ConsultaFachada;
import transporte.negocio.fachada.assembler.impl.FiltrosConsultaAssembler;
import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
public class ConsultaFachadaImpl implements ConsultaFachada {
    private static final String PARAMETRO_PAGINA = "pagina";
    private static final String PARAMETRO_TAMANO = "tamano";
    private static final String PAGINA_INICIAL = "1";
    private static final String TAMANO_PREDETERMINADO = "20";

    private final Clock reloj;
    private final ConductorRepositorio conductor;
    private final VehiculoRepositorio vehiculo;
    private final UsuarioRepositorio usuario;
    private final AsignacionRepositorio asignacion;
    private final RutaRepositorio ruta;
    private final ProgramacionRutaRepositorio programacionRuta;
    private final SalidaRepositorio salida;

    public ConsultaFachadaImpl(
            Clock reloj,
            ConductorRepositorio conductor,
            VehiculoRepositorio vehiculo,
            UsuarioRepositorio usuario,
            AsignacionRepositorio asignacion,
            RutaRepositorio ruta,
            ProgramacionRutaRepositorio programacionRuta,
            SalidaRepositorio salida) {
        this.reloj = reloj;
        this.conductor = conductor;
        this.vehiculo = vehiculo;
        this.usuario = usuario;
        this.asignacion = asignacion;
        this.ruta = ruta;
        this.programacionRuta = programacionRuta;
        this.salida = salida;
    }

    @Override
    public VistaDTO consultar(String recurso, UUID id) {
        return switch (recurso) {
            case "conductores" ->
                    encontrado(conductor.buscarPorId(id), dominio -> convertirADTO(dominio));
            case "vehiculos" ->
                    encontrado(vehiculo.buscarPorId(id), dominio -> convertirADTO(dominio));
            case "usuarios" ->
                    encontrado(usuario.buscarPorId(id), dominio -> convertirADTO(dominio));
            case "asignaciones" ->
                    encontrado(asignacion.buscarPorId(id), dominio -> convertirADTO(dominio));
            case "rutas" -> encontrado(ruta.buscarPorId(id), dominio -> convertirADTO(dominio));
            case "programaciones-ruta" ->
                    encontrado(programacionRuta.buscarPorId(id), dominio -> convertirADTO(dominio));
            case "salidas" -> encontrado(salida.buscarPorId(id), dominio -> convertirADTO(dominio));
            case "incidencias" ->
                    encontrado(
                            salida.consultarIncidencia(id),
                            proyeccion -> convertirADTO(proyeccion));
            default -> throw new IllegalArgumentException(CatalogoMensajes.RECURSO_DESCONOCIDO);
        };
    }

    @Override
    public PaginaDTO<? extends VistaDTO> buscar(String recurso, Map<String, String> parametros) {
        var pagina = paginaSolicitada(parametros);
        return switch (recurso) {
            case "conductores" -> {
                var filtro = FiltrosConsultaAssembler.conductor(parametros);
                yield paginar(
                        pagina,
                        conductor.contar(filtro),
                        () -> conductor.buscar(filtro, pagina),
                        dominio -> convertirADTO(dominio));
            }
            case "vehiculos" -> {
                var filtro = FiltrosConsultaAssembler.vehiculo(parametros, LocalDate.now(reloj));
                yield paginar(
                        pagina,
                        vehiculo.contar(filtro),
                        () -> vehiculo.buscar(filtro, pagina),
                        dominio -> convertirADTO(dominio));
            }
            case "usuarios" -> {
                var filtro = FiltrosConsultaAssembler.usuario(parametros);
                yield paginar(
                        pagina,
                        usuario.contar(filtro),
                        () -> usuario.buscar(filtro, pagina),
                        dominio -> convertirADTO(dominio));
            }
            case "asignaciones" -> {
                var hoy = LocalDate.now(reloj);
                var filtro =
                        FiltrosConsultaAssembler.asignacion(parametros).conFechaReferencia(hoy);
                yield paginar(
                        pagina,
                        asignacion.contar(filtro),
                        () -> asignacion.buscar(filtro, pagina),
                        dominio -> convertirADTO(dominio).conEstado(dominio.calcularEstado(hoy)));
            }
            case "rutas" -> {
                var filtro = FiltrosConsultaAssembler.ruta(parametros);
                yield paginar(
                        pagina,
                        ruta.contar(filtro),
                        () -> ruta.buscar(filtro, pagina),
                        dominio -> convertirADTO(dominio));
            }
            case "programaciones-ruta" -> {
                var filtro = FiltrosConsultaAssembler.programacionRuta(parametros);
                yield paginar(
                        pagina,
                        programacionRuta.contar(filtro),
                        () -> programacionRuta.buscar(filtro, pagina),
                        dominio -> convertirADTO(dominio));
            }
            case "salidas" -> {
                var filtro = FiltrosConsultaAssembler.salida(parametros);
                yield paginar(
                        pagina,
                        salida.contar(filtro),
                        () -> salida.buscar(filtro, pagina),
                        dominio -> convertirADTO(dominio));
            }
            case "incidencias" -> {
                var filtro = FiltrosConsultaAssembler.incidencia(parametros);
                yield paginar(
                        pagina,
                        salida.contarIncidencias(filtro),
                        () -> salida.consultarIncidencias(filtro, pagina),
                        proyeccion -> convertirADTO(proyeccion));
            }
            default -> throw new IllegalArgumentException(CatalogoMensajes.RECURSO_DESCONOCIDO);
        };
    }

    private static Pagina paginaSolicitada(Map<String, String> parametros) {
        return new Pagina(
                Integer.parseInt(parametros.getOrDefault(PARAMETRO_PAGINA, PAGINA_INICIAL)),
                Integer.parseInt(parametros.getOrDefault(PARAMETRO_TAMANO, TAMANO_PREDETERMINADO)));
    }

    private static <D, V extends VistaDTO> V encontrado(
            Optional<D> registro, Function<D, V> conversion) {
        return registro.map(conversion)
                .orElseThrow(
                        () ->
                                new NegocioExcepcion(
                                        NO_ENCONTRADO,
                                        CatalogoMensajes.NO_SE_ENCONTRO_EL_REGISTRO));
    }

    private static <D, V extends VistaDTO> PaginaDTO<V> paginar(
            Pagina pagina, long total, Supplier<List<D>> consulta, Function<D, V> conversion) {
        List<V> registros =
                pagina.desplazamiento() >= total
                        ? List.of()
                        : consulta.get().stream().map(conversion).toList();
        return new PaginaDTO<>(registros, total, pagina.numero(), pagina.tamano());
    }
}
