package mx.sharkit.web.service.impl;

import java.io.Serializable;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Workflow;
import mx.sharkit.web.repository.WorkflowRepository;
import mx.sharkit.web.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado
 */
@Service
@Transactional
public class WorkflowServiceImpl extends BaseServiceImpl<Workflow, Integer> 
        implements WorkflowService, Serializable {

    @Autowired
    private WorkflowRepository workflowRepository;

    @Override
    public Workflow findByClave(String clave) {
        return workflowRepository.findByClave(clave);
    }

}
