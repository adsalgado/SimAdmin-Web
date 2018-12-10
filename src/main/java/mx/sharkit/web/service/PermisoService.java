package mx.sharkit.web.service;

import java.util.List;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.Permiso;

/**
 *
 * @author Adrián Salgado
 */
public interface PermisoService extends BaseService<Permiso, Long> {
    Permiso findByRolIdAndOpcionId(Long rolId, Long opcionId);
    List<Permiso> findByOpcionId(Long opcionId);
    List<Permiso> findByRolId(Long rolId);
}
