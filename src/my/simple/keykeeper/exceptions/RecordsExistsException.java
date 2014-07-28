package my.simple.keykeeper.exceptions;

/**
 * Created by Alex on 26.05.2014.
 */
public class RecordsExistsException extends Exception {

    private final String errorDescription;

    public RecordsExistsException(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
