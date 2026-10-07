CREATE FUNCTION actualizar_nombres_ruta() RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
 UPDATE ruta r SET nombre=CASE WHEN r.tipo_ruta_id='da662198-1e2f-5810-9943-84c7537f4d7c'
 THEN lo.nombre || ' (' || so.nombre || ') > ' || ld.nombre || ' (' || sd.nombre || ') | ' || v.nombre
 ELSE mo.nombre || ' (' || mo.departamento || ') > ' || md.nombre || ' (' || md.departamento || ') | ' || v.nombre END
 FROM lugar lo JOIN sector so ON so.id=lo.sector_id JOIN municipio mo ON mo.id=so.municipio_id,
 lugar ld JOIN sector sd ON sd.id=ld.sector_id JOIN municipio md ON md.id=sd.municipio_id, via v
 WHERE r.origen_lugar_id=lo.id AND r.destino_lugar_id=ld.id AND r.via_id=v.id;
 RETURN NEW;
END $$;
CREATE TRIGGER municipio_nombre AFTER UPDATE OF nombre,departamento ON municipio FOR EACH ROW EXECUTE FUNCTION actualizar_nombres_ruta();
CREATE TRIGGER sector_nombre AFTER UPDATE OF nombre ON sector FOR EACH ROW EXECUTE FUNCTION actualizar_nombres_ruta();
CREATE TRIGGER lugar_nombre AFTER UPDATE OF nombre ON lugar FOR EACH ROW EXECUTE FUNCTION actualizar_nombres_ruta();
CREATE TRIGGER via_nombre AFTER UPDATE OF nombre ON via FOR EACH ROW EXECUTE FUNCTION actualizar_nombres_ruta();
