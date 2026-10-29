import { useEffect, useState } from "react";
import { consultarRecurso, paginaDe } from "../servicios/api/recursos.js";

export const CONSULTA_INICIAL = Object.freeze({ pagina: 1, tamano: 20 });

export default function useListadoRegistros(sesion, modulo) {
  const [consulta, setConsulta] = useState({ ...CONSULTA_INICIAL });
  const [resultado, setResultado] = useState({
    datos: [],
    total: undefined,
    siguiente: false,
  });
  const [revision, setRevision] = useState(0);
  const [cargando, setCargando] = useState(true);
  const [errorConsulta, setErrorConsulta] = useState("");

  useEffect(() => {
    const controlador = new AbortController();
    setCargando(true);
    setErrorConsulta("");
    consultarRecurso(
      sesion.autorizacion,
      modulo.id,
      modulo.catalogo ? {} : consulta,
      controlador.signal,
    )
      .then((datos) => {
        if (!controlador.signal.aborted)
          setResultado(paginaDe(datos, consulta.pagina));
      })
      .catch((fallo) => {
        if (!controlador.signal.aborted) {
          setResultado({ datos: [] });
          setErrorConsulta(fallo.message);
        }
      })
      .finally(() => {
        if (!controlador.signal.aborted) setCargando(false);
      });
    return () => controlador.abort();
  }, [sesion.autorizacion, modulo.id, modulo.catalogo, consulta, revision]);

  return {
    consulta,
    setConsulta,
    resultado,
    cargando,
    errorConsulta,
    recargar: () => setRevision((valor) => valor + 1),
  };
}
