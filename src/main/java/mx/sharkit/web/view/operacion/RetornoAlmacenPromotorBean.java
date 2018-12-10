package mx.sharkit.web.view.operacion;

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
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.ChipHistoricoEstatus;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.model.UsuarioRol;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.ChipHistoricoService;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.UsuarioRolService;
import mx.sharkit.web.service.UsuarioService;
import mx.sharkit.web.view.util.PFMessages;
import mx.sharkit.web.view.util.TypeCast;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author aalquisira
 */
@Setter
@Getter
@Named
@ViewScoped
public class RetornoAlmacenPromotorBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(RetornoAlmacenPromotorBean.class.getName());

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @Autowired
    private ChipService chipService;

    @Autowired
    private ChipHistoricoService chipHistoricoService;

    private List<Usuario> distribuidores;
    private List<Usuario> subdistribuidores;
    private List<Usuario> promotores;

    private List<Chip> chips;
    private List<Chip> chipsAsignados;
    private Map<String, Boolean> mpChipsRegreso;
    List<String> lstChipsRegreso;
    private Chip chip;
    private Chip selectedChip;

    private Long idSupervisor;
    private Long idDistribuidor;
    private Long idVendedor;

    private SSUserDetails userDetails;

    private String serie;
    private String serieFinal;
    private String tipoAsignacion;
    private Boolean distribuidorDisabled;
    private Boolean subdistribuidorDisabled;
    private Boolean promotorDisabled;

    private ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        distribuidorDisabled = false;
        subdistribuidorDisabled = false;
        lstChipsRegreso = new ArrayList<>();

        if (hasRole("ROLE_COORDINADOR")) {
            idDistribuidor = userDetails.getUser().getId();
            distribuidorDisabled = true;
            onDistribuidorChange();
        } else if (hasRole("ROLE_SUPERVISOR")) {
            Usuario usSubdistribuidor = userDetails.getUser();
            Usuario usDistribuidor = usSubdistribuidor.getUsuarioPadre();

            if (usDistribuidor != null) {
                idDistribuidor = usDistribuidor.getId();
                distribuidorDisabled = true;
                onDistribuidorChange();

                idSupervisor = usSubdistribuidor.getId();
                subdistribuidorDisabled = true;
                onSubDistribuidorChange();
            }

        } else if (hasRole("ROLE_VENDEDOR")) {
            Usuario usPromotor = userDetails.getUser();
            Usuario usSubdistribuidor = usPromotor.getUsuarioPadre();
            Usuario usDistribuidor = usSubdistribuidor.getUsuarioPadre();

            if (usDistribuidor != null) {
                idDistribuidor = usDistribuidor.getId();
                distribuidorDisabled = true;
                onDistribuidorChange();

                idSupervisor = usSubdistribuidor.getId();
                subdistribuidorDisabled = true;
                onSubDistribuidorChange();

                idVendedor = usPromotor.getId();
                promotorDisabled = true;
            }

            mpChipsRegreso = new HashMap<>();
            chipsAsignados = obtenerChipsAsignadosPromotor();
            System.out.println("chipsAsignados: " + chipsAsignados.size());
            for (Chip cha : chipsAsignados) {
                mpChipsRegreso.put(cha.getSerie(), false);
            }

        }

//        obtenerChips();
    }

    public void search() {
        distribuidores = usuarioService.findByRolIdOrderByNombre(TypeCast.toLong(UsuarioRol.ID_ROL_COORDINADOR));
    }

    public void onCreateAsignacion() {
        if (StringUtils.equals("SSID", tipoAsignacion)) {
            if (mpChipsRegreso.containsKey(serie)) {
                if (!mpChipsRegreso.get(serie)) {
                    lstChipsRegreso.add(serie);
                    mpChipsRegreso.put(serie, true);
                }
            } else {
                PFMessages.showMessageError("El SSID no está asignado al promotor.");
            }
            serie = null;
        } else {
            List<Chip> chipsTmp = obtenerChipsAsignadosPromotorByRango(serie, serieFinal);
            for (Chip chip1 : chipsTmp) {
                if (mpChipsRegreso.containsKey(chip1.getSerie())) {
                    if (!mpChipsRegreso.get(chip1.getSerie())) {
                        lstChipsRegreso.add(chip1.getSerie());
                        mpChipsRegreso.put(chip1.getSerie(), true);
                    }
                }
            }
        }

    }

    public void regresarChips() {
        try {
            if (hasChipsPorRetornar()) {
                PFMessages.showMessageErrorBackEnd("Tiene chips pendientes por regresar. Dirigite a mesa de control");
                return;
            }
            
            Integer noActualizados = chipService.retornoChipsAlmacenPromotor(lstChipsRegreso,
                    idDistribuidor, idSupervisor, idVendedor, userDetails.getUsername());
//            obtenerChips();
            cleanSearch();
            PFMessages.showMessageInfo("Se regresaron: " + noActualizados + " chips.");
            
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error interno, si continua consulte a su administrador", ex);
            PFMessages.showMessageError(ex.getMessage());
        }
    }

    public void obtenerChips() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            dc.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_ASIGNADO_VENDEDOR));
            dc.add(Restrictions.eq("usuarioCoordinadorId", idDistribuidor));
            dc.add(Restrictions.eq("usuarioSupervisorId", idSupervisor));
            dc.add(Restrictions.eq("usuarioVendedorId", idVendedor));
            dc.addOrder(Order.asc("serie"));
            chips = chipService.findByCriteria(dc);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            PFMessages.showMessageError(e.getMessage());
        }
    }

    public List<Chip> obtenerChipsAsignadosPromotor() {

        DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
        dc.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_ASIGNADO_VENDEDOR));
        dc.add(Restrictions.eq("usuarioVendedorId", idVendedor));
        dc.addOrder(Order.asc("serie"));
        return chipService.findByCriteria(dc);

    }

    public List<Chip> obtenerChipsAsignadosPromotorByRango(String serieInicial, String serieFinal) {

        DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
        dc.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_ASIGNADO_VENDEDOR));
        dc.add(Restrictions.eq("usuarioVendedorId", idVendedor));
        dc.add(Restrictions.between("serie", serieInicial, serieFinal));
        dc.addOrder(Order.asc("serie"));
        return chipService.findByCriteria(dc);

    }

    public void onDistribuidorChange() {
        if (idDistribuidor != null || idDistribuidor != 0) {
            subdistribuidores = usuarioService.findByRolIdAndPadreIdOrderByNombre(TypeCast.toLong(UsuarioRol.ID_ROL_SUPERVISOR), idDistribuidor);
        }
    }

    public void onSubDistribuidorChange() {
        if (idSupervisor != null || idSupervisor != 0) {
            promotores = usuarioService.findByRolIdAndPadreIdOrderByNombre(TypeCast.toLong(UsuarioRol.ID_ROL_VENDEDOR), idSupervisor);
        }
    }

    public void cleanSearch() {
        //idSupervisor = null;
        serie = null;
        serieFinal = null;
        if (!lstChipsRegreso.isEmpty()) {
            lstChipsRegreso.clear();
        }
    }

    private boolean hasChipsPorRetornar() {
        for (Map.Entry<String, Boolean> entry : mpChipsRegreso.entrySet()) {
            String key = entry.getKey();
            Boolean value = entry.getValue();
            if (!value) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasRole(String role) {
        // get security context from thread local
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return false;
        }

        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return false;
        }

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (role.equals(auth.getAuthority())) {
                return true;
            }
        }

        return false;
    }

}
