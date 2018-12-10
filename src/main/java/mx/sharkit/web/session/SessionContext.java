package mx.sharkit.web.session;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.model.Opcion;
import mx.sharkit.web.model.UsuarioRol;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.OpcionService;
import mx.sharkit.web.service.UsuarioService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Adrián Salgado
 */
@Setter
@Getter
@Named
@SessionScoped
public class SessionContext {

    private static final Logger LOGGER = Logger.getLogger(SessionContext.class.getName());

    @Autowired
    OpcionService opcionService;

    @Autowired
    UsuarioService usuarioService;

    private HttpServletRequest request;
    private SSUserDetails userDetails;
    private List<Opcion> opciones;
    private String menuHtml;

    @PostConstruct
    public void init() {
        validateUserSession();
    }

    private void validateUserSession() {

        if (userDetails == null) {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SSUserDetails) {
                    userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    if (userDetails != null) {
                        if (opciones == null) {
                            List<UsuarioRol> roles = usuarioService.getRolesByUsername(userDetails.getUsername());
                            List<Long> lstId = new ArrayList<>();
                            for (UsuarioRol role : roles) {
                                lstId.add(role.getRolId());
                            }
                            opciones = opcionService.findOpcionesByRolesId(lstId);
                        }
                    }
                }
            }
        }

    }

    public boolean isUserAuthenticated() {
        validateUserSession();
        return userDetails != null;
    }

    public UserDetails getUserDetails() {
        validateUserSession();
        return userDetails;
    }

    public String getNombreUsuario() {
        validateUserSession();
        if (userDetails != null) {
            return userDetails.getUser().getNombre() + " " + userDetails.getUser().getPaterno() + " " + userDetails.getUser().getMaterno();
        } else {
            return "";
        }
    }

    public void keepSessionAlive() {
        // do nothing
    }

    public String getMenuHtml() {
        menuHtml = "";
        try {
            validateUserSession();
            if (opciones != null) {
                menuHtml = buildMenuRecursive(opciones);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return menuHtml;
    }

    private String buildMenuRecursive(List<Opcion> opciones) {
        StringBuilder sbMenu = new StringBuilder();

        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        for (Opcion opcion : opciones) {
            if (opcion.getOpcionId() == null) {
                buildMenuHtml(sbMenu, opciones, opcion, 1);
            }
        }

        return sbMenu.toString();
    }

    private void buildMenuHtml(StringBuilder sbMenu, List<Opcion> menus, Opcion opc, int level) {
        List<Opcion> listSubMenu = getSubMenus(opciones, opc);
        if (listSubMenu != null && !listSubMenu.isEmpty()) {
            String styleClass = "";
            String requestUrl = request.getRequestURL().toString();

            boolean activeMenu = isActiveMenu(opc, requestUrl);
            if (activeMenu) {
                styleClass = " active";
            }

            sbMenu.append("                <li class=\"treeview").append(styleClass).append("\">\n");
            sbMenu.append("                    <a href=\"#\">\n");
            sbMenu.append("                        <i class=\"").append(opc.getImagen()).append("\"></i> <span>").append(opc.getNombre()).append("</span> <i class=\"fa fa-angle-left pull-right\"></i>\n");
            sbMenu.append("                    </a>\n");

            if (!StringUtils.isBlank(styleClass)) {
                sbMenu.append("                    <ul class=\"treeview-menu menu-open\" style=\"display: block;\">\n");
            } else {
                sbMenu.append("                    <ul class=\"treeview-menu\">\n");
            }

            for (Opcion opcion : listSubMenu) {
                buildMenuHtml(sbMenu, menus, opcion, level++);
            }
            sbMenu.append("                    </ul>\n");
            sbMenu.append("                </li>\n");

        } else {
            StringBuilder sbHoja = new StringBuilder();
            /*
             <li><a href="pages/layout/top-nav.html"><i class="fa fa-circle-o"></i> Top Navigation</a></li>
             */

            String styleClass = "";
            String requestUrl = request.getRequestURL().toString();
            if (StringUtils.contains(requestUrl, opc.getUrl())) {
                styleClass = "active";
            }

            sbHoja.append("                        <li class=\"").append(styleClass).append("\"> \n");
            sbHoja.append("                            <a href=\"").append(request.getContextPath()).append("/").append(opc.getUrl()).append("\">");
            sbHoja.append("                                <i class=\"").append(opc.getImagen()).append("\"></i> <span>").append(opc.getNombre()).append("</span>");
            sbHoja.append("                            </a> \n");
            sbHoja.append("                        </li> \n");
            sbMenu.append(sbHoja);

        }
    }

    private List<Opcion> getSubMenus(List<Opcion> menus, Opcion opcion) {
        List<Opcion> submenu = new ArrayList<>();
        for (Opcion subopcion : menus) {
            Long parentId = (subopcion.getOpcionId() != null && subopcion.getOpcionId() != null)
                    ? subopcion.getOpcionId() : -1L;
            if (opcion.getId().intValue() == parentId.intValue() && (subopcion.getId().intValue() != parentId.intValue())) {
                submenu.add(subopcion);
            }
        }
        return submenu;
    }

    private boolean isActiveMenu(Opcion opc, String requestUrl) {
        boolean activeMenu = false;
        List<Opcion> subOpciones = getSubMenus(opciones, opc);
        if (subOpciones != null && !subOpciones.isEmpty()) {
            for (Opcion subOpc : subOpciones) {
                if (isActiveMenu(subOpc, requestUrl)) {
                    activeMenu = true;
                }
            }
        } else if (StringUtils.contains(requestUrl, opc.getUrl())) {
            activeMenu = true;
        }
        return activeMenu;
    }

}
