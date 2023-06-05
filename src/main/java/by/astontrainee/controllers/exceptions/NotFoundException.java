package by.astontrainee.controllers.exceptions;

/**
 * @author Alex Mikhalevich
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
