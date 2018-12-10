package mx.sharkit.web.view.catalogos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.catalogos.base.CatalogoBase;
import mx.sharkit.web.model.Empresa;
import mx.sharkit.web.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Adrián Salgado D.
 */
@Setter
@Getter
@Named
@ViewScoped
public class EmpresaBean extends CatalogoBase<Empresa, Integer> implements Serializable {

    @Autowired
    EmpresaService empresaService;

    private List<Empresa> listEmpresa;

    @Override
    @PostConstruct
    public void init() {
        super.init();
        search();
        listEmpresa = empresaService.findAll();
    }

    @Override
    public void doAfterDelete() throws Exception {
        search();
    }

    @Override
    public void doBeforeUpdate() throws Exception {
        current.setFechaUltModificacion(new Date());
        current.setUsuarioUltModificacionId(userDetails.getUser().getId());
    }

    @Override
    public void doAfterUpdate() throws Exception {
        setShowPanelDatos(true);
        search();
    }

    @Override
    public void doBeforeCreate() throws Exception {
        current.setFechaAlta(new Date());
        current.setUsuarioAltaId(userDetails.getUser().getId());
    }

    @Override
    public void doAfterCreate() throws Exception {
        setShowPanelDatos(true);
        search();
    }

}
