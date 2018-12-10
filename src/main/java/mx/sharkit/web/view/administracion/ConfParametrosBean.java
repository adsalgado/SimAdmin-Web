package mx.sharkit.web.view.administracion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.model.DefinicionParametros;
import mx.sharkit.web.model.TipoParametro;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.DefinicionParametrosService;
import mx.sharkit.web.view.util.PFMessages;
import org.hibernate.criterion.DetachedCriteria;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Adrián Salgado D.
 */
@Setter
@Getter
@Named
@ViewScoped
public class ConfParametrosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConfParametrosBean.class.getName());
    private static final long serialVersionUID = 1L;

    @Autowired
    DefinicionParametrosService parametrosService;

    private TreeNode root;
    private TreeNode selectedNode;
    private Integer idTipo;
    private List<TipoParametro> tipos;
    private List<DefinicionParametros> parametros;
    private DefinicionParametros instance;
    private boolean showPanelDatos;
    private String tipoActualizacion;
    private SSUserDetails userDetails;

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DetachedCriteria crt = DetachedCriteria.forClass(TipoParametro.class);
        tipos = parametrosService.findByCriteria(crt);
        root = new DefaultTreeNode();
        showPanelDatos = true;
        instance = new DefinicionParametros();
    }
    
    public void search() {

    }

    public void cleanObject() {
        instance = new DefinicionParametros();
        if (selectedNode != null) {
            DefinicionParametros selected = (DefinicionParametros) selectedNode.getData();
            instance.setParentId(selected.getId());
            instance.setTipoParametroId(selected.getTipoParametroId());
        }
    }

    public void create() {
        try {
            instance.setUsuarioAlta(userDetails.getUsername());
            instance.setFechaAlta(new Date());
            showPanelDatos = true;
            parametrosService.save(instance);
            onTipoParametroChange();

            RequestContext.getCurrentInstance().addCallbackParam("saved", true);
            PFMessages.showMessageInfo("LA OPERACION SE REALIZO CORRECTAMENTE.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurri\u00f3 un error al guardar el registro. {0}", e.getMessage());
            RequestContext.getCurrentInstance().addCallbackParam("saved", false);
            PFMessages.showMessageErrorBackEnd("Ocurri\u00f3 un error al guardar el registro. " + e.getMessage());
        }
    }

    public void onRowEdit() {
        if (selectedNode != null) {
            instance = (DefinicionParametros) selectedNode.getData();
        }
    }

    public void update() {
        try {
            instance.setUsuarioUltModificacion(userDetails.getUsername());
            instance.setFechaUltModificacion(new Date());
            showPanelDatos = false;
            parametrosService.update(instance);
            onTipoParametroChange();

            RequestContext.getCurrentInstance().addCallbackParam("saved", true);
            PFMessages.showMessageInfo("LA OPERACION SE REALIZO CORRECTAMENTE.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurri\u00f3 un error al actualizar el registro. {0}", e.getMessage());
            RequestContext.getCurrentInstance().addCallbackParam("saved", false);
            PFMessages.showMessageErrorBackEnd("Ocurri\u00f3 un error al actualizar el registro. " + e.getMessage());
        }
    }

    public void delete() {
        try {
            instance = (DefinicionParametros) selectedNode.getData();
            parametrosService.deleteById(instance.getId());
            showPanelDatos = true;
            onTipoParametroChange();

            RequestContext.getCurrentInstance().addCallbackParam("saved", true);
            PFMessages.showMessageInfo("LA OPERACION SE REALIZO CORRECTAMENTE.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurri\u00f3 un error al eliminar el registro. {0}", e.getMessage());
            RequestContext.getCurrentInstance().addCallbackParam("saved", false);
            PFMessages.showMessageErrorBackEnd("Ocurri\u00f3 un error al eliminar el registro. " + e.getMessage());
        }
    }

    public void onTipoParametroChange() {
        if (idTipo != null) {
            parametros = parametrosService.findByTipoParametroId(idTipo);
            root = buildTreeRecursive(parametros);
        } else {
            root = new DefaultTreeNode();
        }
    }

    private TreeNode buildTreeRecursive(List<DefinicionParametros> params) {
        TreeNode rootTree = new DefaultTreeNode();

        for (DefinicionParametros param : params) {
            if (param.getParentId() == null) {
                buildTree(rootTree, params, param, 1);
            }
        }

        return rootTree;
    }

    private void buildTree(TreeNode treeNode, List<DefinicionParametros> params, DefinicionParametros param, int level) {
        List<DefinicionParametros> listSubParams = getSubParams(params, param);
        
        TreeNode node = null;

        if (listSubParams != null && !listSubParams.isEmpty()) {
             node = new DefaultTreeNode(param, treeNode);

            for (DefinicionParametros cfg : listSubParams) {
                buildTree(node, params, cfg, level++);
            }

        } else {
             node = new DefaultTreeNode(param, treeNode);
        }
    }
    
    private List<DefinicionParametros> getSubParams(List<DefinicionParametros> params, DefinicionParametros param) {
        List<DefinicionParametros> subparam = new ArrayList<>();
        for (DefinicionParametros subconsulta : params) {
            Integer parentId = subconsulta.getParentId() != null ? subconsulta.getParentId() : -1;
            if (param.getId().compareTo(parentId) == 0 && (subconsulta.getId().compareTo(parentId) != 0)) {
                subparam.add(subconsulta);
            }
        }
        return subparam;
    }

    
}
