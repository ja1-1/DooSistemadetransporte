import { useState } from "react";
import Ingreso from "./seguridad/Ingreso.jsx";
import EspacioModulo from "./modulos/EspacioModulo.jsx";
import Perfil from "./modulos/Perfil.jsx";
import { autorizado, modulos } from "./modulos/definiciones.js";
import { ProveedorReferencias } from "./componentes/Referencias.jsx";
import "./estilos/aplicacion.css";

const NOMBRES_ROL = [
  ["ADMINISTRADOR", "Administrador"],
  ["JEFE_OPERACION", "Jefe de operación"],
];

function nombreRol(roles) {
  const encontrado = NOMBRES_ROL.find(([rol]) => roles.includes(rol));
  return encontrado ? encontrado[1] : "Conductor";
}

function moduloInicial(roles) {
  return roles.includes("CONDUCTOR") ? "salidas" : "categorias-vehiculo";
}

function ContenidoModulo({
  modulo,
  sesion,
  alModificar,
  alRenovarSesion,
  alProcesar,
}) {
  if (modulo.id === "perfil")
    return (
      <Perfil
        sesion={sesion}
        alRenovarSesion={alRenovarSesion}
        alProcesar={alProcesar}
      />
    );
  return (
    <EspacioModulo
      key={modulo.id}
      modulo={modulo}
      sesion={sesion}
      alModificar={alModificar}
      alProcesar={alProcesar}
    />
  );
}

export default function App() {
  const [sesion, setSesion] = useState(null);
  const [actual, setActual] = useState(null);
  const [revision, setRevision] = useState(0);
  const [ocupado, setOcupado] = useState(false);
  if (!sesion)
    return (
      <Ingreso
        alIngresar={(datos) => {
          setSesion(datos);
          setActual(moduloInicial(datos.roles));
        }}
      />
    );
  const rol = nombreRol(sesion.roles);
  const opciones = [
    ...modulos,
    {
      id: "perfil",
      titulo: "Mi perfil",
      grupo: "Mi cuenta",
      roles: sesion.roles,
    },
  ].filter((modulo) => autorizado(sesion, modulo.roles));
  const seleccionado =
    opciones.find((modulo) => modulo.id === actual) || opciones[0];
  return (
    <ProveedorReferencias sesion={sesion} revision={revision}>
      <div className="aplicacion">
        <a className="saltar-contenido" href="#contenido">
          Ir al contenido
        </a>
        <aside className="barra-lateral">
          <nav className="menu-principal" aria-label="Navegación principal">
            {[...new Set(opciones.map((modulo) => modulo.grupo))].map(
              (grupo) => (
                <div className="grupo-menu" key={grupo}>
                  <p className="etiqueta-menu">{grupo}</p>
                  {opciones
                    .filter((modulo) => modulo.grupo === grupo)
                    .map((modulo) => (
                      <button
                        key={modulo.id}
                        disabled={ocupado}
                        className={`opcion-menu ${seleccionado.id === modulo.id ? "opcion-activa" : ""}`}
                        aria-current={
                          seleccionado.id === modulo.id ? "page" : undefined
                        }
                        onClick={() => setActual(modulo.id)}
                      >
                        {modulo.titulo}
                      </button>
                    ))}
                </div>
              ),
            )}
          </nav>
        </aside>
        <div className="zona-trabajo">
          <header className="barra-superior">
            <span className="usuario">
              {sesion.usuario} · {rol}
            </span>
            <button
              className="boton secundario"
              disabled={ocupado}
              onClick={() => {
                setSesion(null);
                setActual(null);
              }}
            >
              Cerrar sesión
            </button>
          </header>
          <main id="contenido" className="contenido">
            <ContenidoModulo
              modulo={seleccionado}
              sesion={sesion}
              alModificar={() => setRevision((valor) => valor + 1)}
              alRenovarSesion={setSesion}
              alProcesar={setOcupado}
            />
          </main>
        </div>
      </div>
    </ProveedorReferencias>
  );
}
