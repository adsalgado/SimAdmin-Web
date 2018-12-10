package mx.sharkit.web.view.reportes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import mx.sharkit.web.service.DynScriptService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author aalquisira
 */
@Getter
@Setter
@Named
@ViewScoped
public class ReporteFinalBean implements Serializable {

    @Autowired
    private DynScriptService dynScriptService;

    private List<Usuario> currents;
    private Usuario current;
    private Usuario selectedCurrent;

    private List<Chip> chips;
    private Chip chip;
    private Chip selectedChip;

    private SSUserDetails userDetails;

    List<Map<String, Object>> registros;

    private SimpleDateFormat formatFechaBasica = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Date fecha;
    
    private List<Boolean> list;

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        current = new Usuario();
        list = Arrays.asList(true, true, true, true, true,true, true, true, true, true,true, true, true, true, true,true, true, true, true, true,true, true, true, true, true, true);
//        obtenerChips();
        search();
    }

    public void search() {
        try {
            Map<String, Object> params = new HashMap<>();
            registros = dynScriptService.getRegistros("chips", "reporte_final", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void obtenerChips() {
        try {
            Map<String, Object> params = new HashMap<>();
            if (fecha != null) {
                String fechaIni = formatFechaBasica.format(fecha) + " 00:00:00";
                String fechaFin = formatFechaBasica.format(fecha) + " 23:59:59";
                //
                Calendar c = Calendar.getInstance();
                c.setTime(formatFecha.parse(fechaFin));
//                c.getActualMaximum(Calendar.DAY_OF_MONTH);
                int lastDate = c.getActualMaximum(Calendar.DATE);
                c.set(Calendar.DATE, lastDate);
                String fechaFin2 = formatFecha.format(c.getTime());
                //
                params.put("fecha", fechaIni);
                params.put("fechaFin", fechaFin2);
            }
            registros = dynScriptService.getRegistros("chips", "reporte_final", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Boolean> getList() {
        return list;
        
    }

    public void setList(List<Boolean> list) {
        this.list = list;
    }

    public void onToggle(ToggleEvent e) {
        list.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }
}
