CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE FUNCTION normalizar_via(text) RETURNS text LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE AS $$
 SELECT translate(lower(regexp_replace(btrim($1), '\s+', ' ', 'g')), 'áéíóúüñ', 'aeiouun')
$$;

CREATE TABLE conductor (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    cedula VARCHAR(20) NOT NULL,
    primer_nombre VARCHAR(40) NOT NULL,
    segundo_nombre VARCHAR(40),
    primer_apellido VARCHAR(40) NOT NULL,
    segundo_apellido VARCHAR(40),
    fecha_nacimiento DATE NOT NULL,
    numero_telefono VARCHAR(10) NOT NULL,
    tipo_sangre VARCHAR(3) NOT NULL,
    numero_licencia VARCHAR(20) NOT NULL,
    categoria_licencia VARCHAR(2) NOT NULL,
    fecha_vencimiento_licencia DATE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id),
    CONSTRAINT uq_conductor_id_cedula UNIQUE (id, cedula),
    CONSTRAINT uq_conductor_cedula UNIQUE (cedula),
    CONSTRAINT uq_conductor_numero_licencia UNIQUE (numero_licencia),
    CONSTRAINT ck_conductor_cedula_longitud CHECK (char_length(cedula) BETWEEN 5 AND 20 AND cedula=btrim(cedula)),
    CONSTRAINT ck_conductor_primer_nombre_longitud CHECK (char_length(primer_nombre) BETWEEN 1 AND 40 AND primer_nombre=btrim(primer_nombre)),
    CONSTRAINT ck_conductor_segundo_nombre_longitud CHECK (char_length(segundo_nombre) BETWEEN 1 AND 40 AND segundo_nombre=btrim(segundo_nombre)),
    CONSTRAINT ck_conductor_primer_apellido_longitud CHECK (char_length(primer_apellido) BETWEEN 1 AND 40 AND primer_apellido=btrim(primer_apellido)),
    CONSTRAINT ck_conductor_segundo_apellido_longitud CHECK (char_length(segundo_apellido) BETWEEN 1 AND 40 AND segundo_apellido=btrim(segundo_apellido)),
    CONSTRAINT ck_conductor_numero_telefono_longitud CHECK (char_length(numero_telefono) BETWEEN 1 AND 10 AND numero_telefono=btrim(numero_telefono)),
    CONSTRAINT ck_conductor_numero_telefono_formato CHECK (numero_telefono ~ '^3[0-9]{9}$'),
    CONSTRAINT ck_conductor_tipo_sangre CHECK (tipo_sangre IN ('O+','O-','A+','A-','B+','B-','AB+','AB-')),
    CONSTRAINT ck_conductor_numero_licencia_longitud CHECK (char_length(numero_licencia) BETWEEN 5 AND 20 AND numero_licencia=btrim(numero_licencia)),
    CONSTRAINT ck_conductor_categoria_licencia CHECK (categoria_licencia IN ('C1','C2','C3'))
);

CREATE TABLE ruta (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    nombre VARCHAR(600) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    activa BOOLEAN NOT NULL DEFAULT true,
    origen_lugar_id UUID NOT NULL,
    destino_lugar_id UUID NOT NULL,
    distancia_total_km NUMERIC(8,2) NOT NULL,
    tipo_ruta_id UUID NOT NULL,
    via_id UUID NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_ruta_recorrido UNIQUE (origen_lugar_id, destino_lugar_id, via_id),
    CONSTRAINT ck_ruta_nombre_longitud CHECK (char_length(nombre) BETWEEN 1 AND 600 AND nombre=btrim(nombre)),
    CONSTRAINT ck_ruta_descripcion_longitud CHECK (char_length(descripcion) BETWEEN 1 AND 500 AND descripcion=btrim(descripcion)),
    CONSTRAINT ck_ruta_distancia_total_km_positiva CHECK (distancia_total_km > 0)
);

CREATE TABLE usuario (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    primer_nombre VARCHAR(40) NOT NULL,
    segundo_nombre VARCHAR(40),
    primer_apellido VARCHAR(40) NOT NULL,
    segundo_apellido VARCHAR(40),
    cedula VARCHAR(20) NOT NULL,
    correo VARCHAR(120) NOT NULL,
    numero_telefono VARCHAR(10),
    hash_contrasena VARCHAR(120) NOT NULL,
    rol_id UUID NOT NULL,
    conductor_id UUID,
    activo BOOLEAN NOT NULL DEFAULT true,
    debe_cambiar_contrasena BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_usuario_cedula UNIQUE (cedula),
    CONSTRAINT uq_usuario_correo UNIQUE (correo),
    CONSTRAINT uq_usuario_conductor_id UNIQUE (conductor_id),
    CONSTRAINT ck_usuario_primer_nombre_longitud CHECK (char_length(primer_nombre) BETWEEN 1 AND 40 AND primer_nombre=btrim(primer_nombre)),
    CONSTRAINT ck_usuario_segundo_nombre_longitud CHECK (char_length(segundo_nombre) BETWEEN 1 AND 40 AND segundo_nombre=btrim(segundo_nombre)),
    CONSTRAINT ck_usuario_primer_apellido_longitud CHECK (char_length(primer_apellido) BETWEEN 1 AND 40 AND primer_apellido=btrim(primer_apellido)),
    CONSTRAINT ck_usuario_segundo_apellido_longitud CHECK (char_length(segundo_apellido) BETWEEN 1 AND 40 AND segundo_apellido=btrim(segundo_apellido)),
    CONSTRAINT ck_usuario_cedula_longitud CHECK (char_length(cedula) BETWEEN 5 AND 20 AND cedula=btrim(cedula)),
    CONSTRAINT ck_usuario_correo_longitud CHECK (char_length(correo) BETWEEN 1 AND 120 AND correo=btrim(correo)),
    CONSTRAINT ck_usuario_correo_formato CHECK (correo ~ '^[^[:space:]@]+@[^[:space:]@]+[.][^[:space:]@]+$'),
    CONSTRAINT ck_usuario_numero_telefono_longitud CHECK (char_length(numero_telefono) BETWEEN 1 AND 10 AND numero_telefono=btrim(numero_telefono)),
    CONSTRAINT ck_usuario_numero_telefono_formato CHECK (numero_telefono ~ '^3[0-9]{9}$'),
    CONSTRAINT ck_usuario_hash_contrasena_longitud CHECK (char_length(hash_contrasena) BETWEEN 60 AND 120 AND hash_contrasena=btrim(hash_contrasena)),
    CONSTRAINT ck_usuario_rol_conductor CHECK ((rol_id='4e4fe28b-c84c-5c85-b935-2c0a99fec851') = (conductor_id IS NOT NULL))
);

CREATE TABLE vehiculo (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    placa VARCHAR(6) NOT NULL,
    marca VARCHAR(60) NOT NULL,
    modelo VARCHAR(60) NOT NULL,
    anio_fabricacion INTEGER NOT NULL,
    capacidad_pasajeros INTEGER NOT NULL,
    categoria_vehiculo_id UUID NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id),
    CONSTRAINT uq_vehiculo_placa UNIQUE (placa),
    CONSTRAINT ck_vehiculo_placa_longitud CHECK (char_length(placa) BETWEEN 1 AND 6 AND placa=btrim(placa)),
    CONSTRAINT ck_vehiculo_placa_formato CHECK (placa ~ '^[A-Z]{3}[0-9]{3}$'),
    CONSTRAINT ck_vehiculo_marca_longitud CHECK (char_length(marca) BETWEEN 1 AND 60 AND marca=btrim(marca)),
    CONSTRAINT ck_vehiculo_modelo_longitud CHECK (char_length(modelo) BETWEEN 1 AND 60 AND modelo=btrim(modelo)),
    CONSTRAINT ck_vehiculo_anio_fabricacion_rango CHECK (anio_fabricacion <= EXTRACT(YEAR FROM CURRENT_DATE)),
    CONSTRAINT ck_vehiculo_capacidad_pasajeros_rango CHECK (capacidad_pasajeros > 0)
);

CREATE TABLE asignacion (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    conductor_id UUID NOT NULL,
    vehiculo_id UUID NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    PRIMARY KEY (id),
    CONSTRAINT uq_asignacion_conductor_id_vehiculo_id_fecha_inicio UNIQUE (conductor_id, vehiculo_id, fecha_inicio),
    CONSTRAINT ck_asignacion_periodo CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio)
);

CREATE TABLE programacion_ruta (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    ruta_id UUID NOT NULL,
    asignacion_id UUID NOT NULL,
    hora_salida TIME NOT NULL,
    hora_ultima_salida TIME NOT NULL,
    intervalo_minutos INTEGER NOT NULL,
    duracion_estimada INTEGER NOT NULL,
    vigencia_desde DATE NOT NULL,
    vigencia_hasta DATE,
    activa BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id),
    CONSTRAINT ck_programacion_ruta_franja CHECK (hora_ultima_salida >= hora_salida AND mod(EXTRACT(EPOCH FROM (hora_ultima_salida-hora_salida)), intervalo_minutos*60)=0 AND (hora_ultima_salida=hora_salida OR intervalo_minutos>=duracion_estimada)),
    CONSTRAINT ck_programacion_ruta_intervalo CHECK (intervalo_minutos BETWEEN 1 AND 720),
    CONSTRAINT ck_programacion_ruta_duracion_estimada_rango CHECK (duracion_estimada > 0),
    CONSTRAINT ck_programacion_ruta_vigencia CHECK (vigencia_hasta IS NULL OR vigencia_hasta >= vigencia_desde)
);

CREATE TABLE programacion_ruta_dia (
    programacion_ruta_id UUID NOT NULL,
    dia_semana VARCHAR(9) NOT NULL,
    PRIMARY KEY (programacion_ruta_id, dia_semana),
    CONSTRAINT ck_programacion_ruta_dia_dia CHECK (dia_semana IN ('Lunes','Martes','Miércoles','Jueves','Viernes','Sábado','Domingo'))
);

CREATE TABLE salida (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    ruta_id UUID NOT NULL,
    nombre_ruta VARCHAR(600) NOT NULL,
    asignacion_id UUID NOT NULL,
    instante_salida_programado TIMESTAMP NOT NULL,
    instante_llegada_estimado TIMESTAMP NOT NULL,
    instante_salida_real TIMESTAMP,
    instante_llegada_real TIMESTAMP,
    estado_salida_id UUID NOT NULL,
    programacion_ruta_id UUID,
    PRIMARY KEY (id),
    CONSTRAINT uq_salida_asignacion_id_instante_salida_programado UNIQUE (asignacion_id, instante_salida_programado),
    CONSTRAINT uq_salida_programacion_instante UNIQUE (instante_salida_programado, programacion_ruta_id),
    CONSTRAINT ck_salida_nombre_ruta_longitud CHECK (char_length(nombre_ruta) BETWEEN 1 AND 600 AND nombre_ruta=btrim(nombre_ruta)),
    CONSTRAINT ck_salida_ventana CHECK (instante_llegada_estimado > instante_salida_programado),
    CONSTRAINT ck_salida_estado_instantes CHECK ((estado_salida_id='581accea-3852-54fd-b783-ce22cb23887c' AND instante_salida_real IS NULL AND instante_llegada_real IS NULL) OR (estado_salida_id='e8b053b0-0044-545c-afee-9653c6d3fab3' AND instante_salida_real IS NOT NULL AND instante_llegada_real IS NULL) OR (estado_salida_id='fdac84c6-19b0-5bc5-92d9-d4823edec4f5' AND instante_salida_real IS NOT NULL AND instante_llegada_real > instante_salida_real) OR (estado_salida_id='c8c28d15-f60c-550f-86d9-f079e11061d6' AND instante_llegada_real IS NULL))
);

CREATE TABLE incidencia (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    salida_id UUID NOT NULL,
    tipo_incidencia_id UUID NOT NULL,
    instante_ocurrencia TIMESTAMP NOT NULL,
    instante_reporte TIMESTAMP NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    responsable_id UUID NOT NULL,
    responsable_nombre VARCHAR(200) NOT NULL,
    responsable_rol VARCHAR(40) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT ck_incidencia_instantes CHECK (instante_ocurrencia <= instante_reporte),
    CONSTRAINT ck_incidencia_descripcion_longitud CHECK (char_length(descripcion) BETWEEN 1 AND 1000 AND descripcion=btrim(descripcion)),
    CONSTRAINT ck_incidencia_responsable_nombre_longitud CHECK (char_length(responsable_nombre) BETWEEN 3 AND 200 AND responsable_nombre=btrim(responsable_nombre)),
    CONSTRAINT ck_incidencia_responsable_rol_longitud CHECK (char_length(responsable_rol) BETWEEN 1 AND 40 AND responsable_rol=btrim(responsable_rol))
);

CREATE TABLE municipio (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    nombre VARCHAR(120) NOT NULL,
    departamento VARCHAR(80) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id),
    CONSTRAINT uq_municipio_nombre_departamento UNIQUE (nombre, departamento),
    CONSTRAINT ck_municipio_nombre_longitud CHECK (char_length(nombre) BETWEEN 1 AND 120 AND nombre=btrim(nombre)),
    CONSTRAINT ck_municipio_departamento_longitud CHECK (char_length(departamento) BETWEEN 1 AND 80 AND departamento=btrim(departamento))
);

CREATE TABLE sector (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    municipio_id UUID NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id),
    CONSTRAINT uq_sector_municipio_nombre UNIQUE (municipio_id, nombre),
    CONSTRAINT ck_sector_nombre_longitud CHECK (char_length(nombre) BETWEEN 1 AND 120 AND nombre=btrim(nombre))
);

CREATE TABLE lugar (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    sector_id UUID NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id),
    CONSTRAINT uq_lugar_sector_nombre UNIQUE (sector_id, nombre),
    CONSTRAINT ck_lugar_nombre_longitud CHECK (char_length(nombre) BETWEEN 1 AND 120 AND nombre=btrim(nombre))
);

CREATE TABLE via (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    nombre VARCHAR(80) NOT NULL,
    descripcion VARCHAR(250) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id),
    CONSTRAINT ck_via_nombre_longitud CHECK (char_length(nombre) BETWEEN 3 AND 80 AND nombre=btrim(nombre)),
    CONSTRAINT ck_via_descripcion_longitud CHECK (char_length(descripcion) BETWEEN 10 AND 250 AND descripcion=btrim(descripcion))
);

ALTER TABLE ruta ADD CONSTRAINT fk_ruta_origen_lugar_id FOREIGN KEY (origen_lugar_id) REFERENCES lugar(id) ON DELETE RESTRICT;

ALTER TABLE ruta ADD CONSTRAINT fk_ruta_destino_lugar_id FOREIGN KEY (destino_lugar_id) REFERENCES lugar(id) ON DELETE RESTRICT;

ALTER TABLE ruta ADD CONSTRAINT fk_ruta_tipo_ruta_id FOREIGN KEY (tipo_ruta_id) REFERENCES tipo_ruta(id) ON DELETE RESTRICT;

ALTER TABLE ruta ADD CONSTRAINT fk_ruta_via_id FOREIGN KEY (via_id) REFERENCES via(id) ON DELETE RESTRICT;

ALTER TABLE vehiculo ADD CONSTRAINT fk_vehiculo_categoria_vehiculo_id FOREIGN KEY (categoria_vehiculo_id) REFERENCES categoria_vehiculo(id) ON DELETE RESTRICT;

ALTER TABLE asignacion ADD CONSTRAINT fk_asignacion_conductor_id FOREIGN KEY (conductor_id) REFERENCES conductor(id) ON DELETE RESTRICT;

ALTER TABLE asignacion ADD CONSTRAINT fk_asignacion_vehiculo_id FOREIGN KEY (vehiculo_id) REFERENCES vehiculo(id) ON DELETE RESTRICT;

ALTER TABLE programacion_ruta ADD CONSTRAINT fk_programacion_ruta_ruta_id FOREIGN KEY (ruta_id) REFERENCES ruta(id) ON DELETE RESTRICT;

ALTER TABLE programacion_ruta ADD CONSTRAINT fk_programacion_ruta_asignacion_id FOREIGN KEY (asignacion_id) REFERENCES asignacion(id) ON DELETE RESTRICT;

ALTER TABLE programacion_ruta_dia ADD CONSTRAINT fk_programacion_ruta_dia_programacion_ruta_id FOREIGN KEY (programacion_ruta_id) REFERENCES programacion_ruta(id) ON DELETE CASCADE;

ALTER TABLE salida ADD CONSTRAINT fk_salida_ruta_id FOREIGN KEY (ruta_id) REFERENCES ruta(id) ON DELETE RESTRICT;

ALTER TABLE salida ADD CONSTRAINT fk_salida_asignacion_id FOREIGN KEY (asignacion_id) REFERENCES asignacion(id) ON DELETE RESTRICT;

ALTER TABLE salida ADD CONSTRAINT fk_salida_estado_salida_id FOREIGN KEY (estado_salida_id) REFERENCES estado_salida(id) ON DELETE RESTRICT;

ALTER TABLE salida ADD CONSTRAINT fk_salida_programacion_ruta_id FOREIGN KEY (programacion_ruta_id) REFERENCES programacion_ruta(id) ON DELETE RESTRICT;

ALTER TABLE incidencia ADD CONSTRAINT fk_incidencia_salida_id FOREIGN KEY (salida_id) REFERENCES salida(id) ON DELETE CASCADE;

ALTER TABLE incidencia ADD CONSTRAINT fk_incidencia_tipo_incidencia_id FOREIGN KEY (tipo_incidencia_id) REFERENCES tipo_incidencia(id) ON DELETE RESTRICT;

ALTER TABLE incidencia ADD CONSTRAINT fk_incidencia_responsable_id FOREIGN KEY (responsable_id) REFERENCES usuario(id) ON DELETE RESTRICT;

ALTER TABLE sector ADD CONSTRAINT fk_sector_municipio_id FOREIGN KEY (municipio_id) REFERENCES municipio(id) ON DELETE RESTRICT;

ALTER TABLE lugar ADD CONSTRAINT fk_lugar_sector_id FOREIGN KEY (sector_id) REFERENCES sector(id) ON DELETE RESTRICT;

ALTER TABLE usuario ADD CONSTRAINT fk_usuario_rol FOREIGN KEY(rol_id) REFERENCES rol(id) ON DELETE RESTRICT;

ALTER TABLE usuario ADD CONSTRAINT fk_usuario_conductor_cedula FOREIGN KEY(conductor_id,cedula) REFERENCES conductor(id,cedula) ON DELETE RESTRICT;

CREATE INDEX ix_ruta_origen_lugar_id ON ruta(origen_lugar_id);

CREATE INDEX ix_ruta_destino_lugar_id ON ruta(destino_lugar_id);

CREATE INDEX ix_ruta_via_id ON ruta(via_id);

CREATE INDEX ix_usuario_conductor_id_cedula ON usuario(cedula, conductor_id);

CREATE INDEX ix_usuario_rol_id ON usuario(rol_id);

CREATE INDEX ix_vehiculo_categoria_vehiculo_id ON vehiculo(categoria_vehiculo_id);

CREATE INDEX ix_asignacion_vehiculo_id ON asignacion(vehiculo_id);

CREATE INDEX ix_programacion_ruta_ruta_id ON programacion_ruta(ruta_id);

CREATE INDEX ix_programacion_ruta_asignacion_id ON programacion_ruta(asignacion_id);

CREATE INDEX ix_salida_ruta_id ON salida(ruta_id);

CREATE INDEX ix_salida_estado_salida_id ON salida(estado_salida_id);

CREATE INDEX ix_salida_programacion_ruta_id ON salida(programacion_ruta_id);

CREATE INDEX ix_incidencia_salida_id ON incidencia(salida_id);

CREATE INDEX ix_incidencia_tipo_incidencia_id ON incidencia(tipo_incidencia_id);

CREATE INDEX ix_incidencia_responsable_id ON incidencia(responsable_id);

CREATE INDEX ix_incidencia_responsable_rol ON incidencia(responsable_rol);

CREATE INDEX ix_sector_municipio_id ON sector(municipio_id);

CREATE INDEX ix_lugar_sector_id ON lugar(sector_id);

CREATE UNIQUE INDEX uq_via_nombre ON via (normalizar_via(nombre));

ALTER TABLE asignacion ADD CONSTRAINT ex_asignacion_conductor_solapado EXCLUDE USING gist(conductor_id WITH =, daterange(fecha_inicio,fecha_fin,'[]') WITH &&);
ALTER TABLE asignacion ADD CONSTRAINT ex_asignacion_vehiculo_solapado EXCLUDE USING gist(vehiculo_id WITH =, daterange(fecha_inicio,fecha_fin,'[]') WITH &&);
CREATE VIEW asignacion_estado AS SELECT id,conductor_id,vehiculo_id,fecha_inicio,fecha_fin,
 CASE WHEN fecha_fin IS NULL OR CURRENT_DATE <= fecha_fin THEN 'Vigente' ELSE 'Finalizada' END AS estado FROM asignacion;
