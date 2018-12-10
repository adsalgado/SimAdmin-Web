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
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.ChipHistoricoEstatus;
import mx.sharkit.web.repository.ChipHistoricoRepository;
import mx.sharkit.web.repository.ChipRepository;
import mx.sharkit.web.service.ChipHistoricoService;
import mx.sharkit.web.service.ChipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jlopez
 */
@Service
@Transactional
public class ChipHistoricoServiceImpl extends BaseServiceImpl<ChipHistoricoEstatus, Integer> implements ChipHistoricoService, Serializable {

    @Autowired
    private ChipService chipService;
    @Autowired
    private ChipHistoricoRepository chipHistoricoRepository;
    
    @Override
    public boolean saveChipAndHistory(Chip c, ChipHistoricoEstatus che) {
        try {
            System.out.println(c);
            chipService.update(c);
            che.setSerieId(c.getId());
            save(che);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
