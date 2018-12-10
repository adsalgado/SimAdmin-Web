package mx.sharkit.web.service.impl;

import java.io.Serializable;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.CierreChip;
import mx.sharkit.web.service.CierreChipService;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado
 */
@Service
@Transactional
public class CierreChipServiceImpl extends BaseServiceImpl<CierreChip, Long> 
        implements Serializable, CierreChipService {
    
}
