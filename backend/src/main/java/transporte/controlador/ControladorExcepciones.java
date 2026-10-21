package transporte.controlador;

import static transporte.transversal.mensajes.CatalogoMensajes.CONFLICTO;
import static transporte.transversal.mensajes.CatalogoMensajes.DATOS_INVALIDOS;
import static transporte.transversal.mensajes.CatalogoMensajes.DUPLICADO;
import static transporte.transversal.mensajes.CatalogoMensajes.FORMATO_INVALIDO;
import static transporte.transversal.mensajes.CatalogoMensajes.INTEGRIDAD;
import static transporte.transversal.mensajes.CatalogoMensajes.PERSISTENCIA;
import static transporte.transversal.mensajes.CatalogoMensajes.PERSISTENCIA_NO_DISPONIBLE;
import static transporte.transversal.mensajes.CatalogoMensajes.PETICION_INVALIDA;

import transporte.transversal.excepciones.NegocioExcepcion;
import transporte.transversal.mensajes.CatalogoMensajes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.DateTimeException;

@RestControllerAdvice
public class ControladorExcepciones {
    private static final Logger LOG = LoggerFactory.getLogger(ControladorExcepciones.class);

    public record ErrorRespuesta(String codigo, String mensaje) {}

    private static String mensaje(Exception error, String defecto) {
        String mensaje = error.getMessage();
        return mensaje == null || mensaje.isBlank() ? defecto : mensaje;
    }

    @ExceptionHandler({IllegalArgumentException.class, DateTimeException.class})
    ResponseEntity<ErrorRespuesta> datosInvalidos(RuntimeException error) {
        return ResponseEntity.badRequest()
                .body(new ErrorRespuesta(DATOS_INVALIDOS, mensaje(error, FORMATO_INVALIDO)));
    }

    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class
    })
    ResponseEntity<ErrorRespuesta> peticionInvalida(Exception error) {
        return ResponseEntity.badRequest()
                .body(new ErrorRespuesta(PETICION_INVALIDA, FORMATO_INVALIDO));
    }

    @ExceptionHandler(NegocioExcepcion.class)
    ResponseEntity<ErrorRespuesta> negocio(NegocioExcepcion error) {
        var estado =
                error.getTipo() == NegocioExcepcion.Tipo.NO_ENCONTRADO
                        ? HttpStatus.NOT_FOUND
                        : HttpStatus.CONFLICT;
        return ResponseEntity.status(estado)
                .body(new ErrorRespuesta(error.getTipo().name(), error.getMensajeUsuario()));
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<ErrorRespuesta> politica(IllegalStateException error) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorRespuesta(CONFLICTO, mensaje(error, INTEGRIDAD)));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ErrorRespuesta> integridad(DataIntegrityViolationException error) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorRespuesta(CONFLICTO, INTEGRIDAD));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<ErrorRespuesta> nombreDuplicado(DuplicateKeyException error) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorRespuesta(CONFLICTO, DUPLICADO));
    }

    @ExceptionHandler(DataAccessException.class)
    ResponseEntity<ErrorRespuesta> persistencia(DataAccessException error) {
        LOG.error(CatalogoMensajes.FALLO_UNA_OPERACION_DE_PERSISTENCIA_DE_TRANSPORTE, error);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorRespuesta(PERSISTENCIA_NO_DISPONIBLE, PERSISTENCIA));
    }
}
