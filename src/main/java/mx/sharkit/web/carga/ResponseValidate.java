package mx.sharkit.web.carga;

import java.util.Map;

/**
 *
 * @author asalgado
 */
public class ResponseValidate {

    private boolean valid = true;
    private int errorLine;
    private String message;
    private Map<String, Object> data;

    public ResponseValidate() {

    }

    public ResponseValidate(boolean valid, int errorLine, String message) {
        this.valid = valid;
        this.message = message;
        this.errorLine = errorLine;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorLine() {
        return errorLine;
    }

    public void setErrorLine(int errorLine) {
        this.errorLine = errorLine;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseValidate{" + "valid=" + valid + ", errorLine=" + errorLine + ", message=" + message + ", data=" + data + '}';
    }

}
