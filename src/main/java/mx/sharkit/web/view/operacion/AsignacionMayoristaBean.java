package mx.sharkit.web.view.operacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class AsignacionMayoristaBean implements Serializable {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @Autowired
    private ChipService chipService;

    @Autowired
    private ChipHistoricoService chipHistoricoService;

    private ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();

    private List<Usuario> currents;
    private Usuario current;
    private Usuario selectedCurrent;

    private List<Chip> chips;
    private List<Chip> chipsAsignados;
    private Chip chip;
    private Chip selectedChip;

    private Long idMayorista;

    private SSUserDetails userDetails;

    private String serie;
    private String dnTemporal;
    private BigDecimal recarga;
    private Integer folio;

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
            List<UsuarioRol> userRol = new ArrayList<>();
            DetachedCriteria dc = DetachedCriteria.forClass(UsuarioRol.class);
            dc.add(Restrictions.eq("rolId", new Long(UsuarioRol.ID_ROL_MAYORISTA)));
            dc.createAlias("usuario", "usuario");
            dc.add(Restrictions.eq("usuario.activo", 1));

//            dc.add(Restrictions.eq("usuario.usuarioPadreId", userDetails.getUser().getId()));
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
            Date fechaAsignacion = new Date();
            chip = chipService.findBySerie(serie);
            if (chip != null) {
                if (chip.getEstatusId() == Estatus.ID_ESTATUS_CHIP_NUEVO) {
                    chip.setUsuarioCoordinadorId(userDetails.getUser().getId());
                    chip.setUsuarioMayoristaId(idMayorista);
                    chip.setFechaAsignacionMayorista(fechaAsignacion);
                    chip.setDnTemporal(dnTemporal);
                    chip.setFolio(folio);
//                    chip.setMontoRecarga(recarga);
                    chip.setFechaRecarga(fechaAsignacion);
                    chip.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_MAYORISTA);
                    chip.setFechaEstatus(fechaAsignacion);
                    chip.setFechaUltModificacion(new Date());
                    //chipService.update(chip);
                    historicoEstatus = new ChipHistoricoEstatus();
                    historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_MAYORISTA);
                    historicoEstatus.setFechaEstatus(fechaAsignacion);
                    historicoEstatus.setObservaciones("Asignación a mayorista desde la WEB");
                    historicoEstatus.setUsuarioEstatus(userDetails.getUser().getUserName());
                    if (chipHistoricoService.saveChipAndHistory(chip, historicoEstatus)) {
                        obtenerChipsAsignados();
                        obtenerChips();
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public void obtenerChips() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            dc.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_ASIGNADO_MAYORISTA));
            dc.add(Restrictions.eq("usuarioCoordinadorId", userDetails.getUser().getId()));
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

    public String obtenerMayorista(Chip asignaciones) {
        try {
            if (asignaciones.getUsuarioMayorista() == null) {
                DetachedCriteria dc = DetachedCriteria.forClass(Usuario.class);
                dc.add(Restrictions.eq("id", asignaciones.getUsuarioMayoristaId()));
                List<Usuario> aMayor = usuarioService.findByCriteria(dc);
                return aMayor.get(0).getNombre() + " " + aMayor.get(0).getPaterno() + " " + aMayor.get(0).getMaterno();
            } else {
                return asignaciones.getUsuarioMayorista().getNombre() + " " + asignaciones.getUsuarioMayorista().getPaterno() + " " + asignaciones.getUsuarioMayorista().getMaterno();
            }
        } catch (Exception e) {
            return null;
        }

    }

    public void cleanSearch() {
        //idMayorista = null;
        current = null;
        serie = null;
        dnTemporal = null;
        recarga = null;
    }
}
