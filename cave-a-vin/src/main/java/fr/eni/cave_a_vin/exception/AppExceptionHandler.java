package fr.eni.cave_a_vin.exception;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
@AllArgsConstructor
public class AppExceptionHandler {
    private MessageSource messageSource;

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<String> capturerExceptions(Exception exception){
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<String> capturerMethodArgumentNotValidException(MethodArgumentNotValidException exception, Locale locale){
        String titreMsg = messageSource.getMessage("notvalidexception", null, locale);

        String message = exception.getBindingResult()
                .getFieldErrors() // Récupère toutes les erreurs de champ
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage()) // Récupère les messages d'erreur
                .reduce(titreMsg, (msg1, msg2) -> msg1 + "; " + msg2); // Concatène les erreurs

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message); // Retourne une réponse HTTP 400 avec les erreurs
    }
}
