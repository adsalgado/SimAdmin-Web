package mx.sharkit.web.service;

import java.util.List;
import java.util.Map;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.ChipConsolidado;

/**
 *
 * @author jlopez
 */
public interface ChipConsolidadoService extends BaseService<ChipConsolidado, Long>{
    public Integer saveChipConsolidado(List<Map<String, Object>> chips) throws Exception;
}
