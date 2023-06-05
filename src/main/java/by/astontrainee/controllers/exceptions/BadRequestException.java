package by.astontrainee.controllers.exceptions;

/**
 * @author Alex Mikhalevich
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
