# Compila React con Vite y lo sirve con `vite preview`, que redirige /api al backend (API_TARGET).
FROM node:24-slim
WORKDIR /frontend
RUN npm install -g pnpm@11.25.0
COPY package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile
COPY . .
RUN pnpm build
EXPOSE 5173
CMD ["pnpm", "exec", "vite", "preview", "--host", "0.0.0.0", "--port", "5173", "--strictPort"]
