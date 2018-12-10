package mx.sharkit.web.view.util;

import java.io.Serializable;
import java.sql.SQLException;

public class CDCException extends Exception implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String frontEndMessage;
    @SuppressWarnings("unused")
    private String backEndMessage;
    private int errorCode;

    /**
     *
     * @param message
     */
    public CDCException(String message) {
        super(message);
        frontEndMessage = message;
        backEndMessage = message;
        errorCode = -1;
    }

    /**
     *
     * @param frontEndMessage
     * @param backEndMessage
     */
    public CDCException(String frontEndMessage, String backEndMessage) {
        super(frontEndMessage);
        this.frontEndMessage = frontEndMessage;
        this.backEndMessage = frontEndMessage + "\n" + backEndMessage;
        errorCode = -1;
    }

    /**
     *
     * @param e
     */
    public CDCException(Exception e) {
        super(e);
        frontEndMessage = e.getMessage();
        backEndMessage = (e.getCause() != null) ? e.getCause().toString() : null;
        if (e instanceof SQLException) {
            errorCode = ((SQLException) e).getErrorCode();
        } else {
            errorCode = -1;
        }
    }

    public CDCException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @return
     */
    public String getFrontEndMessage() {
        return frontEndMessage;
    }

    /**
     *
     * @return
     */
    public int getErrorCode() {
        return errorCode;
    }
}
