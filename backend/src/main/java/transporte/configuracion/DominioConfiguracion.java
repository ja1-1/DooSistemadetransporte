package transporte.configuracion;

import transporte.dominio.puerto.NotificadorSalidas;
import transporte.dominio.repositorio.AsignacionRepositorio;
import transporte.dominio.repositorio.CategoriaVehiculoRepositorio;
import transporte.dominio.repositorio.ConductorRepositorio;
import transporte.dominio.repositorio.ProgramacionRutaRepositorio;
import transporte.dominio.repositorio.RutaRepositorio;
import transporte.dominio.repositorio.SalidaRepositorio;
import transporte.dominio.repositorio.VehiculoRepositorio;
import transporte.dominio.servicio.ServicioAsignacion;
import transporte.dominio.servicio.ServicioFlota;
import transporte.dominio.servicio.ServicioProgramacion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration(proxyBeanMethods = false)
public class DominioConfiguracion {
    @Bean
    Clock reloj() {
        return Clock.system(ZoneId.of("America/Bogota"));
    }

    @Bean
    ServicioFlota servicioFlota(
            AsignacionRepositorio repositorioAsignacion,
            SalidaRepositorio repositorioSalida,
            VehiculoRepositorio repositorioVehiculo,
            ConductorRepositorio repositorioConductor,
            CategoriaVehiculoRepositorio cat) {
        return new ServicioFlota(
                repositorioAsignacion,
                repositorioSalida,
                repositorioVehiculo,
                repositorioConductor,
                cat);
    }

    @Bean
    ServicioAsignacion servicioAsignacion(
            AsignacionRepositorio repositorioAsignacion,
            CategoriaVehiculoRepositorio cat,
            SalidaRepositorio repositorioSalida,
            ProgramacionRutaRepositorio repositorioProgramacionRuta,
            ConductorRepositorio repositorioConductor,
            VehiculoRepositorio repositorioVehiculo,
            Clock reloj) {
        return new ServicioAsignacion(
                repositorioAsignacion,
                cat,
                repositorioSalida,
                repositorioProgramacionRuta,
                repositorioConductor,
                repositorioVehiculo,
                reloj);
    }

    @Bean
    ServicioProgramacion servicioProgramacion(
            ProgramacionRutaRepositorio repositorioProgramacionRuta,
            SalidaRepositorio repositorioSalida,
            RutaRepositorio repositorioRuta,
            AsignacionRepositorio repositorioAsignacion,
            ConductorRepositorio repositorioConductor,
            VehiculoRepositorio repositorioVehiculo,
            CategoriaVehiculoRepositorio cat,
            NotificadorSalidas notificadorSalidas,
            Clock reloj) {
        return new ServicioProgramacion(
                repositorioProgramacionRuta,
                repositorioSalida,
                repositorioRuta,
                repositorioAsignacion,
                repositorioConductor,
                repositorioVehiculo,
                cat,
                notificadorSalidas,
                reloj);
    }
}
