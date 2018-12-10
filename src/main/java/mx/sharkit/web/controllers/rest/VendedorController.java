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
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.model.UsuarioRol;
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
public class VendedorController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    @PersistenceContext
    private EntityManager entityManager;
    long unixTime;

    @CrossOrigin
    @ApiOperation(value = "getVendedores", nickname = "getVendedores")
    @RequestMapping(method = RequestMethod.GET, path = "/getVendedores", produces = "application/json")
    public ObjectRequest getVendedores(Integer idSupervisor, String tipoRol) {
        unixTime = System.currentTimeMillis() / 1000L;
        ObjectRequest or = new ObjectRequest();
        DetachedCriteria dc = DetachedCriteria.forClass(UsuarioRol.class);

        if (idSupervisor == null || idSupervisor <= 0) {
            or.setTimeRequest(sdf.format(new Date()));
            or.setParameters(null);
            or.setDescription("Error idSupervisor es requerido");
            or.setDeviceId(null);
            or.setIdTransaction(String.valueOf(1));
            or.setStatus("I");
            return or;
        }
        if (tipoRol == null || tipoRol.length() <= 0) {
            or.setTimeRequest(sdf.format(new Date()));
            or.setParameters(null);
            or.setDescription("Error tipoRol es requerido");
            or.setDeviceId(null);
            or.setIdTransaction(String.valueOf(1));
            or.setStatus("I");
            return or;
        }
        try {

            //
            List<UsuarioRol> userRol = new ArrayList<>();
            dc.add(Restrictions.eq("rolId", new Long(4)));
            dc.createAlias("usuario", "usuario");

            //
            Session sess = (Session) entityManager.getDelegate();
            if (!tipoRol.equals("Admin")) {
                dc.add(Restrictions.eq("usuario.usuarioPadreId", new Long(idSupervisor)));
            } else {
                dc.add(Restrictions.isNotNull("usuario.usuarioPadreId"));
            }
            Criteria executableCriteria = dc.getExecutableCriteria(sess);
            List<UsuarioRol> companias = executableCriteria.list();
            List<Map<String, Object>> lista = new ArrayList<>();
            if (!companias.isEmpty()) {
                Map<String, Object> mapa2 = new LinkedHashMap<>();
                mapa2.put("id", null);
                mapa2.put("descripcion", "[--Vendedores--]");
                lista.add(mapa2);
                for (UsuarioRol c : companias) {
                    Map<String, Object> mapa = new LinkedHashMap<>();
                    mapa.put("id", c.getUsuario().getId());
                    mapa.put("descripcion", c.getUsuario().getNombre() + " " + c.getUsuario().getPaterno() + " " + c.getUsuario().getMaterno());
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
                or.setDescription("Sin vendedores");
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
