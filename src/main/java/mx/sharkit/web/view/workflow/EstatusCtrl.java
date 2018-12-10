package mx.sharkit.web.view.workflow;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.catalogos.base.CatalogoBase;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.model.TipoEstatus;
import mx.sharkit.web.service.TipoEstatusService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Adrián Salgado D.
 */
@Setter
@Getter
@Named
@ViewScoped
public class EstatusCtrl extends CatalogoBase<Estatus, Integer> implements Serializable {

    @Autowired
    TipoEstatusService tipoEstatusService;

    private List<TipoEstatus> listTipoEstatus;

    @Override
    @PostConstruct
    public void init() {
        super.init();
        search();
        listTipoEstatus = tipoEstatusService.findAll();
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
