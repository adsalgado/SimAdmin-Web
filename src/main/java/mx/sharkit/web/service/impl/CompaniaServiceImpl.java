package mx.sharkit.web.service.impl;

import java.io.Serializable;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Compania;
import mx.sharkit.web.service.CompaniaService;
import org.springframework.stereotype.Service;

/**
 *
 * @author aalquisira
 */
@Service
@Transactional
public class CompaniaServiceImpl extends BaseServiceImpl<Compania, Integer> implements CompaniaService, Serializable {

}
