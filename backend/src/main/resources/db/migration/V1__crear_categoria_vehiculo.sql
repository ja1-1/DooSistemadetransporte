CREATE TABLE categoria_vehiculo (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(40) NOT NULL,
    categoria_licencia_minima VARCHAR(2) NOT NULL,
    CONSTRAINT uq_categoria_vehiculo_nombre UNIQUE (nombre),
    CONSTRAINT ck_categoria_vehiculo_nombre_longitud
        CHECK (char_length(nombre) BETWEEN 1 AND 40 AND nombre = btrim(nombre)),
    CONSTRAINT ck_categoria_vehiculo_categoria_licencia_minima
        CHECK (categoria_licencia_minima IN ('C1', 'C2', 'C3'))
);
