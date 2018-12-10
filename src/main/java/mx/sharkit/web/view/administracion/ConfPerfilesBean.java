package mx.sharkit.web.view.administracion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.model.Opcion;
import mx.sharkit.web.model.Rol;
import mx.sharkit.web.service.RolService;
import mx.sharkit.web.view.util.PFMessages;
import org.apache.commons.lang3.ArrayUtils;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author aalquisira
 */
@Setter
@Getter
@Named
@ViewScoped
public class ConfPerfilesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConfPerfilesBean.class.getName());

    private boolean showPnlConsulta;
    private boolean showPnlModificacion;
    private boolean showPnlPermisos;
    private String tipoActualizacion;

    private List<Rol> currents;
    private Rol current;
    private Rol filterCurrent;
    private Rol selectedCurrent;

    private TreeNode treenode;
    private TreeNode[] selectedOpciones;

    @Autowired
    private RolService rolService;

    @PostConstruct
    public void init() {
        showPanelConsulta();
        search();
        current = new Rol();
        filterCurrent = new Rol();
        currents = rolService.findAll();
    }

    public void search() {
        currents = rolService.findAll();
    }

    public void onCreateRol() {
        current = new Rol();
        tipoActualizacion = "create";
        showPanelModificacion();
    }

    public void create() {

        try {
            Rol r = rolService.findByClaveRol(current.getClaveRol());

            if (r != null) {
                PFMessages.showMessageValidacion("Rol existente.");
                return;
            }
            rolService.save(current);
            search();
            showPanelConsulta();
            PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurrió un error al ejecutar la operación. ", e);
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al ejecutar la operación. ");

        }

    }

    public void update() {
        try {
            rolService.update(current);
            search();
            showPanelConsulta();
            PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurrió un error al ejecutar la operación. ", e);
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al ejecutar la operación. ");

        }

    }

    public void onEditUser() {
        tipoActualizacion = "update";
        showPanelModificacion();
    }

    public void delete() {

        try {
            rolService.deleteById(selectedCurrent.getId());

            search();
            showPanelConsulta();
            PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurrió un error al ejecutar la operación. ", e);
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al ejecutar la operación. ");

        }

    }

    public void onChangePermisos() {
        showPnlPermisos();
        treenode = rolService.getTreeOptionsByRolId(selectedCurrent.getId());
    }

    public void actualizarPermisos() {
        try {
            SortedSet<Long> ss = new TreeSet<>();
            for (TreeNode selOpc : selectedOpciones) {
                for (TreeNode node = selOpc; node != null; node = node.getParent()) {
                    Opcion opc = (Opcion) node.getData();
                    if (opc != null && opc.getId() != 0L) {
                        ss.add(opc.getId());
                    }
                }
            }

            LOGGER.log(Level.INFO, "opcs: {0}", ss);
            List<Long> lstOpcs = new ArrayList<>(ss);
            rolService.savePermisosRol(selectedCurrent, lstOpcs);
            
            showPanelConsulta();
            PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurrió un error al ejecutar la operación. ", e);
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al ejecutar la operación. ");
        }
    }

    public void showPanelConsulta() {
        showPnlConsulta = true;
        showPnlModificacion = false;
        showPnlPermisos = false;
    }

    public void showPanelModificacion() {
        showPnlModificacion = true;
        showPnlConsulta = false;
        showPnlPermisos = false;
    }

    public void showPnlPermisos() {
        showPnlModificacion = false;
        showPnlConsulta = false;
        showPnlPermisos = true;
    }

}
