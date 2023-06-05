package by.astontrainee.services.exceptions;

/**
 * @author Alex Mikhalevich
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }
}
