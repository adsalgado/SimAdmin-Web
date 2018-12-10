package mx.sharkit.web.service;

import java.util.List;
import java.util.Map;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.ChipPortabilidad;

/**
 *
 * @author jlopez
 */
public interface ChipPortabilidadService extends BaseService<ChipPortabilidad, Long>{
    public Integer savePortabilidad(List<Map<String, Object>> chips) throws Exception;
}
