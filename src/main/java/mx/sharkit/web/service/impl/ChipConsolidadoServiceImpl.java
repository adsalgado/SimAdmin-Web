package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.ChipConsolidado;
import mx.sharkit.web.model.ChipVentanaSiv;
import mx.sharkit.web.service.ChipConsolidadoService;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.view.util.TypeCast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado D.
 */
@Service
@Transactional
public class ChipConsolidadoServiceImpl extends BaseServiceImpl<ChipConsolidado, Long> implements ChipConsolidadoService, Serializable {

    @Autowired
    ChipService chipService;

    @Override
    public Integer saveChipConsolidado(List<Map<String, Object>> mpChips) throws Exception {
        int noActualizados = 0;
        for (Map<String, Object> mpChip : mpChips) {
            String serie = String.format("%s", TypeCast.toString(mpChip.get("serie")));
            Chip chip = chipService.findBySerie(serie);
            if (chip != null) {
                ChipConsolidado cv = new ChipConsolidado();
                cv.setChipId(chip.getId());
                cv.setFecha((Date) mpChip.get("fecha"));
                cv.setFactura(TypeCast.toString(mpChip.get("factura")));
                cv.setImporteFactura(TypeCast.toBigDecimal(mpChip.get("importe_factura")));
                cv.setImporteVenta(TypeCast.toBigDecimal(mpChip.get("importe_venta")));
                cv.setImporte(TypeCast.toBigDecimal(mpChip.get("importe")));
                cv.setTotal(TypeCast.toBigDecimal(mpChip.get("total")));
                cv.setNcredito(TypeCast.toString(mpChip.get("ncredito")));
                cv.setMotivo(TypeCast.toString(mpChip.get("motivo")));
                cv.setLlave(TypeCast.toString(mpChip.get("llave")));
                save(cv);
                noActualizados++;
            }
        }
        return noActualizados;

    }

}
