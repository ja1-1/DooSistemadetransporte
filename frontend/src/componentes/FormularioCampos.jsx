import { dias } from "../modulos/definiciones.js";
import { SelectorRelacion } from "./Referencias.jsx";

export default function FormularioCampos({
  campos,
  valores,
  alCambiar,
  prefijo,
  filtro = false,
  disabled = false,
}) {
  return campos.map((campo) => {
    const id = `${prefijo}-${campo.nombre}`;
    const valor = valores[campo.nombre] ?? "";
    const cambiar = (nuevo) => alCambiar({ ...valores, [campo.nombre]: nuevo });
    const atributos = {
      id,
      value: valor,
      disabled,
      required: !filtro && !campo.opcional,
      onChange: (evento) => cambiar(evento.target.value),
    };
    return (
      <div className={`campo campo-${campo.tipo}`} key={campo.nombre}>
        {campo.tipo !== "checkbox" && campo.tipo !== "dias" && (
          <label htmlFor={id}>
            {campo.etiqueta}
            {campo.opcional && !filtro ? " (opcional)" : ""}
          </label>
        )}
        {campo.tipo === "relacion" ? (
          <SelectorRelacion
            campo={campo}
            id={id}
            valor={valor}
            alCambiar={cambiar}
            filtro={filtro}
            disabled={disabled}
          />
        ) : campo.tipo === "dias" ? (
          <fieldset disabled={disabled}>
            <legend>{campo.etiqueta}</legend>
            {dias.map(([codigo, etiqueta]) => (
              <label className="opcion-checkbox" key={codigo}>
                <input
                  type="checkbox"
                  checked={(valor || []).includes(codigo)}
                  onChange={(evento) =>
                    cambiar(
                      evento.target.checked
                        ? [...(valor || []), codigo]
                        : valor.filter((dia) => dia !== codigo),
                    )
                  }
                />
                {etiqueta}
              </label>
            ))}
          </fieldset>
        ) : campo.tipo === "checkbox" ? (
          <label className="opcion-checkbox">
            <input
              id={id}
              type="checkbox"
              checked={Boolean(valor)}
              disabled={disabled}
              onChange={(evento) => cambiar(evento.target.checked)}
            />
            {campo.etiqueta}
          </label>
        ) : campo.tipo === "select" ? (
          <select {...atributos}>
            <option value="">
              {filtro || campo.opcional ? "Todos" : "Selecciona una opción"}
            </option>
            {filtro && valor && !campo.opciones.some(
              (opcion) => (Array.isArray(opcion) ? opcion[0] : opcion) === valor,
            ) && (
              <option value={valor}>Selección actual</option>
            )}
            {campo.opciones.map((opcion) => {
              const [codigo, etiqueta] = Array.isArray(opcion)
                ? opcion
                : [opcion, opcion];
              return (
                <option key={codigo} value={codigo}>
                  {etiqueta}
                </option>
              );
            })}
          </select>
        ) : campo.tipo === "textarea" ? (
          <textarea
            {...atributos}
            rows={3}
            minLength={filtro ? undefined : campo.minLength}
            maxLength={campo.maxLength}
          />
        ) : (
          <input
            {...atributos}
            type={campo.tipo}
            min={filtro ? undefined : campo.min}
            max={filtro ? undefined : campo.max}
            step={campo.step}
            minLength={filtro ? undefined : campo.minLength}
            maxLength={campo.maxLength}
            pattern={filtro ? undefined : campo.pattern}
            inputMode={campo.inputMode}
            autoComplete={
              campo.tipo === "password" ? "new-password" : undefined
            }
          />
        )}
      </div>
    );
  });
}
