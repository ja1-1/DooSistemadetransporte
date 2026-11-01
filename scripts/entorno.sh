#!/usr/bin/env bash
set -euo pipefail

PROYECTO_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROYECTO_DIR"
command -v node >/dev/null || { echo 'Se necesita Node.js 24.15 o posterior compatible.' >&2; exit 1; }
node -e 'const [major,minor]=process.versions.node.split(".").map(Number); if(!((major===24 && minor>=15) || major>=26)) { console.error("Usa Node.js 24.15+ o 26+."); process.exit(1); }'
command -v pnpm >/dev/null || { echo 'Se necesita pnpm 11.25.0: npm install -g pnpm@11.25.0' >&2; exit 1; }
PNPM_BIN="$(command -v pnpm)"
if [[ -x /usr/libexec/java_home ]]; then
  export JAVA_HOME="$(/usr/libexec/java_home -v 25)"
fi
java -version 2>&1 | head -n 1 | grep -q '"25' || { echo 'Se necesita JDK 25.' >&2; exit 1; }
command -v docker >/dev/null || { echo 'Se necesita Docker Desktop con Docker Compose.' >&2; exit 1; }
docker info >/dev/null 2>&1 || { echo 'Abre Docker Desktop antes de continuar.' >&2; exit 1; }
mkdir -p .local
if [[ ! -f .env.local ]]; then
  (umask 077; node --input-type=module <<'JS' > .env.local
import { randomBytes } from 'node:crypto';
const clave=()=>randomBytes(20).toString('base64url');
console.log(`DATABASE_PASSWORD=${clave()}\nDATABASE_PORT=55432\nSERVER_PORT=8083\nFRONTEND_PORT=5176\nTRANSPORTE_ADMIN_USUARIO=admin\nTRANSPORTE_ADMIN_CLAVE=${clave()}\nTRANSPORTE_OPERACION_USUARIO=operacion\nTRANSPORTE_OPERACION_CLAVE=${clave()}`);
JS
  )
fi
set -a
source .env.local
set +a
export MAVEN_USER_HOME="$PROYECTO_DIR/.local/maven"
export DATABASE_URL="${DATABASE_URL:-jdbc:postgresql://127.0.0.1:${DATABASE_PORT:-55432}/transporte}"
export DATABASE_USER=transporte
export SERVER_PORT="${SERVER_PORT:-8083}"
export FRONTEND_PORT="${FRONTEND_PORT:-5176}"
export API_TARGET="http://127.0.0.1:$SERVER_PORT"

maven() { (cd backend && ./mvnw "-Dmaven.repo.local=$PROYECTO_DIR/.local/m2" "$@"); }
compose() { docker compose --env-file .env.local "$@"; }
preparar_frontend() { "$PNPM_BIN" --dir frontend install --frozen-lockfile --store-dir "$PROYECTO_DIR/.local/pnpm"; }

esperar_api() {
  local puerto="$1" proceso="$2" log="$3"
  for intento in {1..60}; do
    kill -0 "$proceso" 2>/dev/null || { echo 'El backend no inició. Revisa el log en .local/.' >&2; return 1; }
    if grep -q 'Started TransporteApplication' "$log" && [[ "$(curl -s -o /dev/null -w '%{http_code}' "http://127.0.0.1:$puerto/api/v1/sesion" || true)" == 401 ]]; then return 0; fi
    sleep 1
  done
  echo 'El backend no estuvo disponible en el tiempo esperado.' >&2
  return 1
}
