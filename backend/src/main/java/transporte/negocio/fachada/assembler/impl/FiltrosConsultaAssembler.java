package transporte.negocio.fachada.assembler.impl;

import static transporte.transversal.utilitarios.Validacion.instante;
import static transporte.transversal.utilitarios.Validacion.rango;
import static transporte.transversal.utilitarios.Validacion.texto;

import transporte.dominio.RutaDominio;
import transporte.dominio.consulta.FiltroAsignacion;
import transporte.dominio.consulta.FiltroConductor;
import transporte.dominio.consulta.FiltroIncidencia;
import transporte.dominio.consulta.FiltroProgramacionRuta;
import transporte.dominio.consulta.FiltroRuta;
import transporte.dominio.consulta.FiltroSalida;
import transporte.dominio.consulta.FiltroUsuario;
import transporte.dominio.consulta.FiltroVehiculo;
import transporte.dominio.enums.CategoriaLicencia;
import transporte.dominio.enums.EstadoAsignacion;
import transporte.dominio.enums.EstadoSalida;
import transporte.dominio.enums.Rol;
import transporte.dominio.enums.TipoIncidencia;
import transporte.dominio.enums.TipoRuta;
import transporte.dominio.enums.TipoSangre;
import transporte.dominio.valor.Cedula;
import transporte.dominio.valor.Correo;
import transporte.dominio.valor.Placa;
import transporte.dominio.valor.Telefono;
import transporte.transversal.mensajes.CatalogoMensajes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public final class FiltrosConsultaAssembler {
    private static final int LONGITUD_LIBRE = 1000;

    private FiltrosConsultaAssembler() {}

    private static String textoLibre(String valor, String campo) {
        return texto(valor, campo, 1, LONGITUD_LIBRE);
    }

    private static String textoHasta(String valor, String campo, int longitudMaxima) {
        return texto(valor, campo, 1, longitudMaxima);
    }

    private static String validado(String valor, Consumer<String> objetoDeValor) {
        objetoDeValor.accept(valor);
        return valor;
    }

    private static UUID identificador(String valor, String campo) {
        return UUID.fromString(textoLibre(valor, campo));
    }

    private static LocalDate fecha(String valor, String campo) {
        return LocalDate.parse(textoLibre(valor, campo));
    }

    private static LocalTime hora(String valor, String campo) {
        return LocalTime.parse(textoLibre(valor, campo)).truncatedTo(ChronoUnit.MICROS);
    }

    private static LocalDateTime instanteDe(String valor, String campo) {
        return instante(LocalDateTime.parse(textoLibre(valor, campo)), campo);
    }

    private static Integer entero(String valor, String campo, int minimo, int maximo) {
        return rango(Integer.parseInt(textoLibre(valor, campo)), campo, minimo, maximo);
    }

    private static BigDecimal distancia(String valor, String campo) {
        return RutaDominio.distancia(new BigDecimal(textoLibre(valor, campo)));
    }

    private static Boolean booleano(String valor, String campo) {
        String texto = textoLibre(valor, campo);
        if (!texto.equals("true") && !texto.equals("false")) {
            throw new IllegalArgumentException(CatalogoMensajes.EL_FILTRO_DEBE_SER_TRUE_O_FALSE);
        }
        return Boolean.valueOf(texto);
    }

    public static FiltroConductor conductor(Map<String, String> parametros) {
        var filtro = FiltroConductor.todos();
        for (var parametro : parametros.entrySet()) {
            String campo = parametro.getKey();
            String valor = parametro.getValue();
            switch (campo) {
                case "pagina", "tamano" -> {}
                case "id" -> filtro = filtro.conId(identificador(valor, campo));
                case "cedula" ->
                        filtro =
                                filtro.conCedula(
                                        validado(textoHasta(valor, campo, 20), Cedula::new));
                case "primerNombre" ->
                        filtro = filtro.conPrimerNombre(textoHasta(valor, campo, 40));
                case "segundoNombre" ->
                        filtro = filtro.conSegundoNombre(textoHasta(valor, campo, 40));
                case "primerApellido" ->
                        filtro = filtro.conPrimerApellido(textoHasta(valor, campo, 40));
                case "segundoApellido" ->
                        filtro = filtro.conSegundoApellido(textoHasta(valor, campo, 40));
                case "fechaNacimiento" -> filtro = filtro.conFechaNacimiento(fecha(valor, campo));
                case "numeroTelefono" ->
                        filtro =
                                filtro.conNumeroTelefono(
                                        validado(textoHasta(valor, campo, 10), Telefono::new));
                case "tipoSangre" ->
                        filtro =
                                filtro.conTipoSangre(
                                        TipoSangre.desdeSimbolo(textoHasta(valor, campo, 3)));
                case "numeroLicencia" ->
                        filtro = filtro.conNumeroLicencia(textoHasta(valor, campo, 20));
                case "categoriaLicencia" ->
                        filtro =
                                filtro.conCategoriaLicencia(
                                        CategoriaLicencia.desdeTexto(textoHasta(valor, campo, 2)));
                case "fechaVencimientoLicencia" ->
                        filtro = filtro.conFechaVencimientoLicencia(fecha(valor, campo));
                case "activo" -> filtro = filtro.conActivo(booleano(valor, campo));
                default ->
                        throw new IllegalArgumentException(
                                CatalogoMensajes.FILTRO_NO_PERMITIDO + campo);
            }
        }
        return filtro;
    }

    public static FiltroVehiculo vehiculo(Map<String, String> parametros, LocalDate hoy) {
        var filtro = FiltroVehiculo.todos();
        for (var parametro : parametros.entrySet()) {
            String campo = parametro.getKey();
            String valor = parametro.getValue();
            switch (campo) {
                case "pagina", "tamano" -> {}
                case "id" -> filtro = filtro.conId(identificador(valor, campo));
                case "placa" ->
                        filtro = filtro.conPlaca(validado(textoHasta(valor, campo, 6), Placa::new));
                case "marca" -> filtro = filtro.conMarca(textoHasta(valor, campo, 60));
                case "modelo" -> filtro = filtro.conModelo(textoHasta(valor, campo, 60));
                case "anioFabricacion" ->
                        filtro =
                                filtro.conAnioFabricacion(
                                        entero(valor, campo, 1900, hoy.getYear()));
                case "capacidadPasajeros" ->
                        filtro =
                                filtro.conCapacidadPasajeros(
                                        entero(valor, campo, 1, Integer.MAX_VALUE));
                case "categoriaVehiculoId" ->
                        filtro = filtro.conCategoriaVehiculoId(identificador(valor, campo));
                case "activo" -> filtro = filtro.conActivo(booleano(valor, campo));
                default ->
                        throw new IllegalArgumentException(
                                CatalogoMensajes.FILTRO_NO_PERMITIDO + campo);
            }
        }
        return filtro;
    }

    public static FiltroUsuario usuario(Map<String, String> parametros) {
        var filtro = FiltroUsuario.todos();
        for (var parametro : parametros.entrySet()) {
            String campo = parametro.getKey();
            String valor = parametro.getValue();
            switch (campo) {
                case "pagina", "tamano" -> {}
                case "id" -> filtro = filtro.conId(identificador(valor, campo));
                case "primerNombre" ->
                        filtro = filtro.conPrimerNombre(textoHasta(valor, campo, 40));
                case "segundoNombre" ->
                        filtro = filtro.conSegundoNombre(textoHasta(valor, campo, 40));
                case "primerApellido" ->
                        filtro = filtro.conPrimerApellido(textoHasta(valor, campo, 40));
                case "segundoApellido" ->
                        filtro = filtro.conSegundoApellido(textoHasta(valor, campo, 40));
                case "cedula" ->
                        filtro =
                                filtro.conCedula(
                                        validado(textoHasta(valor, campo, 20), Cedula::new));
                case "correo" ->
                        filtro =
                                filtro.conCorreo(
                                        validado(textoHasta(valor, campo, 120), Correo::new));
                case "numeroTelefono" ->
                        filtro =
                                filtro.conNumeroTelefono(
                                        validado(textoHasta(valor, campo, 10), Telefono::new));
                case "rolId" -> filtro = filtro.conRol(Rol.desdeId(identificador(valor, campo)));
                case "conductorId" -> filtro = filtro.conConductorId(identificador(valor, campo));
                case "activo" -> filtro = filtro.conActivo(booleano(valor, campo));
                case "debeCambiarContrasena" ->
                        filtro = filtro.conDebeCambiarContrasena(booleano(valor, campo));
                default ->
                        throw new IllegalArgumentException(
                                CatalogoMensajes.FILTRO_NO_PERMITIDO + campo);
            }
        }
        return filtro;
    }

    public static FiltroAsignacion asignacion(Map<String, String> parametros) {
        var filtro = FiltroAsignacion.todos();
        for (var parametro : parametros.entrySet()) {
            String campo = parametro.getKey();
            String valor = parametro.getValue();
            switch (campo) {
                case "pagina", "tamano" -> {}
                case "id" -> filtro = filtro.conId(identificador(valor, campo));
                case "conductorId" -> filtro = filtro.conConductorId(identificador(valor, campo));
                case "vehiculoId" -> filtro = filtro.conVehiculoId(identificador(valor, campo));
                case "fechaInicio" -> filtro = filtro.conFechaInicio(fecha(valor, campo));
                case "fechaFin" -> filtro = filtro.conFechaFin(fecha(valor, campo));
                case "estado" -> filtro = filtro.conEstado(EstadoAsignacion.valueOf(valor));
                default ->
                        throw new IllegalArgumentException(
                                CatalogoMensajes.FILTRO_NO_PERMITIDO + campo);
            }
        }
        return filtro;
    }

    public static FiltroRuta ruta(Map<String, String> parametros) {
        var filtro = FiltroRuta.todos();
        for (var parametro : parametros.entrySet()) {
            String campo = parametro.getKey();
            String valor = parametro.getValue();
            switch (campo) {
                case "pagina", "tamano" -> {}
                case "id" -> filtro = filtro.conId(identificador(valor, campo));
                case "nombre" -> filtro = filtro.conNombre(textoHasta(valor, campo, 600));
                case "descripcion" -> filtro = filtro.conDescripcion(textoHasta(valor, campo, 500));
                case "activa" -> filtro = filtro.conActiva(booleano(valor, campo));
                case "origenLugarId" ->
                        filtro = filtro.conOrigenLugarId(identificador(valor, campo));
                case "destinoLugarId" ->
                        filtro = filtro.conDestinoLugarId(identificador(valor, campo));
                case "distanciaTotalKm" ->
                        filtro = filtro.conDistanciaTotalKm(distancia(valor, campo));
                case "tipoRutaId" ->
                        filtro = filtro.conTipoRuta(TipoRuta.desdeId(identificador(valor, campo)));
                case "viaId" -> filtro = filtro.conViaId(identificador(valor, campo));
                case "municipioId" -> filtro = filtro.conMunicipioId(UUID.fromString(valor));
                default ->
                        throw new IllegalArgumentException(
                                CatalogoMensajes.FILTRO_NO_PERMITIDO + campo);
            }
        }
        return filtro;
    }

    public static FiltroProgramacionRuta programacionRuta(Map<String, String> parametros) {
        var filtro = FiltroProgramacionRuta.todos();
        for (var parametro : parametros.entrySet()) {
            String campo = parametro.getKey();
            String valor = parametro.getValue();
            switch (campo) {
                case "pagina", "tamano" -> {}
                case "id" -> filtro = filtro.conId(identificador(valor, campo));
                case "rutaId" -> filtro = filtro.conRutaId(identificador(valor, campo));
                case "asignacionId" -> filtro = filtro.conAsignacionId(identificador(valor, campo));
                case "horaSalida" -> filtro = filtro.conHoraSalida(hora(valor, campo));
                case "horaUltimaSalida" -> filtro = filtro.conHoraUltimaSalida(hora(valor, campo));
                case "intervaloMinutos" ->
                        filtro = filtro.conIntervaloMinutos(entero(valor, campo, 1, 720));
                case "duracionEstimada" ->
                        filtro =
                                filtro.conDuracionEstimada(
                                        entero(valor, campo, 1, Integer.MAX_VALUE));
                case "vigenciaDesde" -> filtro = filtro.conVigenciaDesde(fecha(valor, campo));
                case "vigenciaHasta" -> filtro = filtro.conVigenciaHasta(fecha(valor, campo));
                case "activa" -> filtro = filtro.conActiva(booleano(valor, campo));
                default ->
                        throw new IllegalArgumentException(
                                CatalogoMensajes.FILTRO_NO_PERMITIDO + campo);
            }
        }
        return filtro;
    }

    public static FiltroSalida salida(Map<String, String> parametros) {
        var filtro = FiltroSalida.todos();
        for (var parametro : parametros.entrySet()) {
            String campo = parametro.getKey();
            String valor = parametro.getValue();
            switch (campo) {
                case "pagina", "tamano" -> {}
                case "id" -> filtro = filtro.conId(identificador(valor, campo));
                case "rutaId" -> filtro = filtro.conRutaId(identificador(valor, campo));
                case "nombreRuta" -> filtro = filtro.conNombreRuta(textoHasta(valor, campo, 600));
                case "asignacionId" -> filtro = filtro.conAsignacionId(identificador(valor, campo));
                case "instanteSalidaProgramado" ->
                        filtro = filtro.conInstanteSalidaProgramado(instanteDe(valor, campo));
                case "instanteLlegadaEstimado" ->
                        filtro = filtro.conInstanteLlegadaEstimado(instanteDe(valor, campo));
                case "instanteSalidaReal" ->
                        filtro = filtro.conInstanteSalidaReal(instanteDe(valor, campo));
                case "instanteLlegadaReal" ->
                        filtro = filtro.conInstanteLlegadaReal(instanteDe(valor, campo));
                case "estadoSalidaId" ->
                        filtro =
                                filtro.conEstadoSalida(
                                        EstadoSalida.desdeId(identificador(valor, campo)));
                case "programacionRutaId" ->
                        filtro = filtro.conProgramacionRutaId(identificador(valor, campo));
                case "fecha" -> {
                    var fecha = LocalDate.parse(valor);
                    filtro =
                            filtro.conSalidaDesde(fecha.atStartOfDay())
                                    .conSalidaHasta(fecha.plusDays(1).atStartOfDay());
                }
                default ->
                        throw new IllegalArgumentException(
                                CatalogoMensajes.FILTRO_NO_PERMITIDO + campo);
            }
        }
        return filtro;
    }

    public static FiltroIncidencia incidencia(Map<String, String> parametros) {
        var filtro = FiltroIncidencia.todos();
        for (var parametro : parametros.entrySet()) {
            String campo = parametro.getKey();
            String valor = parametro.getValue();
            switch (campo) {
                case "pagina", "tamano" -> {}
                case "id" -> filtro = filtro.conId(identificador(valor, campo));
                case "salidaId" -> filtro = filtro.conSalidaId(identificador(valor, campo));
                case "tipoIncidenciaId" ->
                        filtro =
                                filtro.conTipoIncidencia(
                                        TipoIncidencia.desdeId(identificador(valor, campo)));
                case "instanteOcurrencia" ->
                        filtro = filtro.conInstanteOcurrencia(instanteDe(valor, campo));
                case "instanteReporte" ->
                        filtro = filtro.conInstanteReporte(instanteDe(valor, campo));
                case "descripcion" -> filtro = filtro.conDescripcion(textoLibre(valor, campo));
                case "responsableId" ->
                        filtro = filtro.conResponsableId(identificador(valor, campo));
                case "responsableNombre" ->
                        filtro = filtro.conResponsableNombre(textoHasta(valor, campo, 200));
                case "responsableRol" ->
                        filtro = filtro.conResponsableRol(textoHasta(valor, campo, 40));
                case "fechaInicial" ->
                        filtro = filtro.conOcurrenciaDesde(LocalDate.parse(valor).atStartOfDay());
                case "fechaFinal" ->
                        filtro =
                                filtro.conOcurrenciaHasta(
                                        LocalDate.parse(valor).plusDays(1).atStartOfDay());
                default ->
                        throw new IllegalArgumentException(
                                CatalogoMensajes.FILTRO_NO_PERMITIDO + campo);
            }
        }
        if (filtro.ocurrenciaDesde() != null
                && filtro.ocurrenciaHasta() != null
                && !filtro.ocurrenciaHasta().isAfter(filtro.ocurrenciaDesde())) {
            throw new IllegalArgumentException(
                    CatalogoMensajes.LA_FECHA_FINAL_NO_PUEDE_SER_ANTERIOR);
        }
        return filtro;
    }
}
