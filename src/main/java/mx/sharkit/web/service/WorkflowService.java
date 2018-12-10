package mx.sharkit.web.service;

import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.Workflow;

/**
 *
 * @author Adrián Salgado
 */
public interface WorkflowService extends BaseService<Workflow, Integer> {
    Workflow findByClave(String clave); 
}
