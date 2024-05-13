package it.polimi.sw.gianpaolocugola47.exceptions;

public class GameNotStartedException extends Throwable {
    public GameNotStartedException(String message) {
        super(message);
    }

    public GameNotStartedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameNotStartedException(Throwable cause) {
        super(cause);
    }

    public GameNotStartedException(){
        super();
    }
}
