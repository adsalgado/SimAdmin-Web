/*
 * Copyright 2017 JoinFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.ChipDispersionSaldos;
import mx.sharkit.web.service.ChipDispersionSaldosService;
import mx.sharkit.web.service.ChipService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jlopez
 */
@Service
@Transactional
public class ChipDispersionSaldosServiceImpl extends BaseServiceImpl<ChipDispersionSaldos, Long> implements ChipDispersionSaldosService, Serializable {

    @Autowired
    ChipService chipService;

    @Override
    public Integer saveCargaDispersion(List<ChipDispersionSaldos> chips) throws Exception {
        int actualizados = 0;
        for (ChipDispersionSaldos chipDs : chips) {
            // Solo los montos <= 0
            if (chipDs.getMonto().intValue() <= 0) {
                String telefono = getNoTelefono(chipDs.getNoTelefono());
                System.out.println("tel: " + telefono);
                Chip chip = chipService.findByDn(telefono);
                if (chip != null) {
                    chipDs.setChipId(chip.getId());
                    chipDs.setNoTelefono(telefono);
                    save(chipDs);
                    actualizados++;
                }
            }
        }
        return actualizados;
    }

    private String getNoTelefono(String noTelefono) {
        String tel = null;
        if (!StringUtils.isBlank(noTelefono)) {
            StringTokenizer st = new StringTokenizer(noTelefono);
            while (st.hasMoreTokens()) {
                tel = st.nextToken();
            }
        }
        return tel;
    }

}
