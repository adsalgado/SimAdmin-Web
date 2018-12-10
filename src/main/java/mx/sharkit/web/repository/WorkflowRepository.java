package mx.sharkit.web.repository;

import mx.sharkit.web.dao.BaseRepository;
import mx.sharkit.web.model.Workflow;

/**
 *
 * @author Adrián Salgado
 */
public interface WorkflowRepository extends BaseRepository<Workflow, Integer>  {
    Workflow findByClave(String clave); 
}
