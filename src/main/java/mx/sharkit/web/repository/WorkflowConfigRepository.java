package mx.sharkit.web.repository;

import java.util.List;
import mx.sharkit.web.dao.BaseRepository;
import mx.sharkit.web.model.WorkflowConfig;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Adrián Salgado
 */
public interface WorkflowConfigRepository extends BaseRepository<WorkflowConfig, Integer> {

    @Query(value
            = "SELECT \n"
            + "    wc.*\n"
            + "FROM \n"
            + "    cfg_workflow_config wc \n"
            + "INNER JOIN \n"
            + "    cfg_workflow wo ON (wo.id = wc.workflow_id) \n"
            + "WHERE \n"
            + "    wo.clave = ?1 \n"
            + "AND estatus_actual_id = ?2 \n"
            + "ORDER BY orden", nativeQuery = true)
    List<WorkflowConfig> findWorkflowConfigByClaveAndEstatus(String clave, Integer estatusId);

    @Query(value
            = "SELECT 	distinct wc.*\n" +
            "\n" +
            "FROM 	cfg_workflow_config wc\n" +
            "INNER JOIN cfg_workflow wo \n" +
            "	ON (wo.id = wc.workflow_id)\n" +
            "INNER JOIN cfg_workflow_security ws \n" +
            "	ON (ws.workflow_config_id = wc.id)\n" +
            "    \n" +
            "WHERE 	wo.clave = ?1\n" +
            "AND 	wc.estatus_actual_id = ?2\n" +
            "AND 	ws.rol_id IN (?3)\n" +
            "\n" +
            "ORDER BY orden", nativeQuery = true)
    List<WorkflowConfig> findWorkflowConfigByClaveAndEstatusAndRoles(String clave, Integer estatusId, List<Long> roles);

}
