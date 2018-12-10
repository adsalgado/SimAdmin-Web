package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.ChipPortabilidad;
import mx.sharkit.web.service.ChipPortabilidadService;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.view.util.TypeCast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jlopez
 */
@Service
@Transactional
public class ChipPortabilidadServiceImpl extends BaseServiceImpl<ChipPortabilidad, Long> implements ChipPortabilidadService, Serializable {

    @Autowired
    ChipService chipService;
    
    @Autowired
    ChipPortabilidadService chipPortabilidadService;

    @Override
    public Integer savePortabilidad(List<Map<String, Object>> mpChips) throws Exception {
        int noActualizados = 0;
        for (Map<String, Object> mpChip : mpChips) {
            String serie = TypeCast.toString(mpChip.get("serie"));
            Chip chip = chipService.findBySerie(serie);
            if (chip != null) {
                ChipPortabilidad cp = new ChipPortabilidad();
                cp.setChipId(chip.getId());
                cp.setCarrusel(TypeCast.toString(mpChip.get("carrusel")));
                cp.setComision(TypeCast.toBigDecimal(mpChip.get("comision")));
                cp.setFecha((Date) mpChip.get("fecha"));
                cp.setFechaBaja((Date) mpChip.get("fecha_baja"));
                cp.setTelefono(TypeCast.toString(mpChip.get("telefono")));
                chipPortabilidadService.save(cp);
                noActualizados++;
            }
        }
        return noActualizados;
    }

}
