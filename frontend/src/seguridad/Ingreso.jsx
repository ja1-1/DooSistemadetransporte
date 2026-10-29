import { useState } from 'react';
import { cambiarContrasena, iniciarSesion } from '../servicios/api/cuenta.js';

const LONGITUD_MINIMA_CONTRASENA = 8;

function CampoContrasena({ id, etiqueta, autoComplete, valor, alCambiar, minLength }) {
  return <>
    <label htmlFor={id}>{etiqueta}</label>
    <input id={id} type="password" autoComplete={autoComplete} minLength={minLength} value={valor} onChange={e => alCambiar(e.target.value)} required />
  </>;
}

function textoBoton(enviando, cambiandoTemporal) {
  if (enviando) return 'Procesando…';
  return cambiandoTemporal ? 'Cambiar contraseña e ingresar' : 'Ingresar';
}

export default function Ingreso({ alIngresar }) {
  const [usuario, setUsuario] = useState('');
  const [clave, setClave] = useState('');
  const [error, setError] = useState('');
  const [enviando, setEnviando] = useState(false);
  const [sesionTemporal, setSesionTemporal] = useState(null);
  const [nueva, setNueva] = useState('');
  const [confirmacion, setConfirmacion] = useState('');
  const cambiandoTemporal = sesionTemporal !== null;

  async function procesar(evento, accion) {
    evento.preventDefault();
    setError('');
    setEnviando(true);
    try {
      await accion();
    } catch (fallo) {
      setError(fallo.message);
    } finally {
      setEnviando(false);
    }
  }

  async function ingresar() {
    const sesion = await iniciarSesion(usuario, clave);
    if (sesion.debeCambiarContrasena) setSesionTemporal(sesion);
    else alIngresar(sesion);
  }

  async function cambiarContrasenaTemporal() {
    if (nueva !== confirmacion) throw new Error('Las contraseñas no coinciden.');
    await cambiarContrasena(sesionTemporal.autorizacion, clave, nueva);
    alIngresar(await iniciarSesion(usuario, nueva));
  }

  const alEnviar = evento => procesar(evento, cambiandoTemporal ? cambiarContrasenaTemporal : ingresar);

  return <main className="ingreso">
    <form onSubmit={alEnviar} className="formulario-ingreso">
      <h1>{cambiandoTemporal ? 'Cambiar contraseña temporal' : 'Iniciar sesión'}</h1>
      {cambiandoTemporal && <p>La nueva contraseña debe tener al menos {LONGITUD_MINIMA_CONTRASENA} caracteres.</p>}
      {cambiandoTemporal ? <>
        <CampoContrasena id="nueva" etiqueta="Nueva contraseña" autoComplete="new-password" minLength={LONGITUD_MINIMA_CONTRASENA} valor={nueva} alCambiar={setNueva} />
        <CampoContrasena id="confirmacion" etiqueta="Confirmar contraseña" autoComplete="new-password" minLength={LONGITUD_MINIMA_CONTRASENA} valor={confirmacion} alCambiar={setConfirmacion} />
      </> : <>
        <label htmlFor="usuario">Usuario</label>
        <input id="usuario" autoComplete="username" value={usuario} onChange={e => setUsuario(e.target.value)} required />
        <CampoContrasena id="clave" etiqueta="Contraseña" autoComplete="current-password" valor={clave} alCambiar={setClave} />
      </>}
      {error && <p className="mensaje error" role="alert">{error}</p>}
      <button className="boton principal" disabled={enviando}>{textoBoton(enviando, cambiandoTemporal)}</button>
    </form>
  </main>;
}
