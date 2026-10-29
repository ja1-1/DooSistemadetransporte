import { modulos } from "./definiciones.js";
import { TablaRegistros } from "../componentes/DetalleRegistro.jsx";

export default function ResultadoOperacion({ datos, accion, alCerrar }) {
  if (datos == null) return null;
  const incidencias = modulos.find((modulo) => modulo.id === "incidencias");
  const asignaciones = modulos.find((modulo) => modulo.id === "asignaciones");
  const salidas = modulos.find((modulo) => modulo.id === "salidas");
  return (
    <section
      className="tarjeta resultado-operacion"
      aria-label="Resultado de la operación"
    >
      <div className="encabezado-tarjeta">
        <h2>{accion.titulo}: resultado</h2>
        <button className="boton texto" onClick={alCerrar}>
          Cerrar resultado
        </button>
      </div>
      {datos.contrasenaTemporal && (
        <div className="credencial-temporal">
          <strong>Contraseña temporal de {datos.usuario.correo}</strong>
          <p>
            Guárdala ahora. Solo se muestra en este resultado y el usuario
            deberá cambiarla al ingresar.
          </p>
          <output aria-label="Contraseña temporal">
            {datos.contrasenaTemporal}
          </output>
        </div>
      )}
      {Array.isArray(datos) && (
        <TablaRegistros
          datos={datos}
          columnas={
            accion.id === "disponibles"
              ? asignaciones.columnas
              : incidencias.columnas
          }
          nombre="Resultado de consulta"
        />
      )}
      {datos.salidasGeneradas && (
        <>
          <h3>Salidas generadas ({datos.salidasGeneradas.length})</h3>
          <TablaRegistros
            datos={datos.salidasGeneradas}
            columnas={salidas.columnas}
            nombre="Salidas generadas"
          />
          <h3>Salidas existentes ({datos.salidasExistentes.length})</h3>
          <TablaRegistros
            datos={datos.salidasExistentes}
            columnas={salidas.columnas}
            nombre="Salidas existentes"
          />
          <h3>Rechazos ({Object.keys(datos.rechazos).length})</h3>
          <ul>
            {Object.entries(datos.rechazos).map(([instante, mensaje]) => (
              <li key={instante}>
                <strong>{instante}</strong>: {mensaje}
              </li>
            ))}
          </ul>
        </>
      )}
      {datos.salidasSinCambiar && (
        <>
          <h3>
            Salidas que conservaron su programación (
            {datos.salidasSinCambiar.length})
          </h3>
          <TablaRegistros
            datos={datos.salidasSinCambiar}
            columnas={salidas.columnas}
            nombre="Salidas sin cambiar"
          />
        </>
      )}
      {datos.tipoIncidenciaId && (
        <p>Incidencia registrada: {datos.descripcion}</p>
      )}
    </section>
  );
}
