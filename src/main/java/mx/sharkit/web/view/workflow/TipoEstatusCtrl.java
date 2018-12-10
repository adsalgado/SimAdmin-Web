package mx.sharkit.web.view.workflow;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.catalogos.base.CatalogoBase;
import mx.sharkit.web.model.TipoEstatus;

/**
 *
 * @author Adrián Salgado D.
 */
@Setter
@Getter
@Named
@ViewScoped
public class TipoEstatusCtrl extends CatalogoBase<TipoEstatus, Integer> implements Serializable {

    @Override
    @PostConstruct
    public void init() {
        super.init();
        search();
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
