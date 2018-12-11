package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.util.List;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.ChipHistoricoEstatus;
import mx.sharkit.web.repository.ChipHistoricoRepository;
import mx.sharkit.web.service.ChipHistoricoService;
import mx.sharkit.web.service.ChipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jlopez
 */
@Service
@Transactional
public class ChipHistoricoServiceImpl extends BaseServiceImpl<ChipHistoricoEstatus, Integer> implements ChipHistoricoService, Serializable {

    @Autowired
    private ChipService chipService;
    @Autowired
    private ChipHistoricoRepository chipHistoricoRepository;
    
    @Override
    public boolean saveChipAndHistory(Chip c, ChipHistoricoEstatus che) {
        try {
            System.out.println(c);
            chipService.update(c);
            che.setSerieId(c.getId());
            save(che);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteByChipId(Long chipId) throws Exception {
        List<ChipHistoricoEstatus> lstHistorico = chipHistoricoRepository.findBySerieId(chipId);
        chipHistoricoRepository.deleteInBatch(lstHistorico);
    }
    
}
