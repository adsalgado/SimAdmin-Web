package mx.sharkit.web.service.impl;

import java.io.Serializable;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Transaccion;
import mx.sharkit.web.service.TransaccionService;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado D.
 */
@Service
@Transactional
public class TransaccionServiceImpl extends BaseServiceImpl<Transaccion, Long> 
        implements TransaccionService, Serializable {
    
}
