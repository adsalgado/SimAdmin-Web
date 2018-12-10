package mx.sharkit.web.repository;

import java.util.List;
import mx.sharkit.web.dao.BaseRepository;
import mx.sharkit.web.model.DefinicionParametros;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Adrián Salgado D.
 */
public interface DefinicionParametrosRepository extends BaseRepository<DefinicionParametros, Integer> {
    List<DefinicionParametros> findByTipoParametroId(Integer tipoParametroId); 
    
    @Query(value = 
            "SELECT rep.* \n" +
            "FROM   cfg_definicion_parametros sec, cfg_definicion_parametros rep \n" +
            "WHERE  sec.tipo_parametro_id = ?1 \n" +
            "AND    sec.clave_parametro = ?2 \n" +
            "AND    rep.parent_id = sec.id \n" +
            "AND    rep.clave_parametro = ?3", nativeQuery = true)
    DefinicionParametros getParametroByTipoAndSeccionAndClave(Integer tipoParametro, String sCveScript, String sOperacion);
}
