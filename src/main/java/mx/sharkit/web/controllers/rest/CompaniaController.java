/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.sharkit.web.controllers.rest;

import mx.sharkit.web.restObject.ObjectRequest;
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
public class CompaniaController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    @PersistenceContext
    private EntityManager entityManager;
    long unixTime;

    @CrossOrigin
    @ApiOperation(value = "getCompanias", nickname = "getCompanias")
    @RequestMapping(method = RequestMethod.GET, path = "/getCompanias", produces = "application/json")
    public ObjectRequest getCompanias() {
        unixTime = System.currentTimeMillis() / 1000L;
        ObjectRequest or = new ObjectRequest();
        DetachedCriteria dc = DetachedCriteria.forClass(Compania.class);
        try {
            Session sess = (Session) entityManager.getDelegate();
            Criteria executableCriteria = dc.getExecutableCriteria(sess);
            List<Compania> companias = executableCriteria.list();
            if (!companias.isEmpty()) {
                List<Map<String, Object>> lista = new ArrayList<>();
                Map<String, Object> mapa2 = new LinkedHashMap<>();
                mapa2.put("id", null);
                mapa2.put("descripcion", "[--Compañías--]");
                lista.add(mapa2);
                for (Compania c : companias) {
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
                or.setDescription("Sin compañías");
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
