package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.util.List;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.WorkflowConfig;
import mx.sharkit.web.repository.WorkflowConfigRepository;
import mx.sharkit.web.service.WorkflowConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado
 */
@Service
@Transactional
public class WorkflowConfigServiceImpl extends BaseServiceImpl<WorkflowConfig, Integer> 
        implements WorkflowConfigService, Serializable {

    @Autowired
    WorkflowConfigRepository workflowConfigRepository;
    
    @Override
    public List<WorkflowConfig> findWorkflowConfigByClaveAndEstatus(String clave, Integer estatusId) {
        return workflowConfigRepository.findWorkflowConfigByClaveAndEstatus(clave, estatusId);
    }

}
