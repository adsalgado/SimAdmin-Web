package mx.sharkit.web.service;

import java.util.List;
import java.util.Map;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.ChipResidual;

/**
 *
 * @author jlopez
 */
public interface ChipResidualService extends BaseService<ChipResidual, Long>{
    public Integer saveResidual(List<Map<String, Object>> chips) throws Exception;
}
