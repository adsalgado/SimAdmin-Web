package mx.sharkit.web.repository;

import mx.sharkit.web.dao.BaseRepository;
import mx.sharkit.web.model.Rol;

/**
 *
 * @author Adrián Salgado
 */
public interface RolRepository extends BaseRepository<Rol, Long>  {
    Rol findByClaveRol(String claveRol); 
}
