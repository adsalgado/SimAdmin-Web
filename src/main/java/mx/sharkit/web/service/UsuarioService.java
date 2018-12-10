package mx.sharkit.web.service;

import java.util.List;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.model.UsuarioRol;

/**
 *
 * @author Adrián Salgado
 */
public interface UsuarioService extends BaseService<Usuario, Long> {
    Usuario findUserByUserName(String userName);
    boolean isUserExist(Usuario user);
    Usuario findByEmailAddress(String emailAddress);
    List<UsuarioRol> getRolesByUsername(String userName);
    void saveUsuarioPerfiles(Usuario usuario, List<Long> roles);    
    List<UsuarioRol> getRolesByRolKey(String rolKey);
    List<Usuario> findByRolIdOrderByNombre(Long rolId);
    List<Usuario> findByRolIdAndPadreIdOrderByNombre(Long rolId, Long padreId);
}
