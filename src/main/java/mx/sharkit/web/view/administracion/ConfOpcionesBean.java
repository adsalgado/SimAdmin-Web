package mx.sharkit.web.view.administracion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.model.Opcion;
import mx.sharkit.web.model.Rol;
import mx.sharkit.web.service.OpcionService;
import mx.sharkit.web.service.RolService;
import mx.sharkit.web.view.util.PFMessages;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Adrián Salgado D.
 */
@Setter
@Getter
@Named
@ViewScoped
public class ConfOpcionesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConfOpcionesBean.class.getName());

    @Autowired
    private OpcionService opcionService;

    @Autowired
    private RolService rolService;

    private boolean showPnlConsulta;
    private boolean showPnlModificacion;
    private String tipoActualizacion;

    private List<Opcion> currents;
    private List<Opcion> listaMenu;
    private Opcion current;
    private Opcion filterCurrent;
    private Opcion selectedCurrent;

    private List<Rol> listaPerfiles;
    private List<String> selectedPerfiles;
    private Map<String, Rol> mpRoles;

    @PostConstruct
    public void init() {
        showPanelConsulta();
        search();
        current = new Opcion();
        filterCurrent = new Opcion();
        currents = opcionService.findAll();
        listaMenu = opcionService.findByOpcionIdIsNullOrderByOrden();
        listaPerfiles = rolService.findAll();
        mpRoles = new HashMap<>();
        for (Rol role : listaPerfiles) {
            mpRoles.put(role.getClaveRol(), role);
        }
    }

    public void showPanelConsulta() {
        showPnlConsulta = true;
        showPnlModificacion = false;
    }

    public void search() {
        currents = opcionService.findAll();
    }

    public void onCreateOpcion() {
        current = new Opcion();
        tipoActualizacion = "create";
        showPanelModificacion();
    }

    public void showPanelModificacion() {
        showPnlModificacion = true;
        showPnlConsulta = false;
    }

    public void create() {

        try {

            List<Long> idRoles = new ArrayList<>();
            for (String cveRol : selectedPerfiles) {
                if (mpRoles.containsKey(cveRol)) {
                    idRoles.add(mpRoles.get(cveRol).getId());
                }
            }

            opcionService.saveOpcionPerfiles(current, idRoles);

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
            opcionService.update(current);
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
            opcionService.deleteById(selectedCurrent.getId());

            search();
            showPanelConsulta();
            PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurrió un error al ejecutar la operación. ", e);
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al ejecutar la operación. ");

        }

    }

}
