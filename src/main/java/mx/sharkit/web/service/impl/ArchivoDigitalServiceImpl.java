package mx.sharkit.web.service.impl;

import java.io.Serializable;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.ArchivoDigital;
import mx.sharkit.web.service.ArchivoDigitalService;
import org.springframework.stereotype.Service;

/**
 *
 * @author aalquisira
 */
@Service
@Transactional
public class ArchivoDigitalServiceImpl extends BaseServiceImpl<ArchivoDigital, Long> implements ArchivoDigitalService, Serializable {

}
