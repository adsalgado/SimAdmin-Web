package mx.sharkit.web.service;

import java.util.List;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.Opcion;

/**
 *
 * @author Adrián Salgado
 */
public interface OpcionService extends BaseService<Opcion, Long> {
    List<Opcion> findOpcionesByRolesId(List<Long> roles);
    List<Opcion> findByOpcionIdIsNullOrderByOrden();
    void saveOpcionPerfiles(Opcion opcion, List<Long> roles) throws Exception;
}
