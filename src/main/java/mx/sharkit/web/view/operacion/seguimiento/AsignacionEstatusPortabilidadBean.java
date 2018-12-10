package mx.sharkit.web.view.operacion.seguimiento;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.UsuarioRolService;
import mx.sharkit.web.service.UsuarioService;
import mx.sharkit.web.view.util.PFMessages;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.RowEditEvent;
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
public class AsignacionEstatusPortabilidadBean implements Serializable {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @Autowired
    private ChipService chipService;

    private List<Estatus> estatus;

    private List<Usuario> currents;
    private Usuario current;
    private Usuario selectedCurrent;

    private List<Chip> chips;
    private Chip chip;
    private Chip selectedChip;

    private SSUserDetails userDetails;

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        current = new Usuario();
        search();
        DetachedCriteria dc = DetachedCriteria.forClass(Estatus.class);
        dc.add(Restrictions.eq("tipoEstatusId", Estatus.TIPO_ESTATUS_PORTABILIDAD));
        estatus = chipService.findByCriteria(dc);
    }

    public void search() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            
            //dc.add(Restrictions.ne("estatusPortabilidadId", Estatus.ID_ESTATUS_PORTABILIDAD_EXITOSA));
            //dc.add(Restrictions.isNull("estatusPortabilidadId"));
            dc.add(Restrictions.or(
                    Restrictions.ne("estatusPortabilidadId", Estatus.ID_ESTATUS_PORTABILIDAD_EXITOSA),
                    Restrictions.isNull("estatusPortabilidadId")));
            dc.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_DN_TEMPORAL));
            chips = chipService.findByCriteria(dc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            Date fechaEstatusPortabilidad = new Date();
            Chip chipTemporal = (Chip) event.getObject();
            chipTemporal.setFechaEstPortabilidad(fechaEstatusPortabilidad);
            if (chipTemporal.getEstatusPortabilidad() != null) {
                chipTemporal.setEstatusPortabilidadId(chipTemporal.getEstatusPortabilidad().getId());
                
                if (Objects.equals(chipTemporal.getEstatusPortabilidadId(), Estatus.ID_ESTATUS_PORTABILIDAD_EXITOSA)) {
                    chips.remove(chipTemporal);
                }
            }
            chipService.update(chipTemporal);
            PFMessages.showMessageInfo("Estatus de portabilidad exitosa");
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edición cancelada", ((Chip) event.getObject()).getDn());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
