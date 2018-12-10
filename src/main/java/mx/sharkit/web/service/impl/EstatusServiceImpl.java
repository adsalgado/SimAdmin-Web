package mx.sharkit.web.service.impl;

import java.io.Serializable;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.service.EstatusService;
import org.springframework.stereotype.Service;

/**
 *
 * @author aalquisira
 */
@Service
@Transactional
public class EstatusServiceImpl extends BaseServiceImpl<Estatus, Integer> implements EstatusService, Serializable {

}
