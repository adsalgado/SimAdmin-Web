package mx.sharkit.web.view.reportes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.UsuarioService;
import org.hibernate.criterion.DetachedCriteria;
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
public class ReporteErroresBean implements Serializable{
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ChipService chipService;
    
    private List<Usuario> currents;
    private Usuario current;
    private Usuario selectedCurrent;

    private List<Chip> chips;
    private Chip chip;
    private Chip selectedChip;
    
    private SSUserDetails userDetails;
    
    private Integer estatusPo;
    private List<Estatus> estatusPortabilidad;
    private Date fechaEstatusPortabilidad;
    
    private SimpleDateFormat formatFechaBasica = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        current = new Usuario();
        obtenerEstatusPortabilidad();
        search();
    }
    
    public void search() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            dc.add(Restrictions.ne("estatusPortabilidadId", Estatus.ID_ESTATUS_PORTABILIDAD_EXITOSA));
            chips = chipService.findByCriteria(dc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void obtenerChips() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            if (estatusPo != null && estatusPo > 0) {
                dc.add(Restrictions.eq("estatusPortabilidadId", estatusPo));
            }else{
                dc.add(Restrictions.ne("estatusPortabilidadId", Estatus.ID_ESTATUS_PORTABILIDAD_EXITOSA));
            }
            if (fechaEstatusPortabilidad != null) {
                String fPortabilidad = formatFechaBasica.format(fechaEstatusPortabilidad) + " 00:00:00";
                String fPortabilidadF = formatFechaBasica.format(fechaEstatusPortabilidad) + " 23:59:59";
                dc.add(Restrictions.between("fechaEstPortabilidad", formatFecha.parse(fPortabilidad), formatFecha.parse(fPortabilidadF)));
            }
            
            chips = chipService.findByCriteria(dc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void obtenerEstatusPortabilidad() {
        DetachedCriteria dc = DetachedCriteria.forClass(Estatus.class);
        dc.add(Restrictions.eq("tipoEstatusId", Estatus.TIPO_ESTATUS_PORTABILIDAD));
        dc.add(Restrictions.ne("id", Estatus.ID_ESTATUS_PORTABILIDAD_EXITOSA));        
        estatusPortabilidad = chipService.findByCriteria(dc);
    }
}
