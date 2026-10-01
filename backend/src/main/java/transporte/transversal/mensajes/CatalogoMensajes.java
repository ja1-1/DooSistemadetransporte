package transporte.transversal.mensajes;

public final class CatalogoMensajes {
    public static final String INCIDENCIA_INMUTABLE =
            "La identidad de una incidencia ya registrada no admite otro contenido ni otra salida.";
    public static final String RESULTADO_SQL_OBLIGATORIO =
            "La consulta no devolvió el resultado obligatorio.";

    private CatalogoMensajes() {}

    public static final String DATOS_INVALIDOS = "DATOS_INVALIDOS";
    public static final String PETICION_INVALIDA = "PETICION_INVALIDA";
    public static final String CONFLICTO = "CONFLICTO";
    public static final String PERSISTENCIA_NO_DISPONIBLE = "PERSISTENCIA_NO_DISPONIBLE";
    public static final String FORMATO_INVALIDO =
            "Revisa el formato de los datos y del identificador enviados.";
    public static final String INTEGRIDAD =
            "La operación incumple una restricción de integridad o un registro está en uso.";
    public static final String DUPLICADO = "Ya existe un registro con esa combinación única.";
    public static final String PERSISTENCIA =
            "No fue posible completar la operación. Intenta nuevamente.";

    public static final String FALLO_UNA_OPERACION_DE_PERSISTENCIA_DE_TRANSPORTE =
            "Falló una operación de persistencia de Transporte";

    public static final String DEBES_ENVIAR_LOS_DATOS_DE_LA_CATEGORIA =
            "Debes enviar los datos de la categoría.";

    public static final String LOS_DATOS_SON_OBLIGATORIOS = "Los datos son obligatorios.";

    public static final String NO_SE_ENCONTRO_EL_REGISTRO = "No se encontró el registro.";

    public static final String RECURSO_DESCONOCIDO = "Recurso desconocido.";

    public static final String EL_FILTRO_DEBE_SER_TRUE_O_FALSE = "El filtro debe ser true o false.";

    public static final String FILTRO_NO_PERMITIDO = "Filtro no permitido: ";

    public static final String LA_FECHA_FINAL_NO_PUEDE_SER_ANTERIOR =
            "La fecha final no puede ser anterior a la fecha inicial.";

    public static final String EL_VALOR_DE_CATALOGO_ES_OBLIGATORIO =
            "El valor de catálogo es obligatorio.";

    public static final String EL_VALOR_NO_CORRESPONDE_A_LOS_CATALOGOS =
            "El valor no corresponde a los catálogos oficiales.";

    public static final String DEBES_INICIAR_SESION = "Debes iniciar sesión.";

    public static final String EL_USUARIO_YA_NO_ESTA_DISPONIBLE =
            "El usuario ya no está disponible.";

    public static final String EL_ESTADO_O_LOS_PERMISOS_DE_LA =
            "El estado o los permisos de la cuenta cambiaron. Inicia sesión nuevamente.";

    public static final String NO_SE_ENCONTRO_EL_VALOR_DEL_CATALOGO =
            "No se encontró el valor del catálogo.";

    public static final String YA_EXISTE_UNA_CATEGORIA_DE_VEHICULO_CON =
            "Ya existe una categoría de vehículo con ese nombre.";

    public static final String NO_SE_ENCONTRO_LA_CATEGORIA_DE_VEHICULO =
            "No se encontró la categoría de vehículo.";

    public static final String YA_EXISTE_UNA_CATEGORIA_CON_ESE_NOMBRE =
            "Ya existe una categoría con ese nombre.";

    public static final String LA_CATEGORIA_ESTA_EN_USO = "La categoría está en uso.";

    public static final String NO_SE_ENCONTRO_EL_CONDUCTOR = "No se encontró el conductor.";

    public static final String NO_SE_ENCONTRO_EL_VEHICULO = "No se encontró el vehículo.";

    public static final String NO_SE_ENCONTRO_LA_CATEGORIA = "No se encontró la categoría.";

    public static final String LA_CEDULA_YA_ESTA_REGISTRADA = "La cédula ya está registrada.";

    public static final String LA_LICENCIA_YA_ESTA_REGISTRADA = "La licencia ya está registrada.";

    public static final String EL_CONDUCTOR_ESTA_EN_USO_CONSERVA_SU =
            "El conductor está en uso; conserva su historial mediante desactivación.";

    public static final String LA_PLACA_YA_ESTA_REGISTRADA = "La placa ya está registrada.";

    public static final String EL_VEHICULO_ESTA_EN_USO_CONSERVA_SU =
            "El vehículo está en uso; conserva su historial mediante desactivación.";

    public static final String NO_SE_ENCONTRO_MUNICIPIO = "No se encontró municipio.";

    public static final String EL_REGISTRO_ESTA_EN_USO_Y_NO =
            "El registro está en uso y no puede eliminarse.";

    public static final String NO_SE_ENCONTRO_VIA = "No se encontró via.";

    public static final String NO_SE_ENCONTRO_SECTOR = "No se encontró sector.";

    public static final String NO_SE_ENCONTRO_LUGAR = "No se encontró lugar.";

    public static final String NO_SE_ENCONTRO_EL_USUARIO = "No se encontró el usuario.";

    public static final String NO_SE_ENCONTRO_LA_RUTA = "No se encontró la ruta.";

    public static final String LA_RUTA_ESTA_EN_USO_Y_DEBE =
            "La ruta está en uso y debe conservarse.";

    public static final String NO_SE_ENCONTRO_LA_ASIGNACION = "No se encontró la asignación.";

    public static final String NO_SE_ENCONTRO_LA_PROGRAMACION = "No se encontró la programación.";

    public static final String NO_SE_ENCONTRO_LA_SALIDA = "No se encontró la salida.";

    public static final String EL_CATALOGO_NO_ESTA_DISPONIBLE = "El catálogo no está disponible.";

    public static final String ES_OBLIGATORIO = " es obligatorio.";

    public static final String Y = " y ";

    public static final String CARACTERES_VALIDOS = " caracteres válidos.";

    public static final String ESTA_FUERA_DE_RANGO = " está fuera de rango.";

    public static final String LA_OCURRENCIA_NO_PUEDE_SER_POSTERIOR_AL =
            "La ocurrencia no puede ser posterior al reporte.";

    public static final String EL_DETALLE_DE_LA_OPERACION_EXCEDE_EL =
            "El detalle de la operación excede el límite de la incidencia.";

    public static final String EL_NOMBRE_DE_LA_CATEGORIA_ES_OBLIGATORIO =
            "El nombre de la categoría es obligatorio.";

    public static final String EL_NOMBRE_DEBE_TENER_ENTRE_1_Y =
            "El nombre debe tener entre 1 y 40 caracteres.";

    public static final String EL_NOMBRE_CONTIENE_CARACTERES_DE_CONTROL_NO =
            "El nombre contiene caracteres de control no válidos.";

    public static final String EL_MUNICIPIO_NO_CORRESPONDE_AL_SECTOR =
            "El municipio no corresponde al sector.";

    public static final String EL_SECTOR_Y_SU_MUNICIPIO_DEBEN_ESTAR =
            "El sector y su municipio deben estar activos.";

    public static final String EL_REGISTRO_DEBE_ESTAR_ACTIVO = "El registro debe estar activo.";

    public static final String EL_REGISTRO_DEBE_ESTAR_INACTIVO = "El registro debe estar inactivo.";

    public static final String EL_VEHICULO_DEBE_ESTAR_ACTIVO = "El vehículo debe estar activo.";

    public static final String LA_CATEGORIA_NO_CORRESPONDE_AL_VEHICULO =
            "La categoría no corresponde al vehículo.";

    public static final String EL_CONDUCTOR_NO_ESTA_HABILITADO_PARA_ESE =
            "El conductor no está habilitado para ese vehículo en la fecha de inicio.";

    public static final String LA_ASIGNACION_YA_FUE_FINALIZADA = "La asignación ya fue finalizada.";

    public static final String LA_VENTANA_REQUIERE_UNA_LLEGADA_POSTERIOR_A =
            "La ventana requiere una llegada posterior a la salida.";

    public static final String LA_VENTANA_ES_OBLIGATORIA = "La ventana es obligatoria.";

    public static final String EL_PERIODO_REQUIERE_INICIO_Y_UN_FIN =
            "El periodo requiere inicio y un fin opcional no anterior al inicio.";

    public static final String LA_FECHA_ES_OBLIGATORIA = "La fecha es obligatoria.";

    public static final String EL_PERIODO_ES_OBLIGATORIO = "El periodo es obligatorio.";

    public static final String LA_FECHA_DE_FIN_ES_OBLIGATORIA = "La fecha de fin es obligatoria.";

    public static final String HAY_UNA_SALIDA_REPETIDA = "Hay una salida repetida.";

    public static final String UNA_SALIDA_NO_PUEDE_SER_NUEVA_Y =
            "Una salida no puede ser nueva y existente a la vez.";

    public static final String EL_FORMATO_DE_TELEFONO_NO_ES_VALIDO =
            "El formato de Telefono no es válido.";

    public static final String LA_PAGINA_DEBE_EMPEZAR_EN_1_Y =
            "La página debe empezar en 1 y el tamaño debe estar entre 1 y 100.";

    public static final String EL_FORMATO_DE_PLACA_NO_ES_VALIDO =
            "El formato de Placa no es válido.";

    public static final String EL_FORMATO_DE_CORREO_NO_ES_VALIDO =
            "El formato de Correo no es válido.";

    public static final String REVISA_LA_FRANJA_EL_INTERVALO_LA_DURACION =
            "Revisa la franja, el intervalo, la duración y los días del horario.";

    public static final String LA_ULTIMA_SALIDA_DEBE_PERTENECER_AL_INTERVALO =
            "La última salida debe pertenecer al intervalo y los viajes no pueden" + " solaparse.";

    public static final String EL_HORARIO_ES_OBLIGATORIO = "El horario es obligatorio.";

    public static final String LA_LICENCIA_DEBE_VENCER_DESPUES_DE_HOY =
            "La licencia debe vencer después de hoy.";

    public static final String EL_CONDUCTOR_DEBE_SER_MAYOR_DE_EDAD =
            "El conductor debe ser mayor de edad.";

    public static final String EL_CONDUCTOR_DEBE_ESTAR_ACTIVO = "El conductor debe estar activo.";

    public static final String EL_CONDUCTOR_DEBE_ESTAR_INACTIVO =
            "El conductor debe estar inactivo.";

    public static final String EL_IDENTIFICADOR_NO_PERTENECE_AL_CATALOGO_TIPORUTA =
            "El identificador no pertenece al catálogo TipoRuta.";

    public static final String EL_NOMBRE_NO_PERTENECE_AL_CATALOGO_TIPORUTA =
            "El nombre no pertenece al catálogo TipoRuta.";

    public static final String LA_LICENCIA_MINIMA_DEBE_SER_C1_C2 =
            "La licencia mínima debe ser C1, C2 o C3.";

    public static final String EL_IDENTIFICADOR_NO_PERTENECE_AL_CATALOGO_ROL =
            "El identificador no pertenece al catálogo Rol.";

    public static final String EL_NOMBRE_NO_PERTENECE_AL_CATALOGO_ROL =
            "El nombre no pertenece al catálogo Rol.";

    public static final String EL_IDENTIFICADOR_NO_PERTENECE_AL_CATALOGO_ESTADOSALIDA =
            "El identificador no pertenece al catálogo EstadoSalida.";

    public static final String EL_NOMBRE_NO_PERTENECE_AL_CATALOGO_ESTADOSALIDA =
            "El nombre no pertenece al catálogo EstadoSalida.";

    public static final String EL_IDENTIFICADOR_NO_PERTENECE_AL_CATALOGO_TIPOINCIDENCIA =
            "El identificador no pertenece al catálogo" + " TipoIncidencia.";

    public static final String EL_NOMBRE_NO_PERTENECE_AL_CATALOGO_TIPOINCIDENCIA =
            "El nombre no pertenece al catálogo TipoIncidencia.";

    public static final String EL_TIPO_DE_SANGRE_NO_ES_VALIDO = "El tipo de sangre no es válido.";

    public static final String EL_NUMERO_DE_LICENCIA_YA_ESTA_REGISTRADO =
            "El número de licencia ya está registrado.";

    public static final String LA_LICENCIA_NO_CUBRE_UNA_ASIGNACION_VIGENTE =
            "La licencia no cubre una asignación vigente.";

    public static final String LA_LICENCIA_DEBE_ESTAR_VIGENTE_EN_LAS =
            "La licencia debe estar vigente en las salidas programadas.";

    public static final String EL_CONDUCTOR_TIENE_SALIDAS_PROGRAMADAS_O_EN =
            "El conductor tiene salidas programadas o en curso.";

    public static final String EL_VEHICULO_TIENE_SALIDAS_PROGRAMADAS_O_EN =
            "El vehículo tiene salidas programadas o en curso.";

    public static final String UN_CONDUCTOR_ASIGNADO_NO_CUBRE_LA_CATEGORIA =
            "Un conductor asignado no cubre la categoría nueva.";

    public static final String UN_CONDUCTOR_ASIGNADO_NO_CUBRE_LA_LICENCIA =
            "Un conductor asignado no cubre la licencia mínima nueva.";

    public static final String LA_PROGRAMACION_COINCIDE_CON_OTRA_SALIDA_DE =
            "La programación coincide con otra salida de la misma ruta.";

    public static final String LA_PROGRAMACION_SOLAPA_LA_AGENDA_DE_SU =
            "La programación solapa la agenda de su asignación.";

    public static final String LA_ASIGNACION_NO_CUBRE_TODA_LA_VIGENCIA =
            "La asignación no cubre toda la vigencia.";

    public static final String EL_RANGO_DEBE_ESTAR_ENTRE_HOY_Y =
            "El rango debe estar entre hoy y los próximos siete días.";

    public static final String LA_PROGRAMACION_ESTA_DESACTIVADA =
            "La programación está desactivada.";

    public static final String LA_ASIGNACION_NO_ESTA_VIGENTE_DURANTE_TODA =
            "La asignación no está vigente durante toda la ventana.";

    public static final String EL_CONDUCTOR_SU_LICENCIA_O_EL_VEHICULO =
            "El conductor, su licencia o el vehículo no están habilitados en esa fecha.";

    public static final String YA_EXISTE_UNA_SALIDA_DE_LA_ASIGNACION =
            "Ya existe una salida de la asignación en ese instante.";

    public static final String LA_AGENDA_DE_LA_ASIGNACION_ESTA_OCUPADA =
            "La agenda de la asignación está ocupada.";

    public static final String EL_CONDUCTOR_O_EL_VEHICULO_YA_TIENEN =
            "El conductor o el vehículo ya tienen una salida en curso.";

    public static final String LA_SALIDA_YA_TIENE_ESTADO_DEFINITIVO =
            "La salida ya tiene estado definitivo.";

    public static final String YA_EXISTE_UNA_SALIDA_DE_LA_NUEVA =
            "Ya existe una salida de la nueva asignación en el instante programado.";

    public static final String LA_CAPACIDAD_DEL_NUEVO_VEHICULO_ES_INSUFICIENTE =
            "La capacidad del nuevo vehículo es insuficiente.";

    public static final String YA_EXISTE_OTRA_SALIDA_DE_ESA_PROGRAMACION =
            "Ya existe otra salida de esa programación en el instante indicado.";

    public static final String LA_PROGRAMACION_DEBE_ESTAR_INACTIVA =
            "La programación debe estar inactiva.";

    public static final String LA_VIGENCIA_TERMINO_O_LA_ASIGNACION_NO =
            "La vigencia terminó o la asignación no cubre el periodo.";

    public static final String EL_CONDUCTOR_YA_TIENE_UNA_ASIGNACION_QUE =
            "El conductor ya tiene una asignación que cruza ese periodo.";

    public static final String EL_VEHICULO_YA_TIENE_UNA_ASIGNACION_QUE =
            "El vehículo ya tiene una asignación que cruza ese periodo.";

    public static final String HAY_SALIDAS_PROGRAMADAS_O_EN_CURSO_QUE =
            "Hay salidas programadas o en curso que dependen de la asignación.";

    public static final String HAY_PROGRAMACIONES_ACTIVAS_QUE_DEPENDEN_DE_LA =
            "Hay programaciones activas que dependen de la asignación.";

    public static final String LA_ASIGNACION_YA_TIENE_FECHA_DE_FIN =
            "La asignación ya tiene fecha de fin.";

    public static final String EL_RELEVO_DEBE_SER_POSTERIOR_A_HOY =
            "El relevo debe ser posterior a hoy y al inicio.";

    public static final String EL_NUEVO_CONDUCTOR_DEBE_SER_DIFERENTE =
            "El nuevo conductor debe ser diferente.";

    public static final String EL_NUEVO_CONDUCTOR_YA_TIENE_UNA_ASIGNACION =
            "El nuevo conductor ya tiene una asignación en ese periodo.";

    public static final String UNA_SALIDA_PENDIENTE_CRUZA_LA_MEDIANOCHE_DEL =
            "Una salida pendiente cruza la medianoche del relevo.";

    public static final String LA_LICENCIA_DEL_NUEVO_CONDUCTOR_NO_CUBRE =
            "La licencia del nuevo conductor no cubre todas las salidas que se trasladan.";

    public static final String LA_VENTANA_NO_PUEDE_EMPEZAR_ANTES_DE =
            "La ventana no puede empezar antes de ahora y la capacidad debe ser positiva.";

    public static final String EL_ROL_CONDUCTOR_REQUIERE_CONDUCTOR_ASOCIADO_Y =
            "El rol Conductor requiere conductor asociado y los otros roles no lo" + " admiten.";

    public static final String LA_CONTRASENA_TEMPORAL_DEBE_TENER_12_CARACTERES =
            "La contraseña temporal debe tener 12 caracteres.";

    public static final String LAS_CREDENCIALES_NO_SON_VALIDAS = "Las credenciales no son válidas.";

    public static final String EL_USUARIO_DEBE_ESTAR_ACTIVO = "El usuario debe estar activo.";

    public static final String LA_CONTRASENA_ACTUAL_NO_ES_VALIDA =
            "La contraseña actual no es válida.";

    public static final String LA_CONTRASENA_NUEVA_DEBE_TENER_AL_MENOS =
            "La contraseña nueva debe tener al menos 8 caracteres.";

    public static final String EL_CONDUCTOR_ASOCIADO_NO_CORRESPONDE_AL_ROL =
            "El conductor asociado no corresponde al rol.";

    public static final String LA_CEDULA_DEBE_COINCIDIR_CON_LA_DEL =
            "La cédula debe coincidir con la del conductor.";

    public static final String EL_USUARIO_DEBE_ESTAR_INACTIVO = "El usuario debe estar inactivo.";

    public static final String LA_CAPACIDAD_DEBE_SER_POSITIVA = "La capacidad debe ser positiva.";

    public static final String EL_ANIO_MINIMO_ES_1900 =
            "El año de fabricación debe ser mayor o igual que 1900.";

    public static final String EL_ANO_NO_PUEDE_SUPERAR_EL_ACTUAL =
            "El año no puede superar el actual.";

    public static final String EL_VEHICULO_DEBE_ESTAR_INACTIVO = "El vehículo debe estar inactivo.";

    public static final String LA_RUTA_DEBE_ESTAR_ACTIVA = "La ruta debe estar activa.";

    public static final String LA_VIGENCIA_NO_PUEDE_EMPEZAR_ANTES_DE =
            "La vigencia no puede empezar antes de hoy.";

    public static final String LA_PROGRAMACION_DEBE_ESTAR_ACTIVA =
            "La programación debe estar activa.";

    public static final String LA_RUTA_NO_CORRESPONDE_A_LA_PROGRAMACION =
            "La ruta no corresponde a la programación.";

    public static final String EL_MUNICIPIO_DEBE_ESTAR_ACTIVO = "El municipio debe estar activo.";

    public static final String LA_DISTANCIA_DEBE_SER_POSITIVA_CON_HASTA =
            "La distancia debe ser positiva, con hasta seis enteros y dos decimales.";

    public static final String LAS_RELACIONES_GEOGRAFICAS_NO_SON_COHERENTES =
            "Las relaciones geográficas no son coherentes.";

    public static final String LOS_LUGARES_SECTORES_MUNICIPIOS_Y_VIA_DEBEN =
            "Los lugares, sectores, municipios y vía deben estar activos.";

    public static final String EL_RECORRIDO_NO_CORRESPONDE_AL_TIPO_DE =
            "El recorrido no corresponde al tipo de ruta.";

    public static final String YA_EXISTE_ESE_RECORRIDO = "Ya existe ese recorrido.";

    public static final String LA_RUTA_TIENE_SALIDAS_PENDIENTES =
            "La ruta tiene salidas pendientes.";

    public static final String LA_RUTA_DEBE_ESTAR_INACTIVA = "La ruta debe estar inactiva.";

    public static final String LOS_INSTANTES_REALES_NO_CORRESPONDEN_AL_ESTADO =
            "Los instantes reales no corresponden al estado.";

    public static final String UNA_INCIDENCIA_NO_PUEDE_REPETIRSE =
            "Una incidencia no puede repetirse.";

    public static final String LA_SALIDA_DEBE_SER_FUTURA = "La salida debe ser futura.";

    public static final String LA_SALIDA_NO_PUEDE_INICIARSE_FUERA_DE =
            "La salida no puede iniciarse fuera de su ventana o estado programado.";

    public static final String LA_SALIDA_DEBE_ESTAR_EN_CURSO = "La salida debe estar en curso.";

    public static final String LA_LLEGADA_REAL_DEBE_SER_POSTERIOR_AL =
            "La llegada real debe ser posterior al inicio.";

    public static final String LA_SALIDA_YA_TIENE_UN_ESTADO_DEFINITIVO =
            "La salida ya tiene un estado definitivo.";

    public static final String LA_VENTANA_DE_LA_SALIDA_VENCIO_SOLO =
            "La ventana de la salida venció; solo puede cancelarse.";

    public static final String LA_ASIGNACION_DEBE_SER_DIFERENTE =
            "La asignación debe ser diferente.";

    public static final String UNA_SALIDA_PROGRAMADA_CONSERVA_SU_VENTANA_DURANTE =
            "Una salida programada conserva su ventana durante el reemplazo.";

    public static final String LA_NUEVA_LLEGADA_DEBE_SER_FUTURA =
            "La nueva llegada debe ser futura.";

    public static final String SOLO_SE_REPROGRAMA_UNA_SALIDA_PROGRAMADA =
            "Solo se reprograma una salida programada.";

    public static final String LA_NUEVA_VENTANA_DEBE_SER_FUTURA_Y =
            "La nueva ventana debe ser futura y diferente.";

    public static final String UNA_SALIDA_DE_PROGRAMACION_DEBE_CONSERVAR_SU =
            "Una salida de programación debe conservar su fecha.";

    public static final String SOLO_SE_TRASLADAN_SALIDAS_PROGRAMADAS =
            "Solo se trasladan salidas programadas.";

    public static final String NO_FUE_POSIBLE_PUBLICAR_EL_AVISO_DE =
            "No fue posible publicar el aviso de la salida {}: {}";

    public static final String NO_HAY_DESTINATARIO_PARA_EL_AVISO =
            "No hay destinatario para el aviso.";

    public static final String NO_SE_PUDO_ENVIAR_EL_AVISO_DE =
            "No se pudo enviar el aviso de la salida {}: {}";

    public static final String NO_SE_PUDO_PREPARAR_EL_AVISO_DE =
            "No se pudo preparar el aviso de la salida {}: {}";

    public static final String LA_ESCRITURA_NECESITA_UNA_TRANSACCION =
            "La escritura necesita una transacción.";
}
