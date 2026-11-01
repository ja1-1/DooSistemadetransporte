#!/usr/bin/env bash
set -euo pipefail
source "$(dirname "$0")/entorno.sh"

node --input-type=module - "$SERVER_PORT" "$FRONTEND_PORT" <<'JS'
import net from 'node:net';
for (const puerto of process.argv.slice(2)) {
  const servidor = net.createServer();
  try {
    await new Promise((resolve, reject) => servidor.once('error', reject).listen(Number(puerto), '127.0.0.1', resolve));
  } catch (error) {
    console.error(error.code === 'EADDRINUSE' ? `El puerto ${puerto} ya está en uso. Cierra el arranque anterior o utiliza la aplicación abierta.` : error.message);
    process.exitCode = 1;
    break;
  } finally {
    if (servidor.listening) await new Promise(resolve => servidor.close(resolve));
  }
}
JS

BACKEND_PID=''
limpiar() {
  if [[ -n "$BACKEND_PID" ]]; then kill "$BACKEND_PID" 2>/dev/null || true; wait "$BACKEND_PID" 2>/dev/null || true; fi
}
trap limpiar EXIT
trap 'exit 130' INT TERM

compose up -d --wait db
maven package
preparar_frontend
java -jar backend/target/transporte-0.1.0.jar > .local/backend.log 2>&1 &
BACKEND_PID=$!
esperar_api "$SERVER_PORT" "$BACKEND_PID" .local/backend.log
printf '\nTransporte: http://localhost:%s\nAPI: http://localhost:%s/api/v1\nUsuarios y claves locales: %s/.env.local\nCtrl+C detiene la aplicación; PostgreSQL continúa disponible en Docker.\n\n' "$FRONTEND_PORT" "$SERVER_PORT" "$PROYECTO_DIR"
"$PNPM_BIN" --dir frontend dev
