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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.ChipHistoricoEstatus;
import mx.sharkit.web.restObject.ObjectRequest;
import mx.sharkit.web.service.ChipHistoricoService;
import mx.sharkit.web.view.util.TypeCast;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AltaController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    @PersistenceContext
    private EntityManager entityManager;
    long unixTime;

    @Autowired
    private ChipHistoricoService chipHistoricoService;

    @CrossOrigin
    @ApiOperation(value = "alta", nickname = "alta")
    @RequestMapping(method = RequestMethod.POST, path = "/alta", produces = "application/json")
    @ResponseBody
    public ObjectRequest alta(@RequestBody Map body) {

        Map<String, Object> mapBody = (Map<String, Object>) body.get("params");
        System.out.println(mapBody);
        ObjectRequest or = new ObjectRequest();
        try {
            String nombre = null, iccid = null, nip = null, apellido = null, user = null;
            Integer idVendedor = null, idCompania = null, idProducto = null, idTipo = null;
            Integer dn = null;
            Long dnLong = null;
            Double latitud = null, longitud = null, costo = null;
            for (Map.Entry<String, Object> entry : mapBody.entrySet()) {
                switch (entry.getKey()) {
                    case "nombre":
                        nombre = (String) entry.getValue();
                        break;
                    case "dn":
                        if (entry.getValue() instanceof Integer) {
                            System.out.println("integer");
                            dn = (Integer) entry.getValue();
                        } else {
                            System.out.println("long");
                            dnLong = (Long) entry.getValue();
                        }
                        break;
                    case "iccid":
                        iccid = (String) entry.getValue();
                        break;
                    case "nip":
                        nip = (String) entry.getValue();
                        break;
                    case "apellido":
                        apellido = (String) entry.getValue();
                        break;
                    case "user":
                        user = (String) entry.getValue();
                        break;
                    case "latitud":
                        latitud = (Double) entry.getValue();
                        break;
                    case "longitud":
                        longitud = (Double) entry.getValue();
                        System.out.println("-------");
                        break;
                    case "idcompania":
                        idCompania = (Integer) entry.getValue();
                        break;
                    case "idVendedor":
                        idVendedor = (Integer) entry.getValue();
                        break;
                    case "idProducto":
                        idProducto = (Integer) entry.getValue();
                        break;
                    case "idTipo":
                        idTipo = (Integer) entry.getValue();
                        break;
                    case "costo":
                        costo = TypeCast.toDouble(entry.getValue());
                        break;
                }
            }
            if (nombre == null || nombre.length() <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error nombre es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }
            if (user == null || user.length() <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error user es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }
            if (apellido == null || apellido.length() <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error apellido es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }

            if (idTipo != null) {
                if (idTipo == 2) {
                    if (dn == null || dn <= 0) {
                        if (dnLong == null || dnLong <= 0) {
                            or.setTimeRequest(sdf.format(new Date()));
                            or.setParameters(null);
                            or.setDescription("Error dn es requerido");
                            or.setDeviceId(null);
                            or.setIdTransaction(String.valueOf(1));
                            or.setStatus("I");
                            return or;
                        }
                    }
                    if (dnLong == null || dnLong <= 0) {
                        if (dn == null || dn <= 0) {
                            or.setTimeRequest(sdf.format(new Date()));
                            or.setParameters(null);
                            or.setDescription("Error dn es requerido");
                            or.setDeviceId(null);
                            or.setIdTransaction(String.valueOf(1));
                            or.setStatus("I");
                            return or;
                        }
                    }

                    if (nip == null || nip.length() <= 0) {
                        or.setTimeRequest(sdf.format(new Date()));
                        or.setParameters(null);
                        or.setDescription("Error nip es requerido");
                        or.setDeviceId(null);
                        or.setIdTransaction(String.valueOf(1));
                        or.setStatus("I");
                        return or;
                    } else if (nip.length() > 4 || nip.length() < 4) {
                        or.setTimeRequest(sdf.format(new Date()));
                        or.setParameters(null);
                        or.setDescription("Error nip debe contener 4 caracteres");
                        or.setDeviceId(null);
                        or.setIdTransaction(String.valueOf(1));
                        or.setStatus("I");
                        return or;
                    } else if (nip.equals("undefined")) {
                        or.setTimeRequest(sdf.format(new Date()));
                        or.setParameters(null);
                        or.setDescription("Verifique que todos los campos del NIP se encuentren llenos");
                        or.setDeviceId(null);
                        or.setIdTransaction(String.valueOf(1));
                        or.setStatus("I");
                        return or;
                    }
                }
            }

            if (iccid == null || iccid.length() <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error iccid es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }

            if (idVendedor == null || idVendedor <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error vendedor es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }
            if (idCompania == null || idCompania <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error compañía es requerida");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }
            Session sess = (Session) entityManager.getDelegate();
            DetachedCriteria crit = DetachedCriteria.forClass(Chip.class);
            crit.add(Restrictions.eq("serie", iccid));
            Criteria executableCriteria = crit.getExecutableCriteria(sess);
            List<Chip> chips = executableCriteria.list();
            if (chips != null && !chips.isEmpty()) {
                Chip chip = chips.get(0);
                if (Objects.equals(chip.getUsuarioVendedorId(), new Long(idVendedor))) {
                    if (chip.getFechaVenta() == null) {
                        chip.setNip(nip);
                        if (dn != null) {
                            chip.setDn(String.valueOf(dn));
                        } else {
                            chip.setDn(String.valueOf(dnLong));
                        }
                        chip.setCompaniaId(idCompania);
                        chip.setNombreCliente((nombre + " " + apellido).toUpperCase());
                        chip.setFechaVenta(new Date());
                        chip.setEstatusId(4);
                        chip.setLatitudVenta(String.valueOf(latitud));
                        chip.setLongitudVenta(String.valueOf(longitud));
                        chip.setFechaUltModificacion(new Date());

                        chip.setTipoVentaId(idTipo);
                        chip.setProductoId(idProducto);
//                        chip.setCosto(TypeCast.toBigDecimal(costo));
                        chip.setCosto(new BigDecimal(costo));

//                        sess.flush();
//                        sess.persist(chip);
                        ChipHistoricoEstatus che = new ChipHistoricoEstatus();
                        che.setEstatusId(4);
                        che.setFechaEstatus(new Date());
                        che.setObservaciones("Actualizado desde app movil");
//                        che.setSerie(chip);
                        che.setUsuarioEstatus(user);

                        if (chipHistoricoService.saveChipAndHistory(chip, che)) {
                            or.setTimeRequest(sdf.format(new Date()));
                            or.setParameters(null);
                            or.setDescription("Exitoso");
                            or.setDeviceId(null);
                            or.setIdTransaction(String.valueOf(unixTime));
                            or.setStatus("A");
                        } else {
                            or.setTimeRequest(sdf.format(new Date()));
                            or.setParameters(null);
                            or.setDescription("Error en el servidor intente de nuevo");
                            or.setDeviceId(null);
                            or.setIdTransaction(String.valueOf(unixTime));
                            or.setStatus("I");
                        }
                    } else {
                        or.setTimeRequest(sdf.format(new Date()));
                        or.setParameters(null);
                        or.setDescription("El iccid " + iccid + " ya ha sido vendido");
                        or.setDeviceId(null);
                        or.setIdTransaction(String.valueOf(unixTime));
                        or.setStatus("I");
                    }
                } else {
                    or.setTimeRequest(sdf.format(new Date()));
                    or.setParameters(null);
                    or.setDescription("Usted no tiene el iccid " + iccid + " asignado");
                    or.setDeviceId(null);
                    or.setIdTransaction(String.valueOf(unixTime));
                    or.setStatus("I");
                }
            } else {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("No existe el chip con número de serie: " + iccid);
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
