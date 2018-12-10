package mx.sharkit.web.repository;

import java.util.List;
import mx.sharkit.web.dao.BaseRepository;
import mx.sharkit.web.model.UsuarioRol;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Adrián Salgado
 */
public interface UsuarioRolRepository extends BaseRepository<UsuarioRol, Integer> {

    @Query("select ur from UsuarioRol ur where ur.usuario.userName = ?1")
    List<UsuarioRol> findByUserName(String userName);

    @Modifying
    @Query("delete from UsuarioRol ur where ur.id = ?1")
    void deleteById(Integer entityId);
    
    @Query("select ur from UsuarioRol ur where ur.rol.claveRol = ?1")
    List<UsuarioRol> findByRolKey(String rolKey);
    
}
