package mx.sharkit.web.service;

import java.util.List;
import java.util.Map;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.ChipVentanaSiv;

/**
 *
 * @author jlopez
 */
public interface ChipVentanaSivService extends BaseService<ChipVentanaSiv, Long>{
    public Integer saveVentanaSiv(List<Map<String, Object>> chips) throws Exception;
}
