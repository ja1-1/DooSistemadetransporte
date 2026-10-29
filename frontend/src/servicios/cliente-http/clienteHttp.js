export function autorizacionBasica(usuario, clave) {
  const bytes = new TextEncoder().encode(`${usuario}:${clave}`);
  return `Basic ${btoa(String.fromCharCode(...bytes))}`;
}

export async function clienteHttp(
  ruta,
  { autorizacion, metodo = "GET", datos, signal } = {},
) {
  let respuesta;
  try {
    respuesta = await fetch(ruta, {
      method: metodo,
      headers: {
        Accept: "application/json",
        Authorization: autorizacion,
        ...(datos ? { "Content-Type": "application/json" } : {}),
      },
      body: datos ? JSON.stringify(datos) : undefined,
      signal,
    });
  } catch (error) {
    if (error?.name === "AbortError") throw error;
    throw new Error("No se pudo conectar con el servicio. Intenta nuevamente.");
  }
  if (respuesta.ok && respuesta.status === 204) return {};
  const cuerpo = await respuesta.json().catch(() => null);
  if (!respuesta.ok) {
    const mensaje =
      respuesta.status === 401
        ? "Usuario o contraseña incorrectos."
        : cuerpo?.mensaje || "No fue posible completar la operación.";
    throw Object.assign(new Error(mensaje), { estado: respuesta.status });
  }
  const esSesion =
    typeof cuerpo?.usuario === "string" &&
    Array.isArray(cuerpo.roles) &&
    cuerpo.roles.every((rol) => typeof rol === "string");
  if (
    cuerpo === null ||
    typeof cuerpo !== "object" ||
    Array.isArray(cuerpo) ||
    (!Object.hasOwn(cuerpo, "datos") && !esSesion) ||
    ("datos" in cuerpo && cuerpo.datos === null) ||
    (Array.isArray(cuerpo.datos) && cuerpo.datos.some((dato) => dato == null))
  ) {
    throw new Error("El servicio devolvió una respuesta inválida.");
  }
  return cuerpo;
}
