package mx.sharkit.web.repository;

import java.util.List;
import mx.sharkit.web.dao.BaseRepository;
import mx.sharkit.web.model.ChipHistoricoEstatus;

/**
 *
 * @author jlopez
 */
public interface ChipHistoricoRepository extends BaseRepository<ChipHistoricoEstatus, Integer>{
    List<ChipHistoricoEstatus> findBySerieId(Long chipId);
}
