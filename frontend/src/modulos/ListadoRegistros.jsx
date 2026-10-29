import { useState } from "react";
import FormularioCampos from "../componentes/FormularioCampos.jsx";
import { TablaRegistros } from "../componentes/DetalleRegistro.jsx";
import { CONSULTA_INICIAL } from "./useListadoRegistros.js";

function textoTotal(resultado) {
  return resultado.total == null
    ? `${resultado.datos.length} en esta página`
    : `${resultado.total} resultados`;
}

export default function ListadoRegistros({
  modulo,
  listado,
  guardando,
  alAbrir,
}) {
  const [filtros, setFiltros] = useState({});
  const { consulta, setConsulta, resultado, cargando, errorConsulta } =
    listado;
  const cambiarPagina = (desplazamiento) =>
    setConsulta({ ...consulta, pagina: consulta.pagina + desplazamiento });

  return (
    <section
      className="tarjeta listado"
      aria-label={`Listado de ${modulo.titulo.toLowerCase()}`}
    >
      <div className="encabezado-tarjeta">
        <h2>Registros de {modulo.titulo.toLowerCase()}</h2>
        <span className="etiqueta-seccion">{textoTotal(resultado)}</span>
      </div>
      {modulo.filtros.length > 0 && (
        <form
          className="filtros filtros-modulo"
          onSubmit={(evento) => {
            evento.preventDefault();
            setConsulta({ ...filtros, ...CONSULTA_INICIAL });
          }}
        >
          <FormularioCampos
            campos={modulo.filtros.map((campo) =>
              typeof campo.opciones === "function"
                ? { ...campo, opciones: campo.opciones(resultado.datos) }
                : campo,
            )}
            valores={filtros}
            alCambiar={setFiltros}
            prefijo="filtro"
            filtro
            disabled={guardando}
          />
          <button className="boton secundario" disabled={cargando || guardando}>
            Buscar
          </button>
          <button
            type="button"
            className="boton texto"
            disabled={guardando}
            onClick={() => {
              setFiltros({});
              setConsulta({ ...CONSULTA_INICIAL });
            }}
          >
            Limpiar filtros
          </button>
        </form>
      )}
      {errorConsulta && (
        <p className="mensaje error" role="alert">
          {errorConsulta}
        </p>
      )}
      {cargando && (
        <p className="cargando" role="status">
          Cargando registros…
        </p>
      )}
      <TablaRegistros
        datos={resultado.datos}
        columnas={modulo.columnas}
        cargando={cargando}
        alAbrir={guardando ? undefined : alAbrir}
        nombre={modulo.titulo}
      />
      {!modulo.catalogo && (
        <nav className="paginacion" aria-label="Páginas de registros">
          <span>Página {consulta.pagina}</span>
          <div>
            <button
              className="boton texto"
              disabled={cargando || guardando || consulta.pagina === 1}
              onClick={() => cambiarPagina(-1)}
            >
              Anterior
            </button>
            <button
              className="boton texto"
              disabled={cargando || guardando || !resultado.siguiente}
              onClick={() => cambiarPagina(1)}
            >
              Siguiente
            </button>
          </div>
        </nav>
      )}
    </section>
  );
}
