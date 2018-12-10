package mx.sharkit.web.view.administracion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import mx.sharkit.web.model.Cuidad;
import mx.sharkit.web.model.Empresa;
import mx.sharkit.web.model.Pais;
import mx.sharkit.web.model.Rol;
import mx.sharkit.web.model.Sucursal;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.model.UsuarioRol;
import mx.sharkit.web.security.CustomPasswordEncoder;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.EmpresaService;
import mx.sharkit.web.service.PaisService;
import mx.sharkit.web.service.RolService;
import mx.sharkit.web.service.SucursalService;
import mx.sharkit.web.service.UsuarioRolService;
import mx.sharkit.web.service.UsuarioService;
import mx.sharkit.web.view.util.PFMessages;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Adrián Salgado
 */
@Setter
@Getter
@Named
@ViewScoped
public class ConfUsuariosBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ConfUsuariosBean.class.getName());

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @Autowired
    private PaisService paisService;
    
    @Autowired
    private EmpresaService empresaService;
    
    @Autowired
    private SucursalService sucursalService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    private List<Usuario> currents;
    private Usuario current;
    private Usuario selectedCurrent;

    private DualListModel<String> dualRoles;
    private List<String> sourceRoles;
    private List<String> targetRoles;
    private List<Rol> roles;
    private List<Usuario> users;
    private List<Pais> paises;
    private Pais cPais;
    private List<Cuidad> ciudades;
    private Map<String, Rol> mpRoles;
    private List<Empresa> empresas;
    private List<Sucursal> sucursales;
    
    private Long roleId;
    private String confirmPassword;
    private boolean showPnlConsulta;
    private boolean showPnlModificacion;
    private String tipoActualizacion;
    private boolean showPnlDireccion;
    private boolean showPnlUser;
    private Rol cRole;

    private SSUserDetails userDetails;

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        showPanelConsulta();
        search();
        current = new Usuario();
        
        DetachedCriteria dc = DetachedCriteria.forClass(Rol.class);
        dc.add(Restrictions.ne("id", 7L));
        roles = rolService.findByCriteria(dc);
//        roles = rolService.findAll();
        
        
        mpRoles = new HashMap<>();
        for (Rol role : roles) {
            mpRoles.put(role.getClaveRol(), role);
        }
        
        empresas = empresaService.findAll();
        
        initDualList();
    }

    private void initDualList() {
        if (current != null) {
            if (current.getId() == null) {
                sourceRoles = new ArrayList<>();
                for (Rol role : roles) {
                    sourceRoles.add(role.getClaveRol());
                }
                targetRoles = new ArrayList<>();
                dualRoles = new DualListModel<>(sourceRoles, targetRoles);
            } else {
                List<UsuarioRol> ur = usuarioService.getRolesByUsername(current.getUserName());
                targetRoles = new ArrayList<>();
                for (UsuarioRol usuarioRol : ur) {
                    targetRoles.add(usuarioRol.getRol().getClaveRol());
                    roleId = usuarioRol.getRol().getId();
                }
                chargeOptionals();
            }
        }
    }
    
    public void onEmpresaChange() {
        if (current.getEmpresaId() != null || current.getEmpresaId() != 0) {
            sucursales = sucursalService.findByEmpresaIdOrderByNombre(current.getEmpresaId());
        }
    }

    public void search() {
      
        DetachedCriteria dc = DetachedCriteria.forClass(Usuario.class);
        dc.add(Restrictions.eq("activo", 1));
        dc.add(Restrictions.ne("id", 2L));
        currents = usuarioService.findByCriteria(dc);

        //currents = usuarioService.findAll();
    }

    public void onEditUser() {
        tipoActualizacion = "update";
        initDualList();
        showPanelModificacion();
        if (current != null && current.getEmpresaId() != null) {
            onEmpresaChange();
        }
    }

    public void onCreateUser() {
        current = new Usuario();
        tipoActualizacion = "create";
        initDualList();
        showPanelModificacion();
        if (sucursales != null && !sucursales.isEmpty()) {
            sucursales.clear();
        }
    }

    public void create() {

        try {
            current.setPassword(customPasswordEncoder.encode(current.getPassword()));
            current.setFechaAlta(new Date());

            List<Long> idRoles = new ArrayList<>();
            idRoles.add(roleId);
//            for (String cveRol : dualRoles.getTarget()) {
//                if (mpRoles.containsKey(cveRol)) {
//                    idRoles.add(mpRoles.get(cveRol).getId());
//                }
//            }
            usuarioService.saveUsuarioPerfiles(current, idRoles);

            /**
             * * TRANSACTION LOGS **
             */
            StringBuilder transactionLogs = new StringBuilder("[");
            transactionLogs.append("Id: ").append(current.getId());
            transactionLogs.append(", Username: ").append(current.getUserName());
            transactionLogs.append(", Roles: ").append(idRoles);
            transactionLogs.append(", Roles: ").append(idRoles);
            transactionLogs.append(", Roles: ").append(idRoles);
            transactionLogs.append(", Roles: ").append(idRoles);
            transactionLogs.append(", Roles: ").append(idRoles);
            transactionLogs.append("]");
            userDetails.saveTransaccion("ALTA_USUARIO", transactionLogs.toString());

//        usuarioRolService.save(ur);
//            search();
            showPanelConsulta();
            PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurrió un error al ejecutar la operación. ", e);
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al ejecutar la operación. ");

        }

    }

    public void update() {
        try {
            current.setFechaUltModificacion(new Date());

            List<Long> idRoles = new ArrayList<>();
            idRoles.add(roleId);
            usuarioService.saveUsuarioPerfiles(current, idRoles);

            /**
             * * TRANSACTION LOGS **
             */
            StringBuilder transactionLogs = new StringBuilder("[");
            transactionLogs.append("Id: ").append(current.getId());
            transactionLogs.append(", Username: ").append(current.getUserName());
            transactionLogs.append(", Roles: ").append(idRoles);
            transactionLogs.append("]");
            userDetails.saveTransaccion("MODIFICACION_USUARIO", transactionLogs.toString());

//        usuarioRolService.save(ur);
//            search();
            showPanelConsulta();
            PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurrió un error al ejecutar la operación. ", e);
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al ejecutar la operación. ");

        }

    }

    public void delete() {

        try {
            selectedCurrent.setActivo(0);
            selectedCurrent.setFechaUltModificacion(new Date());
            selectedCurrent.setCuentaBloqueada("F");
            usuarioService.update(selectedCurrent);

//        usuarioRolService.save(ur);
            search();
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
    }

    public void showPanelModificacion() {
        showPnlModificacion = true;
        showPnlConsulta = false;
    }

    public void showOptionsRole() {
        limpiarDatos();
        cRole = rolService.findById(roleId);
        List<UsuarioRol> ur;

        current.setPais(null);
        current.setZona(null);
        current.setCuidad(null);
        current.setPaisId(null);
        current.setZonaId(null);
        current.setCuidadId(null);
        current.setUsuarioPadre(null);
        current.setUsuarioPadreId(null);

        switch (cRole.getClaveRol()) {
            case "ROLE_COORDINADOR":
                showPnlDireccion = true;
                showPnlUser = false;
                paises = paisService.findAll();
                break;
            case "ROLE_SUPERVISOR":
            case "ROLE_VENDEDOR":
                showPnlDireccion = false;
                showPnlUser = true;

                users = new ArrayList();
                if (cRole.getClaveRol().equals("ROLE_SUPERVISOR")) {
                    ur = usuarioService.getRolesByRolKey("ROLE_COORDINADOR");
                } else {
                    ur = usuarioService.getRolesByRolKey("ROLE_SUPERVISOR");
                }
                for (UsuarioRol item : ur) {
                    users.add(item.getUsuario());
                }
                break;
                case "ROL_MAYORISTA":
                showPnlDireccion = false;
                showPnlUser = true;

                users = new ArrayList();
                if (cRole.getClaveRol().equals("ROLE_COORDINADOR")) {
                    ur = usuarioService.getRolesByRolKey("ROLE_ADMIN");
                } else {
                    ur = usuarioService.getRolesByRolKey("ROLE_COORDINADOR");
                }
                for (UsuarioRol item : ur) {
                    users.add(item.getUsuario());
                }
                break;
            default:
                showPnlDireccion = false;
                showPnlUser = false;
                break;

        }
    }

    private void chargeOptionals() {
        limpiarDatos();
        cRole = rolService.findById(roleId);
        List<UsuarioRol> ur;

        switch (cRole.getClaveRol()) {
            case "ROLE_COORDINADOR":
                showPnlDireccion = true;
                showPnlUser = false;
                paises = paisService.findAll();
                getZone();
                getCity();
                break;
            case "ROLE_SUPERVISOR":
            case "ROLE_VENDEDOR":

                showPnlDireccion = false;
                showPnlUser = true;
                users = new ArrayList();
                if (cRole.getClaveRol().equals("ROLE_SUPERVISOR")) {
                    ur = usuarioService.getRolesByRolKey("ROLE_COORDINADOR");
                } else {
                    ur = usuarioService.getRolesByRolKey("ROLE_SUPERVISOR");
                }

                for (UsuarioRol item : ur) {
                    users.add(item.getUsuario());
                }
                break;
            default:
                showPnlDireccion = false;
                showPnlUser = false;
                break;
        }
    }

    public void getZone() {
        if (current.getPaisId() != null) {
            cPais = paisService.getPaisByPaisId(current.getPaisId());
        }
    }

    public void getCity() {
        if (current.getZonaId() != null) {
            ciudades = paisService.getZonaById(current.getZonaId()).getCuidadList();
        }
    }

    public Pais getcPais() {
        return cPais;
    }

    public Rol getcRole() {
        return cRole;
    }

    public void limpiarDatos() {
        paises = null;
        cPais = null;
        ciudades = null;
        users = null;
    }

}
