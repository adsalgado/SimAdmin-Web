package mx.sharkit.web.view.workflow;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.catalogos.base.CatalogoBase;
import mx.sharkit.web.model.Workflow;

/**
 *
 * @author Adrián Salgado D.
 */
@Setter
@Getter
@Named
@ViewScoped
public class WorkflowCtrl extends CatalogoBase<Workflow, Integer> implements Serializable {

    @Override
    @PostConstruct
    public void init() {
        super.init();
        search();
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
        search();
    }

    @Override
    public void doAfterCreate() throws Exception {
        setShowPanelDatos(true);
        search();
    }
    
}
