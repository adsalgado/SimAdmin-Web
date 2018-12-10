package mx.sharkit.web.dao;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Adrián Salgado
 */
@Setter
@Getter
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;

    public SearchCriteria() {
    }

    public SearchCriteria(String key, String operation, String value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
