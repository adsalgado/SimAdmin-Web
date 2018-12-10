package mx.sharkit.web.service;

import java.util.List;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.ChipDispersionSaldos;

/**
 *
 * @author jlopez
 */
public interface ChipDispersionSaldosService extends BaseService<ChipDispersionSaldos, Long>{
    public Integer saveCargaDispersion(List<ChipDispersionSaldos> chips) throws Exception;
}
