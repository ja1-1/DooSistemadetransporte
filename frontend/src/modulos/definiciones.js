const ADMIN = ["ADMINISTRADOR"];
const GESTION = ["ADMINISTRADOR", "JEFE_OPERACION"];
const JEFE = ["JEFE_OPERACION"];
const CONDUCTOR = ["CONDUCTOR"];
const OPERACION = ["JEFE_OPERACION", "CONDUCTOR"];
const TODOS = [...GESTION, ...CONDUCTOR];
const texto = (nombre, etiqueta, opciones = {}) => ({
  nombre,
  etiqueta,
  tipo: "text",
  ...opciones,
});
const fecha = (nombre, etiqueta, opciones = {}) =>
  texto(nombre, etiqueta, { tipo: "date", ...opciones });
const instante = (nombre, etiqueta) =>
  texto(nombre, etiqueta, { tipo: "datetime-local", step: "any" });
const numero = (nombre, etiqueta, opciones = {}) =>
  texto(nombre, etiqueta, { tipo: "number", min: 1, ...opciones });
const lista = (nombre, etiqueta, opciones, extra = {}) =>
  texto(nombre, etiqueta, { tipo: "select", opciones, ...extra });
const relacion = (nombre, etiqueta, recurso, opciones = {}) =>
  texto(nombre, etiqueta, { tipo: "relacion", recurso, ...opciones });
const opcional = { opcional: true };
const nombre = texto("nombre", "Nombre", { maxLength: 120 });
const nombreVia = { ...nombre, minLength: 3, maxLength: 80 };
const descripcion = texto("descripcion", "Descripción", {
  tipo: "textarea",
  maxLength: 500,
});
const descripcionVia = { ...descripcion, minLength: 10, maxLength: 250 };
const motivo = texto("motivo", "Motivo", { tipo: "textarea", maxLength: 1000 });
const activo = lista(
  "activo",
  "Activo",
  [
    ["true", "Sí"],
    ["false", "No"],
  ],
  opcional,
);
const activa = { ...activo, nombre: "activa", etiqueta: "Activa" };
const licencias = ["C1", "C2", "C3"];
const camposCategoria = [
  texto("nombre", "Nombre de la categoría", {
    minLength: 1,
    normalizar: (valor) => {
      const limpio = (valor ?? "").trim();
      if ([...limpio].length < 1 || [...limpio].length > 40)
        throw new Error("El nombre debe tener entre 1 y 40 caracteres.");
      return limpio;
    },
  }),
  lista("categoriaLicenciaMinima", "Licencia mínima", licencias, {
    valorInicial: "C1",
  }),
];
const nombres = [
  texto("primerNombre", "Primer nombre", { maxLength: 40 }),
  texto("segundoNombre", "Segundo nombre", { ...opcional, maxLength: 40 }),
  texto("primerApellido", "Primer apellido", { maxLength: 40 }),
  texto("segundoApellido", "Segundo apellido", { ...opcional, maxLength: 40 }),
];
const telefono = texto("numeroTelefono", "Teléfono", {
  inputMode: "numeric",
  pattern: "3[0-9]{9}",
  maxLength: 10,
});
const licencia = [
  texto("numeroLicencia", "Número de licencia", {
    minLength: 5,
    maxLength: 20,
    origen: "licencia.numero",
  }),
  lista("categoriaLicencia", "Categoría de licencia", licencias, {
    origen: "licencia.categoria",
  }),
  fecha("fechaVencimientoLicencia", "Vencimiento de licencia", {
    origen: "licencia.fechaVencimiento",
  }),
];
const conductor = relacion("conductorId", "Conductor", "conductores");
const vehiculo = relacion("vehiculoId", "Vehículo", "vehiculos");
const asignacion = relacion("asignacionId", "Asignación", "asignaciones");
const ruta = relacion("rutaId", "Ruta", "rutas");
const recorrido = [
  relacion("origenId", "Lugar de origen", "lugares"),
  relacion("destinoId", "Lugar de destino", "lugares"),
  relacion("viaId", "Vía", "vias"),
  numero("distanciaTotalKm", "Distancia total (km)", {
    min: "0.005",
    step: "any",
  }),
];
const horario = [
  asignacion,
  texto("horaSalida", "Primera salida", {
    tipo: "time",
    step: "any",
    origen: "horario.horaSalida",
  }),
  texto("horaUltimaSalida", "Última salida", {
    tipo: "time",
    step: "any",
    origen: "horario.horaUltimaSalida",
  }),
  numero("intervaloMinutos", "Intervalo (minutos)", {
    max: 720,
    origen: "horario.intervaloMinutos",
  }),
  numero("duracionEstimada", "Duración estimada (minutos)", {
    origen: "horario.duracionEstimada",
  }),
  texto("diasSemana", "Días de operación", {
    tipo: "dias",
    origen: "horario.diasSemana",
  }),
];
const ventana = [
  instante("instanteSalidaProgramado", "Salida programada"),
  instante("instanteLlegadaEstimado", "Llegada estimada"),
].map((campo) => ({ ...campo, origen: `ventana.${campo.nombre}` }));
const accion = (
  id,
  titulo,
  metodo,
  sufijo,
  roles,
  campos = [],
  extra = {},
) => ({ id, titulo, metodo, sufijo, roles, campos, ...extra });
const editar = (campos) =>
  accion("editar", "Modificar", "PUT", "", ADMIN, campos);
const eliminar = accion(
  "eliminar",
  "Eliminar definitivamente",
  "DELETE",
  "",
  ADMIN,
  [],
  {
    advertencia:
      "Esta acción elimina definitivamente el registro. El sistema rechazará el borrado si tiene relaciones que deben conservarse.",
  },
);
const estados = [
  accion("desactivar", "Desactivar", "POST", "/desactivar", ADMIN, [], {
    estado: true,
  }),
  accion("reactivar", "Reactivar", "POST", "/reactivar", ADMIN, [], {
    estado: false,
  }),
];
const alta = (campos, roles = ADMIN) =>
  accion("crear", "Registrar", "POST", "", roles, campos, { coleccion: true });
const columnas = (...pares) =>
  pares.map(([campo, etiqueta, recurso]) => ({ campo, etiqueta, recurso }));

export const tiposIncidencia = [
  ["23ac0422-5acc-56a1-928f-539aa5f983a7", "Retraso"],
  ["062a719f-c134-5376-a5fb-401acd4c57b4", "Avería"],
  ["0d1e1111-6dda-5258-83c2-3e3465f71120", "Reemplazo de vehículo"],
  ["533a1de0-820c-52d4-80bb-7a0554a86916", "Cancelación"],
  ["df627dce-fa63-5af4-a65a-69d9e4f0f8e8", "Reprogramación"],
  ["a7321fe4-2fa1-53d4-8bd9-f16bc724424a", "Refuerzo"],
  ["47911191-5a10-5ecd-b1ec-5f8c82e1679f", "Relevo de conductor"],
  ["634d7621-03c9-5d87-8f34-0e80a6438c8c", "Otro"],
];
const incidencia = accion(
  "incidencia",
  "Registrar incidencia",
  "POST",
  "/incidencias",
  OPERACION,
  [
    lista("tipoIncidenciaId", "Tipo de incidencia", tiposIncidencia),
    instante("instanteOcurrencia", "Instante de ocurrencia"),
    texto("descripcion", "Descripción", { tipo: "textarea", maxLength: 1000 }),
  ],
);
const historial = accion(
  "historial",
  "Consultar historial de incidencias",
  "GET",
  "/incidencias",
  GESTION,
);

export const modulos = [
  {
    id: "categorias-vehiculo",
    titulo: "Categorías de vehículo",
    grupo: "Gestión de flota",
    roles: GESTION,
    filtros: [
      texto("nombre", "Buscar por nombre"),
      lista("categoriaLicenciaMinima", "Licencia", licencias, opcional),
    ],
    columnas: columnas(
      ["nombre", "Nombre"],
      ["categoriaLicenciaMinima", "Licencia mínima"],
    ),
    crear: alta(camposCategoria),
    acciones: [editar(camposCategoria), eliminar],
  },
  {
    id: "conductores",
    titulo: "Conductores",
    grupo: "Gestión de flota",
    roles: GESTION,
    filtros: [
      texto("cedula", "Cédula"),
      texto("primerNombre", "Primer nombre"),
      activo,
    ],
    columnas: columnas(
      ["cedula", "Cédula"],
      ["primerNombre", "Nombre"],
      ["primerApellido", "Apellido"],
      ["licencia.categoria", "Licencia"],
      ["licencia.fechaVencimiento", "Vencimiento"],
      ["activo", "Activo"],
    ),
    crear: alta([
      texto("cedula", "Cédula", { minLength: 5, maxLength: 20 }),
      ...nombres,
      fecha("fechaNacimiento", "Fecha de nacimiento"),
      telefono,
      lista("tipoSangre", "Tipo de sangre", [
        "A+",
        "A-",
        "B+",
        "B-",
        "AB+",
        "AB-",
        "O+",
        "O-",
      ]),
      ...licencia,
    ]),
    acciones: [
      editar([
        ...nombres,
        telefono,
        lista("tipoSangre", "Tipo de sangre", [
          "A+",
          "A-",
          "B+",
          "B-",
          "AB+",
          "AB-",
          "O+",
          "O-",
        ]),
        ...licencia,
      ]),
      ...estados,
      eliminar,
    ],
  },
  {
    id: "vehiculos",
    titulo: "Vehículos",
    grupo: "Gestión de flota",
    roles: GESTION,
    filtros: [texto("placa", "Placa"), activo],
    columnas: columnas(
      ["placa", "Placa"],
      ["marca", "Marca"],
      ["modelo", "Modelo"],
      ["capacidadPasajeros", "Capacidad"],
      ["categoriaVehiculoId", "Categoría", "categorias-vehiculo"],
      ["activo", "Activo"],
    ),
    crear: alta([
      texto("placa", "Placa", { pattern: "[A-Za-z]{3}[0-9]{3}", maxLength: 6 }),
      texto("marca", "Marca", { maxLength: 60 }),
      texto("modelo", "Modelo", { maxLength: 60 }),
      numero("anioFabricacion", "Año de fabricación", { min: 1900 }),
      numero("capacidadPasajeros", "Capacidad de pasajeros"),
      relacion(
        "categoriaVehiculoId",
        "Categoría de vehículo",
        "categorias-vehiculo",
      ),
    ]),
    acciones: [
      editar([
        texto("marca", "Marca", { maxLength: 60 }),
        texto("modelo", "Modelo", { maxLength: 60 }),
        numero("anioFabricacion", "Año de fabricación", { min: 1900 }),
        numero("capacidadPasajeros", "Capacidad de pasajeros"),
        relacion(
          "categoriaVehiculoId",
          "Categoría de vehículo",
          "categorias-vehiculo",
        ),
      ]),
      ...estados,
      eliminar,
    ],
  },
  {
    id: "asignaciones",
    titulo: "Asignaciones",
    grupo: "Planificación y operación",
    roles: GESTION,
    filtros: [
      conductor,
      vehiculo,
      lista("estado", "Estado", ["VIGENTE", "FINALIZADA"], opcional),
    ],
    columnas: columnas(
      ["conductorId", "Conductor", "conductores"],
      ["vehiculoId", "Vehículo", "vehiculos"],
      ["fechaInicio", "Inicio"],
      ["fechaFin", "Fin"],
      ["estado", "Estado"],
    ),
    crear: alta(
      [conductor, vehiculo, fecha("fechaInicio", "Fecha de inicio")],
      GESTION,
    ),
    acciones: [
      accion(
        "finalizar",
        "Finalizar asignación",
        "POST",
        "/finalizar",
        GESTION,
        [fecha("fechaFin", "Fecha de finalización")],
      ),
      accion("relevar", "Relevar conductor", "POST", "/relevar", JEFE, [
        relacion("nuevoConductorId", "Nuevo conductor", "conductores"),
        fecha("fechaRelevo", "Fecha de relevo"),
        motivo,
      ]),
    ],
    consulta: accion(
      "disponibles",
      "Consultar disponibilidad",
      "GET",
      "/disponibles",
      JEFE,
      [
        instante("inicio", "Inicio de la ventana"),
        instante("fin", "Fin de la ventana"),
        numero("capacidadMinima", "Capacidad mínima"),
      ],
      { coleccion: true },
    ),
  },
  {
    id: "rutas",
    titulo: "Rutas",
    grupo: "Planificación y operación",
    roles: GESTION,
    filtros: [
      texto("nombre", "Nombre"),
      relacion("municipioId", "Municipio", "municipios", opcional),
      activa,
    ],
    columnas: columnas(
      ["nombre", "Nombre"],
      ["tipoRutaId", "Tipo", "tipos-ruta"],
      ["distanciaTotalKm", "Distancia (km)"],
      ["activa", "Activa"],
    ),
    crear: alta([
      descripcion,
      relacion("tipoRutaId", "Tipo de ruta", "tipos-ruta"),
      ...recorrido,
    ]),
    acciones: [
      editar([descripcion]),
      accion(
        "recorrido",
        "Cambiar recorrido",
        "PUT",
        "/recorrido",
        ADMIN,
        recorrido,
      ),
      ...estados,
      eliminar,
    ],
  },
  {
    id: "programaciones-ruta",
    titulo: "Programaciones de ruta",
    grupo: "Planificación y operación",
    roles: GESTION,
    filtros: [ruta, asignacion, activa],
    columnas: columnas(
      ["rutaId", "Ruta", "rutas"],
      ["asignacionId", "Asignación", "asignaciones"],
      ["horario.horaSalida", "Primera salida"],
      ["horario.horaUltimaSalida", "Última salida"],
      ["horario.diasSemana", "Días"],
      ["vigenciaDesde", "Desde"],
      ["vigenciaHasta", "Hasta"],
      ["activa", "Activa"],
    ),
    crear: alta([
      ruta,
      ...horario,
      fecha("vigenciaDesde", "Vigencia desde"),
      fecha("vigenciaHasta", "Vigencia hasta", opcional),
    ]),
    acciones: [
      editar([
        ...horario,
        texto(
          "reprogramarSalidasGeneradas",
          "Reprogramar las salidas generadas",
          { tipo: "checkbox" },
        ),
      ]),
      ...estados,
      accion(
        "generar",
        "Generar salidas",
        "POST",
        "/generar",
        JEFE,
        [
          fecha("fechaInicial", "Fecha inicial"),
          fecha("fechaFinal", "Fecha final"),
        ],
        {
          ayuda:
            "Selecciona un rango entre hoy y los próximos siete días. Se mostrarán las salidas nuevas, las existentes y los rechazos.",
        },
      ),
    ],
  },
  {
    id: "salidas",
    titulo: "Salidas",
    grupo: "Planificación y operación",
    roles: OPERACION,
    filtros: [fecha("fecha", "Fecha"), texto("nombreRuta", "Nombre de ruta")],
    columnas: columnas(
      ["nombreRuta", "Ruta"],
      ["ventana.instanteSalidaProgramado", "Salida programada"],
      ["ventana.instanteLlegadaEstimado", "Llegada estimada"],
      ["estadoSalida", "Estado"],
    ),
    crear: alta([ruta, asignacion, ...ventana], JEFE),
    acciones: [
      accion("iniciar", "Iniciar salida", "POST", "/iniciar", CONDUCTOR),
      accion("finalizar", "Finalizar salida", "POST", "/finalizar", CONDUCTOR),
      accion("cancelar", "Cancelar salida", "POST", "/cancelar", JEFE, [
        motivo,
      ]),
      accion(
        "reemplazar",
        "Reemplazar asignación",
        "POST",
        "/reemplazar",
        JEFE,
        [
          relacion("nuevaAsignacionId", "Nueva asignación", "asignaciones"),
          instante("nuevaLlegadaEstimada", "Nueva llegada estimada"),
          motivo,
        ],
      ),
      accion(
        "reprogramar",
        "Reprogramar salida",
        "POST",
        "/reprogramar",
        JEFE,
        [ventana[0], { ...ventana[1], ...opcional }, motivo],
        {
          ayuda:
            "Si dejas vacía la llegada estimada, se conserva la duración que ya tenía la salida.",
        },
      ),
      accion("refuerzo", "Programar refuerzo", "POST", "/refuerzo", JEFE, [
        asignacion,
        instante("instanteSalida", "Salida del refuerzo"),
        motivo,
      ]),
      incidencia,
      historial,
    ],
  },
  {
    id: "incidencias",
    titulo: "Incidencias",
    grupo: "Planificación y operación",
    roles: GESTION,
    filtros: [
      lista(
        "salidaId",
        "Salida de esta página",
        (registros) => [
          ...new Map(
            registros.filter((registro) => registro.salidaId).map((registro) => [
              registro.salidaId,
              [
                registro.salidaId,
                `${registro.instanteOcurrencia} · ${registro.descripcion}`,
              ],
            ]),
          ).values(),
        ],
        opcional,
      ),
      fecha("fechaInicial", "Fecha inicial"),
      fecha("fechaFinal", "Fecha final"),
      lista(
        "tipoIncidenciaId",
        "Tipo de incidencia",
        tiposIncidencia,
        opcional,
      ),
    ],
    columnas: columnas(
      ["instanteOcurrencia", "Ocurrencia"],
      ["descripcion", "Descripción"],
      ["responsable.nombre", "Responsable"],
      ["salidaId", "Salida", "salidas"],
    ),
    acciones: [historial],
    rutaAccion: (registro) =>
      `/api/v1/salidas/${encodeURIComponent(registro.salidaId)}`,
  },
  {
    id: "municipios",
    titulo: "Municipios",
    grupo: "Geografía",
    roles: GESTION,
    filtros: [nombre, texto("departamento", "Departamento"), activo],
    columnas: columnas(
      ["nombre", "Nombre"],
      ["departamento", "Departamento"],
      ["activo", "Activo"],
    ),
    crear: alta([
      nombre,
      texto("departamento", "Departamento", { maxLength: 80 }),
    ]),
    acciones: [
      editar([
        nombre,
        texto("departamento", "Departamento", { maxLength: 80 }),
      ]),
      ...estados,
      eliminar,
    ],
  },
  {
    id: "sectores",
    titulo: "Sectores",
    grupo: "Geografía",
    roles: GESTION,
    filtros: [
      nombre,
      relacion("municipioId", "Municipio", "municipios", opcional),
      activo,
    ],
    columnas: columnas(
      ["nombre", "Nombre"],
      ["municipioId", "Municipio", "municipios"],
      ["activo", "Activo"],
    ),
    crear: alta([nombre, relacion("municipioId", "Municipio", "municipios")]),
    acciones: [editar([nombre]), ...estados, eliminar],
  },
  {
    id: "lugares",
    titulo: "Lugares",
    grupo: "Geografía",
    roles: GESTION,
    filtros: [
      nombre,
      relacion("sectorId", "Sector", "sectores", opcional),
      activo,
    ],
    columnas: columnas(
      ["nombre", "Nombre"],
      ["sectorId", "Sector", "sectores"],
      ["activo", "Activo"],
    ),
    crear: alta([nombre, relacion("sectorId", "Sector", "sectores")]),
    acciones: [editar([nombre]), ...estados, eliminar],
  },
  {
    id: "vias",
    titulo: "Vías",
    grupo: "Geografía",
    roles: GESTION,
    filtros: [nombre, activo],
    columnas: columnas(
      ["nombre", "Nombre"],
      ["descripcion", "Descripción"],
      ["activo", "Activo"],
    ),
    crear: alta([nombreVia, descripcionVia]),
    acciones: [editar([nombreVia, descripcionVia]), ...estados, eliminar],
  },
  {
    id: "usuarios",
    titulo: "Usuarios",
    grupo: "Administración",
    roles: ADMIN,
    filtros: [
      texto("correo", "Correo"),
      relacion("rolId", "Rol", "roles", opcional),
      activo,
    ],
    columnas: columnas(
      ["correo", "Correo"],
      ["primerNombre", "Nombre"],
      ["primerApellido", "Apellido"],
      ["rol", "Rol"],
      ["activo", "Activo"],
      ["debeCambiarContrasena", "Cambio de contraseña pendiente"],
    ),
    crear: alta([
      ...nombres,
      texto("cedula", "Cédula", { minLength: 5, maxLength: 20 }),
      texto("correo", "Correo", { tipo: "email", maxLength: 120 }),
      { ...telefono, ...opcional },
      relacion("rolId", "Rol", "roles"),
      { ...conductor, ...opcional },
    ]),
    acciones: [
      editar([
        relacion("rolId", "Rol", "roles"),
        { ...conductor, ...opcional },
      ]),
      ...estados,
      accion(
        "restablecer",
        "Restablecer contraseña",
        "POST",
        "/restablecer-contrasena",
        ADMIN,
        [],
        {
          advertencia:
            "La contraseña actual dejará de funcionar. Se mostrará una contraseña temporal una sola vez.",
        },
      ),
    ],
  },
  ...[
    ["roles", "Roles"],
    ["estados-salida", "Estados de salida"],
    ["tipos-ruta", "Tipos de ruta"],
    ["tipos-incidencia", "Tipos de incidencia"],
  ].map(([id, titulo]) => ({
    id,
    titulo,
    grupo: "Catálogos",
    roles: GESTION,
    filtros: [],
    columnas: columnas(["nombre", "Nombre"]),
    acciones: [],
    catalogo: true,
  })),
];

export const perfilCampos = [...nombres, { ...telefono, ...opcional }];
export const rolesTodos = TODOS;
export const dias = [
  ["MONDAY", "Lunes"],
  ["TUESDAY", "Martes"],
  ["WEDNESDAY", "Miércoles"],
  ["THURSDAY", "Jueves"],
  ["FRIDAY", "Viernes"],
  ["SATURDAY", "Sábado"],
  ["SUNDAY", "Domingo"],
];
export const autorizado = (sesion, roles) =>
  roles.some((rol) => sesion.roles.includes(rol));
export const valorEn = (registro, campo) =>
  campo.split(".").reduce((valor, parte) => valor?.[parte], registro);

export function datosFormulario(campos, valores) {
  return Object.fromEntries(
    campos.map((campo) => {
      const valor = campo.normalizar
        ? campo.normalizar(valores[campo.nombre])
        : valores[campo.nombre];
      if (campo.tipo === "checkbox") return [campo.nombre, Boolean(valor)];
      if (campo.tipo === "dias") return [campo.nombre, valor || []];
      if (valor === "" || valor == null) return [campo.nombre, null];
      return [campo.nombre, campo.tipo === "number" ? Number(valor) : valor];
    }),
  );
}
