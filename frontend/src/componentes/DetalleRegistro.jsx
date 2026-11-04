import { dias, valorEn } from "../modulos/definiciones.js";
import { NombreReferencia, nombreRegistro } from "./Referencias.jsx";
import { describirIncidencia, fechaLegible } from "./presentacion.js";

const etiquetas = {
  activo: "Activo",
  activa: "Activa",
  nombreRuta: "Nombre histórico de la ruta",
  instanteSalidaReal: "Salida real",
  instanteLlegadaReal: "Llegada real",
  estadoSalida: "Estado",
  salidaId: "Salida",
  "responsable.nombre": "Responsable",
  "responsable.rol": "Rol del responsable",
  instanteReporte: "Instante de reporte",
  debeCambiarContrasena: "Debe cambiar la contraseña",
  rol: "Rol",
  estado: "Estado",
};
const nombreDia = Object.fromEntries(dias);

export function mostrarValor(valor) {
  if (valor == null || valor === "") return "—";
  if (typeof valor === "boolean") return valor ? "Sí" : "No";
  if (Array.isArray(valor))
    return valor.map((dia) => nombreDia[dia] || dia).join(", ");
  return String(fechaLegible(valor));
}

export function DescripcionIncidencia({ descripcion }) {
  const { motivo, cambios } = describirIncidencia(descripcion);
  return (
    <div className="descripcion-incidencia">
      <p>{motivo || "—"}</p>
      {cambios.length > 0 && (
        <ul className="cambios-incidencia">
          {cambios.map((cambio, indice) => (
            <li key={`${cambio.etiqueta}-${indice}`}>
              <strong>{cambio.etiqueta}:</strong>{" "}
              {cambio.recurso ? (
                <NombreReferencia recurso={cambio.recurso} id={cambio.id} />
              ) : cambio.valor}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

function ValorPresentado({ campo, valor }) {
  return campo === "descripcion"
    ? <DescripcionIncidencia descripcion={valor} />
    : mostrarValor(valor);
}

export function TablaRegistros({
  datos,
  columnas,
  alAbrir,
  cargando = false,
  nombre = "Registros",
}) {
  return (
    <div className="contenedor-tabla">
      <table aria-label={nombre} aria-busy={cargando}>
        <thead>
          <tr>
            {columnas.map((columna) => (
              <th scope="col" key={columna.campo}>
                {columna.etiqueta}
              </th>
            ))}
            {alAbrir && <th scope="col">Detalle</th>}
          </tr>
        </thead>
        <tbody>
          {datos.map((registro) => (
            <tr key={registro.id}>
              {columnas.map((columna) => {
                const valor = valorEn(registro, columna.campo);
                return (
                  <td key={columna.campo}>
                    {columna.recurso && valor ? (
                      <NombreReferencia recurso={columna.recurso} id={valor} />
                    ) : (
                      <ValorPresentado campo={columna.campo} valor={valor} />
                    )}
                  </td>
                );
              })}
              {alAbrir && (
                <td>
                  <button
                    type="button"
                    className="boton texto"
                    onClick={() => alAbrir(registro)}
                  >
                    Abrir
                    <span className="solo-lectores">
                      {" "}
                      {nombreRegistro(registro)}
                    </span>
                  </button>
                </td>
              )}
            </tr>
          ))}
          {!cargando && datos.length === 0 && (
            <tr>
              <td
                className="vacio"
                colSpan={columnas.length + (alAbrir ? 1 : 0)}
              >
                No hay registros para estos filtros.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

function camposDe(registro, prefijo = "") {
  return Object.entries(registro).flatMap(([campo, valor]) => {
    const ruta = prefijo ? `${prefijo}.${campo}` : campo;
    if (valor && typeof valor === "object" && !Array.isArray(valor))
      return camposDe(valor, ruta);
    return [[ruta, valor]];
  });
}

export default function DetalleRegistro({
  registro,
  modulo,
  resolverReferencias = true,
}) {
  const campos = [
    ...(modulo.crear?.campos || []),
    ...(modulo.filtros || []),
    ...(modulo.acciones || []).flatMap((accion) => accion.campos),
  ];
  return (
    <dl className="detalle-registro">
      {camposDe(registro).map(([ruta, valor]) => {
        const campo = campos.find(
          (candidato) => (candidato.origen || candidato.nombre) === ruta,
        );
        const columna = modulo.columnas.find(
          (candidato) => candidato.campo === ruta,
        );
        const recurso = columna?.recurso || campo?.recurso;
        const opcion =
          campo?.tipo === "select" && Array.isArray(campo.opciones)
            ? campo.opciones.find(
                (item) => (Array.isArray(item) ? item[0] : item) === valor,
              )
            : undefined;
        const identificador = /(^|\.)(id|\w+Id)$/.test(ruta);
        if (identificador && (!resolverReferencias || !recurso) && !opcion)
          return null;
        const etiqueta =
          etiquetas[ruta] ||
          campo?.etiqueta ||
          columna?.etiqueta ||
          ruta.replace(/([A-Z])/g, " $1").replaceAll(".", " · ");
        return (
          <div key={ruta}>
            <dt>{etiqueta}</dt>
            <dd>
              {opcion ? (
                Array.isArray(opcion) ? opcion[1] : opcion
              ) : resolverReferencias && recurso && valor ? (
                <NombreReferencia recurso={recurso} id={valor} />
              ) : (
                <ValorPresentado campo={ruta} valor={valor} />
              )}
            </dd>
          </div>
        );
      })}
    </dl>
  );
}
