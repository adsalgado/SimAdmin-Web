package mx.sharkit.web.service.impl;

import java.io.Serializable;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.TipoEstatus;
import mx.sharkit.web.service.TipoEstatusService;
import org.springframework.stereotype.Service;

/**
 *
 * @author aalquisira
 */
@Service
@Transactional
public class TipoEstatusServiceImpl extends BaseServiceImpl<TipoEstatus, Integer> implements TipoEstatusService, Serializable {

}
