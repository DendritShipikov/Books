package by.dendritshipikov.books.jsonparser;

public class JsonParserException extends RuntimeException {
    JsonParserException(String message) {
        super(message);
    }

    JsonParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
