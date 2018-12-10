package mx.sharkit.web.service;

import java.util.List;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.Sucursal;

/**
 *
 * @author aalquisira
 */
public interface SucursalService extends BaseService<Sucursal, Integer>{
    List<Sucursal> findByEmpresaIdOrderByNombre(Integer empresaId);
}
