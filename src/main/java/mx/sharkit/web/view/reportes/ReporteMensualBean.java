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
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.DynScriptService;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
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
public class ReporteMensualBean implements Serializable {

    @Autowired
    private DynScriptService dynScriptService;

    private List<Usuario> currents;
    private Usuario current;
    private Usuario selectedCurrent;

    private SSUserDetails userDetails;

    List<Map<String, Object>> registros;

    private Date fecha = new Date();

    private Integer mesName = null;
    
    private List<Boolean> list;

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        current = new Usuario();
        list = Arrays.asList(true, true, true, true, true,true, true, true, true, true,true, true, true, true, true,true, true, true, true, true,true, true, true, true, true,true, true, true, true, true,true, true, true, true, true,true, true, true, true, true,true, true, true, true, true, true, true, true, true);
//        obtenerChips();
        search();
    }

    public void search() {
        /*try {
            Map<String, Object> params = new HashMap<>();
            if (fecha != null) {
                String mes = new SimpleDateFormat("MM").format(fecha);
                String anio = new SimpleDateFormat("yyyy").format(fecha);
                params.put("mes", mes);
                params.put("anio", anio);
            }
            registros = dynScriptService.getRegistros("chips", "reporte_mensual", params);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        obtenerChips();
    }

    public void obtenerChips() {
        try {
            Map<String, Object> params = new HashMap<>();
            if (fecha != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(fecha);
                mesName = cal.get(Calendar.MONTH) + 1;
                System.out.println("mesname" + mesName);
                String mes = new SimpleDateFormat("MM").format(fecha);
                String anio = new SimpleDateFormat("yyyy").format(fecha);
                params.put("mes", mes);
                params.put("anio", anio);
            }
            registros = dynScriptService.getRegistros("chips", "reporte_mensual", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String obtenerMes(Integer mes2) {
        try {
            int mes = mesName - mes2;
            if (mes <= 0) {
                mes = mes + 12;
                System.out.println("mes" + mes);
            }
            return mes == 1 ? "Enero" : mes == 2 ? "Febrero" : mes == 3 ? "Marzo" : mes == 4 ? "Abril" : mes == 5 ? "Mayo" : mes == 6 ? "Junio" : mes == 7 ? "Julio" : mes == 8 ? "Agosto" : mes == 9 ? "Septiembre" : mes == 10 ? "Octubre" : mes == 11 ? "Noviembre" : "Diciembre";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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
