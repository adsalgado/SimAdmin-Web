package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.ChipResidual;
import mx.sharkit.web.service.ChipResidualService;
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
public class ChipResidualServiceImpl extends BaseServiceImpl<ChipResidual, Long> implements ChipResidualService, Serializable {

    @Autowired
    ChipService chipService;
    
    @Autowired
    ChipResidualService chipResidualService;

    @Override
    public Integer saveResidual(List<Map<String, Object>> mpChips) throws Exception {
        int noActualizados = 0;
        for (Map<String, Object> mpChip : mpChips) {
            String serie = TypeCast.toString(mpChip.get("serie"));
            Chip chip = chipService.findBySerie(serie);
            if (chip != null) {
                ChipResidual cr = new ChipResidual();
                cr.setChipId(chip.getId());
                cr.setComisionResidual(TypeCast.toBigDecimal(mpChip.get("comision")));
                cr.setFechaActivacion((Date) mpChip.get("fecha_activacion"));
                cr.setFechaBaja((Date) mpChip.get("fecha_baja"));
                cr.setTelefono(TypeCast.toString(mpChip.get("telefono")));
                chipResidualService.save(cr);
                noActualizados++;
            }
        }
        return noActualizados;

    }

}
