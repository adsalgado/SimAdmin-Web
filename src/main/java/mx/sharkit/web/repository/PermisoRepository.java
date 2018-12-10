package mx.sharkit.web.repository;

import java.util.List;
import mx.sharkit.web.dao.BaseRepository;
import mx.sharkit.web.model.Permiso;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Adrián Salgado
 */
public interface PermisoRepository extends BaseRepository<Permiso, Long> {

    List<Permiso> findByOpcionId(Long opcionId);

    List<Permiso> findByRolId(Long rolId);

    @Modifying
    @Query("delete from Permiso pe where pe.id = ?1")
    void deleteById(Long id);

    Permiso findByRolIdAndOpcionId(Long rolId, Long opcionId);
    
}
