package transporte.configuracion;

import transporte.infraestructura.persistencia.BloqueoEscritura;
import transporte.infraestructura.seguridad.VerificadorActorActual;
import transporte.negocio.fachada.Escritura;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(order = 0)
public class CoordinacionEscrituras {
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor protegerEscrituras(BloqueoEscritura bloqueo, VerificadorActorActual actor) {
        MethodInterceptor interceptor =
                invocacion -> {
                    bloqueo.adquirir();
                    actor.verificar();
                    return invocacion.proceed();
                };
        var asesor =
                new DefaultPointcutAdvisor(
                        new AnnotationMatchingPointcut(null, Escritura.class), interceptor);
        asesor.setOrder(1);
        return asesor;
    }
}
