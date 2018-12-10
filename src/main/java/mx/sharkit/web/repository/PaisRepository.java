
package mx.sharkit.web.repository;
import java.util.List;
import mx.sharkit.web.dao.BaseRepository;
import mx.sharkit.web.model.Pais;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author malcantara
 */
public interface PaisRepository extends BaseRepository<Pais, Integer> {
    
     @Query("select p from Pais p where p.id = ?1")
    Pais findByPaisId(int paisId);
    
   
    
}
