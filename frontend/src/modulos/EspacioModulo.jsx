import { useEffect, useRef, useState } from "react";
import { autorizado, datosFormulario, valorEn } from "./definiciones.js";
import {
  consultarRegistro,
  ejecutarAccion,
} from "../servicios/api/recursos.js";
import ResultadoOperacion from "./ResultadoOperacion.jsx";
import ListadoRegistros from "./ListadoRegistros.jsx";
import PanelTrabajo from "./PanelTrabajo.jsx";
import useListadoRegistros from "./useListadoRegistros.js";

const ACCIONES_QUE_DEVUELVEN_REGISTRO = ["relevar", "refuerzo"];

function valorVacio(campo) {
  return campo.valorInicial ?? (campo.tipo === "checkbox" ? false : "");
}

function valoresIniciales(accion, registro) {
  return Object.fromEntries(
    accion.campos.map((campo) => {
      if (accion.coleccion) return [campo.nombre, valorVacio(campo)];
      const actual = valorEn(registro, campo.origen || campo.nombre);
      return [campo.nombre, actual ?? valorVacio(campo)];
    }),
  );
}

function necesitaResultado(accion, datos) {
  return (
    accion.metodo === "GET" ||
    datos?.contrasenaTemporal ||
    datos?.salidasGeneradas ||
    datos?.salidasSinCambiar ||
    accion.id === "incidencia"
  );
}

function accionesDisponibles(modulo, sesion, registro) {
  const activo = registro?.activo ?? registro?.activa;
  return (modulo.acciones || []).filter(
    (item) =>
      autorizado(sesion, item.roles) &&
      (item.estado == null || activo === item.estado),
  );
}

export default function EspacioModulo({
  modulo,
  sesion,
  alModificar,
  alProcesar,
}) {
  const listado = useListadoRegistros(sesion, modulo);
  const [registro, setRegistro] = useState(null);
  const [accion, setAccion] = useState(null);
  const [valores, setValores] = useState({});
  const [guardando, setGuardando] = useState(false);
  const [abriendo, setAbriendo] = useState(false);
  const [error, setError] = useState("");
  const [aviso, setAviso] = useState("");
  const [respuesta, setRespuesta] = useState(null);
  const tituloPanel = useRef(null);
  const detallePendiente = useRef(null);
  useEffect(() => () => detallePendiente.current?.abort(), []);
  useEffect(() => {
    if (accion || registro) tituloPanel.current?.focus();
  }, [accion, registro]);

  async function abrir(fila) {
    detallePendiente.current?.abort();
    const controlador = new AbortController();
    detallePendiente.current = controlador;
    setAbriendo(true);
    setError("");
    setAccion(null);
    setRespuesta(null);
    setRegistro(null);
    try {
      const { datos } = await consultarRegistro(
        sesion.autorizacion,
        modulo.id,
        fila.id,
        controlador.signal,
      );
      if (!controlador.signal.aborted) setRegistro(datos);
    } catch (fallo) {
      if (!controlador.signal.aborted) setError(fallo.message);
    } finally {
      if (!controlador.signal.aborted) setAbriendo(false);
    }
  }

  function preparar(nueva) {
    detallePendiente.current?.abort();
    setAbriendo(false);
    setError("");
    setAviso("");
    setRespuesta(null);
    setAccion(nueva);
    setValores(valoresIniciales(nueva, registro));
  }

  async function registroActualizado(datos) {
    if (accion.metodo === "DELETE") return null;
    if (accion.coleccion || ACCIONES_QUE_DEVUELVEN_REGISTRO.includes(accion.id))
      return datos?.usuario || datos;
    try {
      return (
        await consultarRegistro(sesion.autorizacion, modulo.id, registro.id)
      ).datos;
    } catch {
      return null;
    }
  }

  async function ejecutar(evento) {
    evento.preventDefault();
    setError("");
    setAviso("");
    setGuardando(true);
    alProcesar(true);
    try {
      const salida = await ejecutarAccion(
        sesion.autorizacion,
        modulo,
        accion,
        registro,
        datosFormulario(accion.campos, valores),
      );
      setRespuesta(
        necesitaResultado(accion, salida.datos)
          ? { datos: salida.datos, accion }
          : null,
      );
      setAviso(`${accion.titulo}: operación completada.`);
      if (accion.metodo !== "GET") {
        listado.recargar();
        alModificar();
        setRegistro(await registroActualizado(salida.datos));
      }
      setAccion(null);
      setValores({});
    } catch (fallo) {
      setError(fallo.message);
    } finally {
      setGuardando(false);
      alProcesar(false);
    }
  }

  return (
    <>
      <header className="cabecera-pagina">
        <h1>{modulo.titulo}</h1>
        <div className="acciones-cabecera">
          {modulo.consulta && autorizado(sesion, modulo.consulta.roles) && (
            <button
              className="boton secundario"
              onClick={() => preparar(modulo.consulta)}
              disabled={guardando}
            >
              {modulo.consulta.titulo}
            </button>
          )}
          {modulo.crear && autorizado(sesion, modulo.crear.roles) && (
            <button
              className="boton principal"
              onClick={() => preparar(modulo.crear)}
              disabled={guardando}
            >
              Nuevo registro
            </button>
          )}
        </div>
      </header>
      {aviso && (
        <p className="mensaje exito" role="status">
          {aviso}
        </p>
      )}
      {error && (
        <p className="mensaje error" role="alert">
          {error}
        </p>
      )}
      <ListadoRegistros
        modulo={modulo}
        listado={listado}
        guardando={guardando}
        alAbrir={abrir}
      />
      {abriendo && <p role="status">Cargando detalle…</p>}
      {(accion || registro) && (
        <PanelTrabajo
          modulo={modulo}
          accion={accion}
          registro={registro}
          valores={valores}
          alCambiarValores={setValores}
          acciones={accionesDisponibles(modulo, sesion, registro)}
          guardando={guardando}
          tituloRef={tituloPanel}
          alEjecutar={ejecutar}
          alPreparar={preparar}
          alVolver={() => {
            setAccion(null);
            setError("");
            setValores({});
          }}
          alCerrar={() => {
            setAccion(null);
            setRegistro(null);
            setError("");
          }}
        />
      )}
      {respuesta && (
        <ResultadoOperacion
          {...respuesta}
          alCerrar={() => setRespuesta(null)}
        />
      )}
    </>
  );
}
