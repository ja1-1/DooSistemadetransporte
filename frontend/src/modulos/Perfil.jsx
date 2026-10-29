import { useEffect, useState } from "react";
import {
  actualizarPerfil,
  cambiarContrasena,
  consultarPerfil,
  iniciarSesion,
} from "../servicios/api/cuenta.js";
import FormularioCampos from "../componentes/FormularioCampos.jsx";
import DetalleRegistro from "../componentes/DetalleRegistro.jsx";
import { datosFormulario, modulos, perfilCampos } from "./definiciones.js";

export default function Perfil({ sesion, alRenovarSesion, alProcesar }) {
  const [registro, setRegistro] = useState(null);
  const [valores, setValores] = useState({});
  const [claves, setClaves] = useState({
    actual: "",
    nueva: "",
    confirmacion: "",
  });
  const [error, setError] = useState("");
  const [aviso, setAviso] = useState("");
  const [guardando, setGuardando] = useState(false);
  useEffect(() => {
    const controlador = new AbortController();
    consultarPerfil(sesion.autorizacion, controlador.signal)
      .then(({ datos }) => {
        if (!controlador.signal.aborted) {
          setRegistro(datos);
          setValores(
            Object.fromEntries(
              perfilCampos.map((campo) => [
                campo.nombre,
                datos[campo.nombre] || "",
              ]),
            ),
          );
        }
      })
      .catch((fallo) => {
        if (!controlador.signal.aborted) setError(fallo.message);
      });
    return () => controlador.abort();
  }, [sesion.autorizacion]);

  async function procesar(evento, operacion, mensajeExito) {
    evento.preventDefault();
    setError("");
    setAviso("");
    setGuardando(true);
    alProcesar(true);
    try {
      await operacion();
      setAviso(mensajeExito);
    } catch (fallo) {
      setError(fallo.message);
    } finally {
      setGuardando(false);
      alProcesar(false);
    }
  }

  function guardarPerfil(evento) {
    return procesar(
      evento,
      async () => {
        const { datos } = await actualizarPerfil(
          sesion.autorizacion,
          datosFormulario(perfilCampos, valores),
        );
        setRegistro(datos);
      },
      "Información personal actualizada.",
    );
  }

  function guardarContrasena(evento) {
    if (claves.nueva !== claves.confirmacion) {
      evento.preventDefault();
      setAviso("");
      setError("Las contraseñas no coinciden.");
      return;
    }
    return procesar(
      evento,
      async () => {
        const { datos } = await cambiarContrasena(
          sesion.autorizacion,
          claves.actual,
          claves.nueva,
        );
        setRegistro(datos);
        alRenovarSesion(await iniciarSesion(sesion.usuario, claves.nueva));
        setClaves({ actual: "", nueva: "", confirmacion: "" });
      },
      "Contraseña cambiada. La sesión utiliza la nueva contraseña.",
    );
  }

  return (
    <>
      <h1>Mi perfil</h1>
      {error && (
        <p role="alert" className="mensaje error">
          {error}
        </p>
      )}
      {aviso && (
        <p role="status" className="mensaje exito">
          {aviso}
        </p>
      )}
      {registro ? (
        <div className="distribucion-perfil">
          <section className="tarjeta panel-trabajo">
            <div className="encabezado-tarjeta">
              <h2>Información personal</h2>
            </div>
            <DetalleRegistro
              registro={registro}
              modulo={modulos.find((modulo) => modulo.id === "usuarios")}
              resolverReferencias={false}
            />
            <form
              className="formulario-modulo"
              onSubmit={guardarPerfil}
            >
              <FormularioCampos
                campos={perfilCampos}
                valores={valores}
                alCambiar={setValores}
                prefijo="perfil"
                disabled={guardando}
              />
              <button className="boton principal" disabled={guardando}>
                Guardar perfil
              </button>
            </form>
          </section>
          <section className="tarjeta panel-trabajo">
            <div className="encabezado-tarjeta">
              <h2>Cambiar contraseña</h2>
            </div>
            <form
              className="formulario-modulo"
              onSubmit={guardarContrasena}
            >
              <label htmlFor="clave-actual">Contraseña actual</label>
              <input
                id="clave-actual"
                type="password"
                autoComplete="current-password"
                required
                value={claves.actual}
                disabled={guardando}
                onChange={(evento) =>
                  setClaves({ ...claves, actual: evento.target.value })
                }
              />
              <label htmlFor="clave-nueva">Nueva contraseña</label>
              <input
                id="clave-nueva"
                type="password"
                minLength={8}
                autoComplete="new-password"
                required
                value={claves.nueva}
                disabled={guardando}
                onChange={(evento) =>
                  setClaves({ ...claves, nueva: evento.target.value })
                }
              />
              <label htmlFor="clave-confirmacion">Confirmar contraseña</label>
              <input
                id="clave-confirmacion"
                type="password"
                minLength={8}
                autoComplete="new-password"
                required
                value={claves.confirmacion}
                disabled={guardando}
                onChange={(evento) =>
                  setClaves({ ...claves, confirmacion: evento.target.value })
                }
              />
              <button className="boton principal" disabled={guardando}>
                Cambiar contraseña
              </button>
            </form>
          </section>
        </div>
      ) : (
        !error && <p role="status">Cargando perfil…</p>
      )}
    </>
  );
}
