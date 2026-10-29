import { createContext, useContext, useEffect, useMemo, useState } from "react";
import {
  consultarRegistro,
  consultarRecurso,
} from "../servicios/api/recursos.js";
import { autorizado, modulos } from "../modulos/definiciones.js";

const Referencias = createContext(null);
export const nombreRegistro = (registro) =>
  registro.nombre ||
  registro.placa ||
  registro.correo ||
  [registro.primerNombre, registro.primerApellido].filter(Boolean).join(" ") ||
  registro.nombreRuta ||
  registro.descripcion ||
  (registro.fechaInicio
    ? `Asignación del ${registro.fechaInicio}`
    : "Registro seleccionado");
const puedeConsultar = (sesion, recurso) => {
  const modulo = modulos.find((item) => item.id === recurso);
  return !modulo || autorizado(sesion, modulo.roles);
};

export function ProveedorReferencias({ sesion, revision, children }) {
  const valor = useMemo(() => {
    const consultas = new Map();
    async function describir(recurso, datos) {
      if (recurso === "asignaciones")
        return `${await nombre("vehiculos", datos.vehiculoId)} · ${await nombre("conductores", datos.conductorId)} · ${datos.fechaInicio}`;
      if (recurso === "programaciones-ruta")
        return `${await nombre("rutas", datos.rutaId)} · ${datos.horario.horaSalida}`;
      if (recurso === "municipios" && datos.departamento)
        return `${datos.nombre} (${datos.departamento})`;
      if (recurso === "sectores")
        return `${datos.nombre} · ${await nombre("municipios", datos.municipioId)}`;
      if (recurso === "lugares")
        return `${datos.nombre} · ${await nombre("sectores", datos.sectorId)}`;
      if (recurso === "rutas") return `${datos.nombre} · ${datos.descripcion}`;
      if (recurso === "conductores")
        return `${nombreRegistro(datos)} · ${datos.cedula}`;
      if (recurso === "salidas")
        return `${datos.nombreRuta} · ${datos.ventana.instanteSalidaProgramado}`;
      return nombreRegistro(datos);
    }
    function recordar(recurso, datos) {
      const clave = `${recurso}/${datos.id}`;
      if (!consultas.has(clave))
        consultas.set(clave, describir(recurso, datos));
      return consultas.get(clave);
    }
    async function nombre(recurso, id) {
      if (!id) return "—";
      if (!puedeConsultar(sesion, recurso))
        return "Consulta no disponible para tu rol";
      const clave = `${recurso}/${id}`;
      if (!consultas.has(clave)) {
        consultas.set(
          clave,
          consultarRegistro(sesion.autorizacion, recurso, id)
            .then(({ datos }) => describir(recurso, datos))
            .catch(() => "Referencia no disponible"),
        );
      }
      return consultas.get(clave);
    }
    return { nombre, recordar, autorizacion: sesion.autorizacion };
  }, [sesion, revision]);
  return <Referencias.Provider value={valor}>{children}</Referencias.Provider>;
}

export function NombreReferencia({ recurso, id }) {
  const referencias = useContext(Referencias);
  const [nombre, setNombre] = useState(null);
  useEffect(() => {
    let vigente = true;
    setNombre(null);
    referencias.nombre(recurso, id).then((valor) => {
      if (vigente) setNombre(valor);
    });
    return () => {
      vigente = false;
    };
  }, [referencias, recurso, id]);
  return <span>{nombre || "Cargando referencia…"}</span>;
}

const busquedaPor = {
  conductores: "cedula",
  vehiculos: "placa",
  usuarios: "correo",
  asignaciones: null,
  "programaciones-ruta": null,
};

export function SelectorRelacion({
  campo,
  id,
  valor,
  alCambiar,
  filtro = false,
  disabled = false,
}) {
  const { autorizacion, recordar } = useContext(Referencias);
  const [pagina, setPagina] = useState(1);
  const [busqueda, setBusqueda] = useState("");
  const [filtroActual, setFiltroActual] = useState("");
  const [resultado, setResultado] = useState({ datos: [], siguiente: false });
  const [error, setError] = useState("");
  const [cargando, setCargando] = useState(true);
  const catalogo = [
    "roles",
    "tipos-ruta",
    "estados-salida",
    "tipos-incidencia",
  ].includes(campo.recurso);
  const criterio = Object.hasOwn(busquedaPor, campo.recurso)
    ? busquedaPor[campo.recurso]
    : "nombre";
  useEffect(() => {
    const controlador = new AbortController();
    setCargando(true);
    setError("");
    consultarRecurso(
      autorizacion,
      campo.recurso,
      catalogo
        ? {}
        : { pagina, tamano: 20, ...(criterio && { [criterio]: filtroActual }) },
      controlador.signal,
    )
      .then(async (respuesta) => {
        const datos = await Promise.all(
          respuesta.datos.map(async (registro) => ({
            ...registro,
            etiqueta: await recordar(campo.recurso, registro),
          })),
        );
        if (!controlador.signal.aborted)
          setResultado({
            datos,
            siguiente:
              respuesta.total == null
                ? datos.length === 20
                : pagina * 20 < respuesta.total,
          });
      })
      .catch((fallo) => {
        if (!controlador.signal.aborted) {
          setResultado({ datos: [], siguiente: false });
          setError(fallo.message);
        }
      })
      .finally(() => {
        if (!controlador.signal.aborted) setCargando(false);
      });
    return () => controlador.abort();
  }, [
    autorizacion,
    recordar,
    campo.recurso,
    pagina,
    filtroActual,
    catalogo,
    criterio,
  ]);
  const estaEnPagina = resultado.datos.some(
    (registro) => registro.id === valor,
  );
  return (
    <div className="selector-relacion">
      {!catalogo && criterio && (
        <div className="buscar-relacion">
          <input
            aria-label={`Buscar ${campo.etiqueta.toLowerCase()} por ${criterio}`}
            placeholder={`Buscar por ${criterio}`}
            value={busqueda}
            onChange={(evento) => setBusqueda(evento.target.value)}
            disabled={disabled}
          />
          <button
            type="button"
            className="boton texto"
            disabled={disabled || cargando}
            onClick={() => {
              setPagina(1);
              setFiltroActual(busqueda);
            }}
          >
            Buscar opciones
          </button>
        </div>
      )}
      <select
        id={id}
        value={valor || ""}
        onChange={(evento) => alCambiar(evento.target.value)}
        required={!filtro && !campo.opcional}
        disabled={disabled}
        aria-describedby={error ? `${id}-error` : undefined}
      >
        <option value="">
          {filtro || campo.opcional ? "Sin selección" : "Selecciona una opción"}
        </option>
        {valor && !estaEnPagina && (
          <option value={valor}>Selección actual</option>
        )}
        {resultado.datos.map((registro) => (
          <option key={registro.id} value={registro.id}>
            {registro.etiqueta}
            {registro.cedula ? ` · ${registro.cedula}` : ""}
          </option>
        ))}
      </select>
      {valor && (
        <small>
          <NombreReferencia recurso={campo.recurso} id={valor} />
        </small>
      )}
      {cargando && <small role="status">Cargando opciones…</small>}
      {error && (
        <small id={`${id}-error`} role="alert">
          {error}
        </small>
      )}
      {!catalogo && (
        <div className="paginas-opciones">
          <button
            type="button"
            className="boton texto"
            aria-label={`Opciones anteriores de ${campo.etiqueta.toLowerCase()}`}
            disabled={disabled || cargando || pagina === 1}
            onClick={() => setPagina(pagina - 1)}
          >
            Anterior
          </button>
          <small>Página {pagina}</small>
          <button
            type="button"
            className="boton texto"
            aria-label={`Opciones siguientes de ${campo.etiqueta.toLowerCase()}`}
            disabled={disabled || cargando || !resultado.siguiente}
            onClick={() => setPagina(pagina + 1)}
          >
            Siguiente
          </button>
        </div>
      )}
    </div>
  );
}
