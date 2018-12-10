package mx.sharkit.web.service.impl;

import java.io.Serializable;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Empresa;
import mx.sharkit.web.service.EmpresaService;
import org.springframework.stereotype.Service;

/**
 *
 * @author aalquisira
 */
@Service
@Transactional
public class EmpresaServiceImpl extends BaseServiceImpl<Empresa, Integer> implements EmpresaService, Serializable {

}
