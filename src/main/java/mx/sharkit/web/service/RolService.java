package mx.sharkit.web.service;

import java.util.List;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.Opcion;
import mx.sharkit.web.model.Rol;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Adrián Salgado
 */
public interface RolService extends BaseService<Rol, Long> {
    Rol findByClaveRol(String claveRol); 
    List<Opcion> findOpcionesByRolId(Long rolId);
    TreeNode getTreeOptionsByRolId(Long rolId);
    void savePermisosRol(Rol rol, List<Long> opciones) throws Exception;
}
