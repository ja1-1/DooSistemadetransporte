package transporte.configuracion;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.core.StreamReadFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.cfg.CoercionAction;
import tools.jackson.databind.cfg.CoercionInputShape;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.type.LogicalType;

@Configuration(proxyBeanMethods = false)
public class JsonConfiguracion {
    @Bean
    JsonMapperBuilderCustomizer lecturaEstricta() {
        return constructor ->
                constructor
                        .disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
                        .disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT)
                        .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                        .enable(EnumFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
                        .enable(StreamReadFeature.STRICT_DUPLICATE_DETECTION)
                        .withCoercionConfig(
                                LogicalType.Textual,
                                configuracion ->
                                        configuracion
                                                .setCoercion(
                                                        CoercionInputShape.Integer,
                                                        CoercionAction.Fail)
                                                .setCoercion(
                                                        CoercionInputShape.Float,
                                                        CoercionAction.Fail)
                                                .setCoercion(
                                                        CoercionInputShape.Boolean,
                                                        CoercionAction.Fail));
    }
}
