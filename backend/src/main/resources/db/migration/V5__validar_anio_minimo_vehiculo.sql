-- Modelo de dominio enriquecido, C-Vehiculo: el año de fabricación inicia en 1900.
ALTER TABLE vehiculo ADD CONSTRAINT ck_vehiculo_anio_fabricacion_minimo
    CHECK (anio_fabricacion >= 1900);
