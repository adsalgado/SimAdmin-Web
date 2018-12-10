package mx.sharkit.web.view.operacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.UsuarioRolService;
import mx.sharkit.web.service.UsuarioService;
import mx.sharkit.web.service.ChipHistoricoService;
import mx.sharkit.web.view.util.PFMessages;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author aalquisira
 */
@Setter
@Getter
@Named
@ViewScoped
public class AsignacionDistribuidorBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(AsignacionDistribuidorBean.class.getName());

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @Autowired
    private ChipService chipService;

    @Autowired
    private ChipHistoricoService chipHistoricoService;

    private List<Usuario> currents;
    private Usuario current;
    private Usuario selectedCurrent;

    private List<Chip> chips;
    private List<Chip> chipsAsignados;
    private Chip chip;
    private Chip selectedChip;

    private Long idDistribuidor;

    private SSUserDetails userDetails;

    private String serie;
    private String serieFinal;
    private String tipoAsignacion;

    private ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        current = new Usuario();
        search();
        obtenerChipsAsignados();
        obtenerChips();
    }

    public void search() {
        try {
            List<UsuarioRol> userRol;
            DetachedCriteria dc = DetachedCriteria.forClass(UsuarioRol.class);
            dc.add(Restrictions.eq("rolId", new Long(UsuarioRol.ID_ROL_COORDINADOR)));
            dc.createAlias("usuario", "usuario");
            dc.add(Restrictions.eq("usuario.activo", 1));
            userRol = usuarioRolService.findByCriteria(dc);
            currents = new ArrayList<>();
            for (UsuarioRol usuarioRol : userRol) {
                currents.add(usuarioRol.getUsuario());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCreateAsignacion() {
        try {
            if (StringUtils.equals("SSID", tipoAsignacion)) {
                Date fechaAsignacion = new Date();
                chip = chipService.findBySerie(serie);
                if (chip != null) {
                    if (Objects.equals(chip.getEstatusId(), Estatus.ID_ESTATUS_CHIP_NUEVO)) {
                        chip.setUsuarioCoordinadorId(idDistribuidor);
                        chip.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_DISTRIBUIDOR);
                        chip.setFechaEstatus(fechaAsignacion);
                        chip.setFechaUltModificacion(new Date());
                        //chipService.update(chip);
                        historicoEstatus = new ChipHistoricoEstatus();
                        historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_DISTRIBUIDOR);
                        historicoEstatus.setFechaEstatus(fechaAsignacion);
                        historicoEstatus.setObservaciones("Asignación a supervisor desde la WEB");
                        historicoEstatus.setUsuarioEstatus(userDetails.getUser().getUserName());
                        if (chipHistoricoService.saveChipAndHistory(chip, historicoEstatus)) {
//                            obtenerChipsAsignados();
//                            obtenerChips();
                            PFMessages.showMessageInfo("El chip con numero: " + serie + " ha sido asignado correctamente");
                            cleanSearch();
                        } else {
                            PFMessages.showMessageError("Error interno, si continua consulte a su administrador");
                            System.out.println("El chip ya fue asignado");
                        }

                    } else {
                        PFMessages.showMessageError("El chip con numero: " + serie + " ya esta asignado o fue vendido");
                        System.out.println("El chip ya fue asignado");
                    }
                } else {
                    PFMessages.showMessageError("El chip con numero: " + serie + " no ha sido registrado");
                    System.out.println("El chip no existe");
                }
            } else {
                Integer noActualizados = chipService.asignarChipsDistribuidorByRango(serie, serieFinal, 
                        idDistribuidor, userDetails.getUsername());
//                obtenerChipsAsignados();
//                obtenerChips();
                cleanSearch();
                PFMessages.showMessageInfo("Se asignaron: " + noActualizados + " chips.");

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error interno, si continua consulte a su administrador", e);
            PFMessages.showMessageError(e.getMessage());
        } 
    }

    public void obtenerChips() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            dc.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_ASIGNADO_DISTRIBUIDOR));
            dc.add(Restrictions.eq("usuarioCoordinadorId", idDistribuidor));
            dc.addOrder(Order.asc("serie"));
            chips = chipService.findByCriteria(dc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void obtenerChipsAsignados() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            dc.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_CHIP_NUEVO));
            dc.addOrder(Order.asc("serie"));
            chipsAsignados = chipService.findByCriteria(dc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String obtenerSuper(Chip asignaciones) {
        try {
            if (asignaciones.getUsuarioSupervisor() == null) {
                DetachedCriteria dc = DetachedCriteria.forClass(Usuario.class);
                dc.add(Restrictions.eq("id", asignaciones.getUsuarioSupervisorId()));
                List<Usuario> aSuper = usuarioService.findByCriteria(dc);
                return aSuper.get(0).getNombre() + " " + aSuper.get(0).getPaterno() + " " + aSuper.get(0).getMaterno();
            } else {
                return asignaciones.getUsuarioSupervisor().getNombre() + " " + asignaciones.getUsuarioSupervisor().getPaterno() + " " + asignaciones.getUsuarioSupervisor().getMaterno();
            }

        } catch (Exception e) {
            return null;
        }

    }

    public void cleanSearch() {
        //idDistribuidor = null;
        current = null;
        serie = null;
        serieFinal = null;
    }
}
