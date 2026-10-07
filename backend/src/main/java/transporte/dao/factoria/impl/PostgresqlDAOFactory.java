package transporte.dao.factoria.impl;

import transporte.dao.datos.entidad.AsignacionDAO;
import transporte.dao.datos.entidad.CatalogoDAO;
import transporte.dao.datos.entidad.CategoriaVehiculoDAO;
import transporte.dao.datos.entidad.ConductorDAO;
import transporte.dao.datos.entidad.IncidenciaDAO;
import transporte.dao.datos.entidad.LugarDAO;
import transporte.dao.datos.entidad.MunicipioDAO;
import transporte.dao.datos.entidad.ProgramacionRutaDAO;
import transporte.dao.datos.entidad.RutaDAO;
import transporte.dao.datos.entidad.SalidaDAO;
import transporte.dao.datos.entidad.SectorDAO;
import transporte.dao.datos.entidad.UsuarioDAO;
import transporte.dao.datos.entidad.VehiculoDAO;
import transporte.dao.datos.entidad.ViaDAO;
import transporte.dao.factoria.DAOFactory;

import org.springframework.stereotype.Component;

@Component
public record PostgresqlDAOFactory(
        CategoriaVehiculoDAO categoriaVehiculo,
        CatalogoDAO catalogos,
        ConductorDAO conductor,
        VehiculoDAO vehiculo,
        UsuarioDAO usuario,
        AsignacionDAO asignacion,
        RutaDAO ruta,
        ProgramacionRutaDAO programacionRuta,
        SalidaDAO salida,
        IncidenciaDAO incidencia,
        MunicipioDAO municipio,
        ViaDAO via,
        SectorDAO sector,
        LugarDAO lugar)
        implements DAOFactory {}
