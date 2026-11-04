const fecha = new Intl.DateTimeFormat("es-CO", {
  day: "numeric",
  month: "short",
  year: "numeric",
  timeZone: "UTC",
});
const instante = new Intl.DateTimeFormat("es-CO", {
  day: "numeric",
  month: "short",
  year: "numeric",
  hour: "numeric",
  minute: "2-digit",
  hour12: true,
  timeZone: "America/Bogota",
});

export function fechaLegible(valor) {
  if (typeof valor !== "string") return valor;
  const soloFecha = /^\d{4}-\d{2}-\d{2}$/.test(valor);
  const fechaHora = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}(?::\d{2}(?:\.\d+)?)?(?:Z|[+-]\d{2}:\d{2})?$/.test(valor);
  if (!soloFecha && !fechaHora) return valor;
  const normalizado = soloFecha
    ? `${valor}T00:00:00Z`
    : valor.replace(/(\.\d{3})\d+/, "$1") + (/(?:Z|[+-]\d{2}:\d{2})$/.test(valor) ? "" : "-05:00");
  const momento = new Date(normalizado);
  if (Number.isNaN(momento.getTime())) return valor;
  return (soloFecha ? fecha : instante).format(momento);
}

const detalles = {
  "conductor anterior": { etiqueta: "Conductor anterior", recurso: "conductores", siguiente: "Conductor nuevo" },
  "nuevo conductor": { etiqueta: "Conductor nuevo", recurso: "conductores" },
  "vehículo anterior": { etiqueta: "Vehículo anterior", siguiente: "Vehículo nuevo" },
  "vehículo nuevo": { etiqueta: "Vehículo nuevo" },
  "asignación anterior": { etiqueta: "Asignación anterior", recurso: "asignaciones", siguiente: "Asignación nueva" },
  "salida de refuerzo": { etiqueta: "Salida de refuerzo", recurso: "salidas" },
  "llegada anterior": { etiqueta: "Llegada anterior", siguiente: "Llegada nueva" },
  "ventana anterior": { etiqueta: "Horario anterior", siguiente: "Horario nuevo" },
};
const identificador = /^[\da-f]{8}-(?:[\da-f]{4}-){3}[\da-f]{12}$/i;

function valorLegible(valor) {
  const ventana = /^VentanaViaje\[instanteSalidaProgramado=(.+), instanteLlegadaEstimado=(.+)\]$/.exec(valor);
  return ventana
    ? `${fechaLegible(ventana[1])} → ${fechaLegible(ventana[2])}`
    : fechaLegible(valor);
}

export function describirIncidencia(descripcion) {
  const partes = String(descripcion ?? "").split(/;\s*/);
  const motivo = [partes.shift()];
  const cambios = [];
  let anterior;
  for (const parte of partes) {
    const coincidencia = /^([^=]+)=([\s\S]+)$/.exec(parte);
    const campo = coincidencia?.[1].trim();
    const valor = coincidencia?.[2].trim();
    const definido = Object.hasOwn(detalles, campo) ? detalles[campo] : null;
    const configuracion = definido ||
      ((campo === "nuevo" || campo === "nueva") && anterior?.siguiente
        ? { etiqueta: anterior.siguiente, recurso: anterior.recurso }
        : null);
    if (!configuracion || !valor) {
      motivo.push(parte);
      continue;
    }
    cambios.push({
      etiqueta: configuracion.etiqueta,
      ...(configuracion.recurso && identificador.test(valor)
        ? { recurso: configuracion.recurso, id: valor }
        : { valor: valorLegible(valor) }),
    });
    if (definido) anterior = configuracion;
  }
  return { motivo: motivo.join("; "), cambios };
}

export const resumenDescripcion = (descripcion) => describirIncidencia(descripcion).motivo;
