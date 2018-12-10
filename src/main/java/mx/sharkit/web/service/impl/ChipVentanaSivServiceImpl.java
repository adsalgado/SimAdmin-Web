package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.ChipResidual;
import mx.sharkit.web.model.ChipVentanaSiv;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.ChipVentanaSivService;
import mx.sharkit.web.view.util.TypeCast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado D.
 */
@Service
@Transactional
public class ChipVentanaSivServiceImpl extends BaseServiceImpl<ChipVentanaSiv, Long> implements ChipVentanaSivService, Serializable {

    @Autowired
    ChipService chipService;

    @Override
    public Integer saveVentanaSiv(List<Map<String, Object>> mpChips) throws Exception {
        int noActualizados = 0;
        for (Map<String, Object> mpChip : mpChips) {
            String serie = String.format("%sF", TypeCast.toString(mpChip.get("serie")));
            Chip chip = chipService.findBySerie(serie);
            if (chip != null) {
                ChipVentanaSiv cv = new ChipVentanaSiv();
                cv.setChipId(chip.getId());
                cv.setFecha((Date) mpChip.get("fecha"));
                cv.setDocumento(TypeCast.toString(mpChip.get("documento")));
                cv.setCliente(TypeCast.toString(mpChip.get("cliente")));
                cv.setArticulo(TypeCast.toString(mpChip.get("articulo")));
                cv.setDescripcion(TypeCast.toString(mpChip.get("descripcion")));
                cv.setCantidad(TypeCast.toInteger(mpChip.get("cantidad")));
                cv.setImporte(TypeCast.toBigDecimal(mpChip.get("importe")));
                cv.setTotal(TypeCast.toBigDecimal(mpChip.get("total")));
                cv.setSaldo(TypeCast.toBigDecimal(mpChip.get("saldo")));
                BigDecimal iiva = TypeCast.toBigDecimal(mpChip.get("importe_iva")) != null ? TypeCast.toBigDecimal(mpChip.get("importe_iva")) : BigDecimal.ZERO;
                cv.setImporteIva(iiva);
                save(cv);
                noActualizados++;
            }
        }
        return noActualizados;

    }

}
