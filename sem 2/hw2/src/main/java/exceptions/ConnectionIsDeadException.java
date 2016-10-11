package exceptions;

import java.io.IOException;

/**
 * Created by Mark on 11.10.2016.
 */
public class ConnectionIsDeadException extends IOException {
    public ConnectionIsDeadException() {
        super("Socket connection already died");
    }
}
