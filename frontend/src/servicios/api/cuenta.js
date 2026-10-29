import {
  autorizacionBasica,
  clienteHttp,
} from "../cliente-http/clienteHttp.js";

const RUTA_SESION = "/api/v1/sesion";
const RUTA_PERFIL = "/api/v1/usuarios/me";
const RUTA_CONTRASENA = `${RUTA_PERFIL}/contrasena`;

export async function iniciarSesion(usuario, clave) {
  const autorizacion = autorizacionBasica(usuario.trim(), clave);
  const sesion = await clienteHttp(RUTA_SESION, { autorizacion });
  return { ...sesion, autorizacion };
}

export function consultarPerfil(autorizacion, signal) {
  return clienteHttp(RUTA_PERFIL, { autorizacion, signal });
}

export function actualizarPerfil(autorizacion, datos) {
  return clienteHttp(RUTA_PERFIL, { autorizacion, metodo: "PUT", datos });
}

export function cambiarContrasena(autorizacion, contrasenaActual, contrasenaNueva) {
  return clienteHttp(RUTA_CONTRASENA, {
    autorizacion,
    metodo: "POST",
    datos: { contrasenaActual, contrasenaNueva },
  });
}
