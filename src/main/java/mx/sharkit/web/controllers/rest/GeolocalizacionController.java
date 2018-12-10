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
package mx.sharkit.web.controllers.rest;

import io.swagger.annotations.ApiOperation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.restObject.ObjectRequest;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jlopez
 */
@RestController
public class GeolocalizacionController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    @PersistenceContext
    private EntityManager entityManager;
    long unixTime;

    @CrossOrigin
    @ApiOperation(value = "coordenadas", nickname = "coordenadas")
    @RequestMapping(method = RequestMethod.GET, path = "/coordenadas", produces = "application/json")
    @ResponseBody
    public ObjectRequest coordenadas(Integer idChip) {

        ObjectRequest or = new ObjectRequest();
        try {
            if (idChip == null || idChip <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error idChip es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }

            Session sess = (Session) entityManager.getDelegate();
            DetachedCriteria crit = DetachedCriteria.forClass(Chip.class);
            crit.add(Restrictions.eq("id", new Long(idChip)));
            Map<String, Object> mapa = new LinkedHashMap<>();

            Criteria executableCriteria = crit.getExecutableCriteria(sess);
            List<Chip> chips = executableCriteria.list();
            if (chips != null && !chips.isEmpty()) {
                Chip c = chips.get(0);
                if (c.getLatitudVenta() != null && c.getLongitudVenta() != null && !"null".equals(c.getLatitudVenta()) && !"null".equals(c.getLongitudVenta())) {
                    mapa.put("latitud", c.getLatitudVenta());
                    mapa.put("longitud", c.getLongitudVenta());
                    or.setTimeRequest(sdf.format(new Date()));
                    or.setParameters(mapa);
                    or.setDescription("Exitoso");
                    or.setDeviceId(null);
                    or.setIdTransaction(String.valueOf(unixTime));
                    or.setStatus("A");
                } else {
                    or.setTimeRequest(sdf.format(new Date()));
                    or.setParameters(null);
                    or.setDescription("La portabilidad no cuenta con localización");
                    or.setDeviceId(null);
                    or.setIdTransaction(String.valueOf(unixTime));
                    or.setStatus("I");
                }
            } else {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("No se encontró portabilidad");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(unixTime));
                or.setStatus("I");
            }
        } catch (Exception e) {
            e.printStackTrace();
            or.setTimeRequest(sdf.format(new Date()));
            or.setParameters(new ArrayList<>());
            or.setDescription("Error " + e.getMessage());
            or.setDeviceId(null);
            or.setIdTransaction(String.valueOf(unixTime));
            or.setStatus("I");
        } finally {
            return or;
        }
    }
}
