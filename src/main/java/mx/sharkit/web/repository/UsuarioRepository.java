package mx.sharkit.web.repository;

import java.util.List;
import mx.sharkit.web.dao.BaseRepository;
import mx.sharkit.web.model.Usuario;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Adrián Salgado
 */
public interface UsuarioRepository extends BaseRepository<Usuario, Long> {

    Usuario findByUserName(String userName);

    @Query(value = "SELECT * FROM cfg_usuario WHERE email = ?1", nativeQuery = true)
    Usuario findByEmailAddress(String emailAddress);

    @Query(value = "SELECT  us.* \n"
            + "FROM	cfg_usuario_rol cr \n"
            + "INNER JOIN cfg_usuario us \n"
            + "    ON (us.id = cr.usuario_id) \n"
            + "WHERE   cr.rol_id = ?1 \n"
            + "AND     us.activo = 1 \n"
            + "ORDER BY CONCAT(nombre, ' ', paterno, ' ', materno) ASC", nativeQuery = true)
    List<Usuario> findByRolIdOrderByNombre(Long rolId);

    @Query(value = "SELECT  us.* \n"
            + "FROM	cfg_usuario_rol cr \n"
            + "INNER JOIN cfg_usuario us \n"
            + "    ON (us.id = cr.usuario_id) \n"
            + "WHERE   cr.rol_id = ?1 \n"
            + "AND   	us.usuario_padre_id = ?2 \n"
            + "AND     us.activo = 1 \n"
            + "ORDER BY CONCAT(nombre, ' ', paterno, ' ', materno) ASC", nativeQuery = true)
    List<Usuario> findByRolIdAndPadreIdOrderByNombre(Long rolId, Long padreId);

}
