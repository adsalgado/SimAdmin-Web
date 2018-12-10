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
import mx.sharkit.web.model.Compania;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.restObject.ObjectRequest;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jlopez
 */
@RestController
public class EstatusController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    @PersistenceContext
    private EntityManager entityManager;
    long unixTime;

    @CrossOrigin
    @ApiOperation(value = "getEstatus", nickname = "getEstatus")
    @RequestMapping(method = RequestMethod.GET, path = "/getEstatus", produces = "application/json")
    public ObjectRequest getEstatus() {
        unixTime = System.currentTimeMillis() / 1000L;
        ObjectRequest or = new ObjectRequest();
        DetachedCriteria dc = DetachedCriteria.forClass(Estatus.class);
        try {
            Session sess = (Session) entityManager.getDelegate();
            dc.add(Restrictions.eq("tipoEstatusId",1));
            Criteria executableCriteria = dc.getExecutableCriteria(sess);
            List<Estatus> companias = executableCriteria.list();
            if (!companias.isEmpty()) {
                List<Map<String, Object>> lista = new ArrayList<>();
                Map<String, Object> mapa2 = new LinkedHashMap<>();
                mapa2.put("id", null);
                mapa2.put("descripcion", "[--Estatus--]");
                lista.add(mapa2);
                for (Estatus c : companias) {
                    Map<String, Object> mapa = new LinkedHashMap<>();
                    mapa.put("id", c.getId());
                    mapa.put("descripcion", c.getNombre());
                    lista.add(mapa);
                }
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(lista);
                or.setDescription("Exitoso");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(unixTime));
                or.setStatus("A");
            } else {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Sin Estatus");
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
    
    
    @CrossOrigin
    @ApiOperation(value = "getEstatusPortabilidad", nickname = "getEstatusPortabilidad")
    @RequestMapping(method = RequestMethod.GET, path = "/getEstatusPortabilidad", produces = "application/json")
    public ObjectRequest getEstatusPortabilidad() {
        unixTime = System.currentTimeMillis() / 1000L;
        ObjectRequest or = new ObjectRequest();
        DetachedCriteria dc = DetachedCriteria.forClass(Estatus.class);
        try {
            Session sess = (Session) entityManager.getDelegate();
            dc.add(Restrictions.eq("tipoEstatusId",2));
            Criteria executableCriteria = dc.getExecutableCriteria(sess);
            List<Estatus> companias = executableCriteria.list();
            if (!companias.isEmpty()) {
                List<Map<String, Object>> lista = new ArrayList<>();
                Map<String, Object> mapa2 = new LinkedHashMap<>();
                mapa2.put("id", null);
                mapa2.put("descripcion", "[--Estatus de Portabilidad--]");
                lista.add(mapa2);
                for (Estatus c : companias) {
                    Map<String, Object> mapa = new LinkedHashMap<>();
                    mapa.put("id", c.getId());
                    mapa.put("descripcion", c.getNombre());
                    lista.add(mapa);
                }
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(lista);
                or.setDescription("Exitoso");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(unixTime));
                or.setStatus("A");
            } else {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Sin Estatus");
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
    
    @CrossOrigin
    @ApiOperation(value = "getEstatusProceso", nickname = "getEstatusProceso")
    @RequestMapping(method = RequestMethod.GET, path = "/getEstatusProceso", produces = "application/json")
    public ObjectRequest getEstatusProceso() {
        unixTime = System.currentTimeMillis() / 1000L;
        ObjectRequest or = new ObjectRequest();
        DetachedCriteria dc = DetachedCriteria.forClass(Estatus.class);
        try {
            Session sess = (Session) entityManager.getDelegate();
            dc.add(Restrictions.eq("tipoEstatusId",3));
            Criteria executableCriteria = dc.getExecutableCriteria(sess);
            List<Estatus> companias = executableCriteria.list();
            if (!companias.isEmpty()) {
                List<Map<String, Object>> lista = new ArrayList<>();
                Map<String, Object> mapa2 = new LinkedHashMap<>();
                mapa2.put("id", null);
                mapa2.put("descripcion", "[--Estatus de Proceso--]");
                lista.add(mapa2);
                for (Estatus c : companias) {
                    Map<String, Object> mapa = new LinkedHashMap<>();
                    mapa.put("id", c.getId());
                    mapa.put("descripcion", c.getNombre());
                    lista.add(mapa);
                }
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(lista);
                or.setDescription("Exitoso");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(unixTime));
                or.setStatus("A");
            } else {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Sin Estatus");
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
