import FormularioCampos from "../componentes/FormularioCampos.jsx";
import DetalleRegistro from "../componentes/DetalleRegistro.jsx";
import { nombreRegistro } from "../componentes/Referencias.jsx";

function FormularioAccion({
  accion,
  registro,
  valores,
  alCambiar,
  guardando,
  alEjecutar,
  alVolver,
}) {
  return (
    <form onSubmit={alEjecutar} className="formulario-modulo">
      {accion.ayuda && <p>{accion.ayuda}</p>}
      {accion.advertencia && (
        <p className="mensaje advertencia">{accion.advertencia}</p>
      )}
      {!accion.coleccion && registro && (
        <p>
          Registro: <strong>{nombreRegistro(registro)}</strong>
        </p>
      )}
      <FormularioCampos
        campos={accion.campos}
        valores={valores}
        alCambiar={alCambiar}
        prefijo="accion"
        disabled={guardando}
      />
      <div className="acciones-formulario">
        <button className="boton principal" disabled={guardando}>
          {guardando
            ? "Procesando…"
            : `Confirmar ${accion.titulo.toLowerCase()}`}
        </button>
        <button
          type="button"
          className="boton secundario"
          disabled={guardando}
          onClick={alVolver}
        >
          Volver
        </button>
      </div>
    </form>
  );
}

function DetalleConAcciones({ registro, modulo, acciones, alPreparar }) {
  return (
    <>
      <DetalleRegistro registro={registro} modulo={modulo} />
      <div className="acciones-registro">
        {acciones.map((item) => (
          <button
            className="boton secundario"
            key={item.id}
            onClick={() => alPreparar(item)}
          >
            {item.titulo}
          </button>
        ))}
      </div>
    </>
  );
}

export default function PanelTrabajo({
  modulo,
  accion,
  registro,
  valores,
  alCambiarValores,
  acciones,
  guardando,
  tituloRef,
  alEjecutar,
  alPreparar,
  alVolver,
  alCerrar,
}) {
  return (
    <section className="tarjeta panel-trabajo" aria-labelledby="titulo-panel">
      <div className="encabezado-tarjeta">
        <h2 id="titulo-panel" tabIndex={-1} ref={tituloRef}>
          {accion
            ? `${accion.titulo} · ${modulo.titulo}`
            : `Detalle · ${modulo.titulo}`}
        </h2>
        <button className="boton texto" disabled={guardando} onClick={alCerrar}>
          Cerrar panel
        </button>
      </div>
      {accion ? (
        <FormularioAccion
          accion={accion}
          registro={registro}
          valores={valores}
          alCambiar={alCambiarValores}
          guardando={guardando}
          alEjecutar={alEjecutar}
          alVolver={alVolver}
        />
      ) : (
        <DetalleConAcciones
          registro={registro}
          modulo={modulo}
          acciones={acciones}
          alPreparar={alPreparar}
        />
      )}
    </section>
  );
}
