package mx.sharkit.web.service;

import java.util.List;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.WorkflowConfig;

/**
 *
 * @author Adrián Salgado
 */
public interface WorkflowConfigService extends BaseService<WorkflowConfig, Integer> {
    List<WorkflowConfig> findWorkflowConfigByClaveAndEstatus(String clave, Integer estatusId);
}
