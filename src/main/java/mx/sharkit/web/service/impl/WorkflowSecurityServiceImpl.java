package mx.sharkit.web.service.impl;

import java.io.Serializable;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.WorkflowSecurity;
import mx.sharkit.web.service.WorkflowSecurityService;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado
 */
@Service
@Transactional
public class WorkflowSecurityServiceImpl extends BaseServiceImpl<WorkflowSecurity, Integer> 
        implements WorkflowSecurityService, Serializable {

}
