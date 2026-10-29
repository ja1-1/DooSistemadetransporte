import { clienteHttp } from "../cliente-http/clienteHttp.js";

export const rutaRecurso = (recurso) => `/api/v1/${recurso}`;

export function consultarRecurso(autorizacion, recurso, filtros = {}, signal) {
  const parametros = new URLSearchParams();
  Object.entries(filtros).forEach(([campo, valor]) => {
    if (valor !== "" && valor != null)
      parametros.set(campo, String(valor).trim());
  });
  return clienteHttp(
    `${rutaRecurso(recurso)}${parametros.size ? `?${parametros}` : ""}`,
    { autorizacion, signal },
  );
}

export function consultarRegistro(autorizacion, recurso, id, signal) {
  return clienteHttp(`${rutaRecurso(recurso)}/${encodeURIComponent(id)}`, {
    autorizacion,
    signal,
  });
}

export function ejecutarAccion(autorizacion, modulo, accion, registro, datos) {
  const base = accion.coleccion
    ? rutaRecurso(modulo.id)
    : modulo.rutaAccion?.(registro) ||
      `${rutaRecurso(modulo.id)}/${encodeURIComponent(registro.id)}`;
  if (accion.metodo === "GET") {
    return clienteHttp(
      `${base}${accion.sufijo}?${new URLSearchParams(datos)}`,
      { autorizacion },
    );
  }
  return clienteHttp(`${base}${accion.sufijo}`, {
    autorizacion,
    metodo: accion.metodo,
    datos: accion.campos.length ? datos : undefined,
  });
}

export function paginaDe(respuesta, pagina = 1, tamano = 20) {
  return {
    datos: respuesta.datos,
    total: respuesta.total,
    pagina,
    tamano,
    siguiente:
      respuesta.total == null
        ? respuesta.datos.length === tamano
        : pagina * tamano < respuesta.total,
  };
}
