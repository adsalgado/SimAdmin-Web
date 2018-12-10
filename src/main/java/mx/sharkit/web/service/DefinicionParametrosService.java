package mx.sharkit.web.service;

import java.util.List;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.DefinicionParametros;

/**
 *
 * @author Adrián Salgado D.
 */
public interface DefinicionParametrosService extends BaseService<DefinicionParametros, Integer> {
    List<DefinicionParametros> findByTipoParametroId(Integer tipoParametroId); 
    DefinicionParametros getParametroByTipoAndSeccionAndClave(Integer tipoParametro, String sCveScript, String sOperacion);
}
