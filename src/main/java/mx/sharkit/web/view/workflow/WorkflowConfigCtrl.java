package mx.sharkit.web.view.workflow;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.catalogos.base.CatalogoBase;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.model.TipoEstatus;
import mx.sharkit.web.model.Workflow;
import mx.sharkit.web.model.WorkflowConfig;
import mx.sharkit.web.service.EstatusService;
import mx.sharkit.web.service.TipoEstatusService;
import mx.sharkit.web.service.WorkflowService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Adrián Salgado D.
 */
@Setter
@Getter
@Named
@ViewScoped
public class WorkflowConfigCtrl extends CatalogoBase<WorkflowConfig, Integer> implements Serializable {

    @Autowired
    WorkflowService workflowService;

    @Autowired
    EstatusService estatusService;
    
    @Autowired
    TipoEstatusService tipoEstatusService;
    
    private List<Workflow> listWorkflow;
    private List<Estatus> listEstatus;
    private List<TipoEstatus> listTipoEstatus;
    
    private Integer tipoEstatusId;
    
    @Override
    @PostConstruct
    public void init() {
        super.init();
        search();
        listWorkflow = workflowService.findAll();
        listTipoEstatus = tipoEstatusService.findAll();
    }
    
    public void onTipoEstatusChange() {
        
        if (tipoEstatusId != null) {
            DetachedCriteria criteriaEst = DetachedCriteria.forClass(Estatus.class);
            criteriaEst.add(Restrictions.eq("tipoEstatusId", tipoEstatusId));
            listEstatus = estatusService.findByCriteria(criteriaEst);
        } else {
          if (listEstatus != null) {
              listEstatus.clear();
          }  
        }
        
    }
    
    public void onConfigSelected() {
        if (current != null && current.getEstatusActual() != null) {
            tipoEstatusId = current.getEstatusActual().getTipoEstatusId();
            if (tipoEstatusId != null) {
                onTipoEstatusChange();
            }
        }
    }

    @Override
    public void cleanObject() {
        super.cleanObject(); 
        tipoEstatusId = null;
    }

    @Override
    public void doBeforeDelete() throws Exception {

    }

    @Override
    public void doBeforeUpdate() throws Exception {
        if (current != null) {
            current.setFechaUltModificacion(new Date());
            current.setUsuarioUltModificacion(getUserDetails().getUsername());
        }
    }

    @Override
    public void doBeforeCreate() throws Exception {
        if (current != null) {
            current.setFechaAlta(new Date());
            current.setUsuarioAlta(getUserDetails().getUsername());
        }
    }

    @Override
    public void doAfterDelete() throws Exception {
        search();
    }

    @Override
    public void doAfterUpdate() throws Exception {
        setShowPanelDatos(true);
    }

    @Override
    public void doAfterCreate() throws Exception {
        setShowPanelDatos(true);
    }
    
}
