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
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.restObject.ObjectRequest;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
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
public class ChipController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    @PersistenceContext
    private EntityManager entityManager;
    long unixTime;

    @CrossOrigin
    @ApiOperation(value = "findChips", nickname = "findChips")
    @RequestMapping(method = RequestMethod.POST, path = "/findChips", produces = "application/json")
    @ResponseBody
    public ObjectRequest findChips(@RequestBody Map body) {

        Map<String, Object> mapBody = (Map<String, Object>) body.get("params");
        System.out.println(mapBody);
        ObjectRequest or = new ObjectRequest();
        try {
            String nombre = null, iccid = null, nip = null, tipoRol = null, fechaInicio = null, fechaFin = null;
            Integer idEstatus = null;
            Integer idUsuario = null;
            for (Map.Entry<String, Object> entry : mapBody.entrySet()) {
                switch (entry.getKey()) {
                    case "nombre":
                        nombre = (String) entry.getValue();
                        break;
                    case "iccid":
                        iccid = (String) entry.getValue();
                        break;
                    case "nip":
                        nip = (String) entry.getValue();
                        break;
                    case "tipoRol":
                        tipoRol = (String) entry.getValue();
                        break;
                    case "fechaInicio":
                        fechaInicio = (String) entry.getValue() + " 00:00:00";
                        break;
                    case "fechaFin":
                        fechaFin = (String) entry.getValue() + " 59:59:59";
                        break;
                    case "idEstatus":
                        idEstatus = (Integer) entry.getValue();
                        break;
                    case "idUsuario":
                        idUsuario = (Integer) entry.getValue();
                        break;
                }
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
            if (idUsuario == null || idUsuario <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error idUsuario es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }

            Session sess = (Session) entityManager.getDelegate();
            DetachedCriteria crit = DetachedCriteria.forClass(Chip.class);
            List<Map<String, Object>> lista = new ArrayList<>();
            if (iccid != null && iccid.length() != 0) {
                crit.add(Restrictions.ilike("serie", iccid, MatchMode.ANYWHERE));
            }
            if (nombre != null && nombre.length() != 0) {
                crit.add(Restrictions.ilike("nombreCliente", nombre, MatchMode.ANYWHERE));
            }
            if (nip != null && nip.length() != 0) {
                crit.add(Restrictions.ilike("nip", nip, MatchMode.ANYWHERE));
            }
//            if (idEstatus != null && idEstatus != 0) {
//                crit.add(Restrictions.eq("estatusId", idEstatus));
//            }

            if (fechaInicio != null && fechaInicio.length() >= 0 && fechaFin != null && fechaFin.length() >= 0) {
                Date fi, ff;
                fi = sdf.parse(fechaInicio);
                ff = sdf.parse(fechaFin);
                if (fi.after(ff)) {
                    or.setTimeRequest(sdf.format(new Date()));
                    or.setParameters(null);
                    or.setDescription("Error 'Fecha Inicio' no puede ser mayor a 'Fecha Fin'");
                    or.setDeviceId(null);
                    or.setIdTransaction(String.valueOf(1));
                    or.setStatus("I");
                    return or;
                } else {
                    crit.add(Restrictions.between("fecha", fi, ff));
                }
            }

            //Datos del usuario
            if (!tipoRol.equals("Admin")) {
                if (tipoRol.equals("Supervisor") || tipoRol.equals("Dos")) {
                    crit.add(Restrictions.eq("usuarioSupervisorId", new Long(idUsuario)));
                } else if (tipoRol.equals("Vendedor")) {
                    crit.add(Restrictions.eq("usuarioVendedorId", new Long(idUsuario)));
                } else {
                    crit.add(Restrictions.eq("usuarioVendedorId", new Long(0)));
                }
            }
            //

            Criteria executableCriteria = crit.getExecutableCriteria(sess);
            List<Chip> chips = executableCriteria.list();
            if (chips != null && !chips.isEmpty()) {
                for (Chip c : chips) {
                    //
                    Map<String, Object> mapa = new LinkedHashMap<>();
                    if (idEstatus != null) {
                        if (c.getEstatusProceso() != null && c.getEstatusProcesoId() == idEstatus) {
                            mapa.put("id", c.getId());
                            mapa.put("nip", c.getNip());
                            mapa.put("nombreCliente", c.getNombreCliente());
                            mapa.put("estatus", c.getEstatusProceso().getNombre());
                            mapa.put("iccid", c.getSerie());
                            mapa.put("numero", c.getDn());
                            mapa.put("temporal", c.getDnTemporal());
                            lista.add(mapa);
                        } else if (c.getEstatusPortabilidad() != null && c.getEstatusPortabilidadId() == idEstatus) {
                            mapa.put("id", c.getId());
                            mapa.put("nip", c.getNip());
                            mapa.put("nombreCliente", c.getNombreCliente());
                            mapa.put("estatus", c.getEstatusPortabilidad().getNombre());
                            mapa.put("iccid", c.getSerie());
                            mapa.put("numero", c.getDn());
                            mapa.put("temporal", c.getDnTemporal());
                            lista.add(mapa);
                        } else {
                            mapa.put("id", c.getId());
                            mapa.put("nip", c.getNip());
                            mapa.put("nombreCliente", c.getNombreCliente());
                            mapa.put("estatus", c.getEstatus().getNombre());
                            mapa.put("iccid", c.getSerie());
                            mapa.put("numero", c.getDn());
                            mapa.put("temporal", c.getDnTemporal());
                            lista.add(mapa);
                        }
                    } else {
                        if (c.getEstatusProceso() != null) {
                            mapa.put("id", c.getId());
                            mapa.put("nip", c.getNip());
                            mapa.put("nombreCliente", c.getNombreCliente());
                            mapa.put("estatus", c.getEstatusProceso().getNombre());
                            mapa.put("iccid", c.getSerie());
                            mapa.put("numero", c.getDn());
                            mapa.put("temporal", c.getDnTemporal());
                            lista.add(mapa);
                        } else if (c.getEstatusPortabilidad() != null) {
                            mapa.put("id", c.getId());
                            mapa.put("nip", c.getNip());
                            mapa.put("nombreCliente", c.getNombreCliente());
                            mapa.put("estatus", c.getEstatusPortabilidad().getNombre());
                            mapa.put("iccid", c.getSerie());
                            mapa.put("numero", c.getDn());
                            mapa.put("temporal", c.getDnTemporal());
                            lista.add(mapa);
                        } else {
                            mapa.put("id", c.getId());
                            mapa.put("nip", c.getNip());
                            mapa.put("nombreCliente", c.getNombreCliente());
                            mapa.put("estatus", c.getEstatus().getNombre());
                            mapa.put("iccid", c.getSerie());
                            mapa.put("numero", c.getDn());
                            mapa.put("temporal", c.getDnTemporal());
                            lista.add(mapa);
                        }
                    }

                    //
                }
                for (Map<String, Object> map : lista) {
                    System.out.println("-> " + map);
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
                or.setDescription("No se encontraron chips");
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
    @ApiOperation(value = "findChipsPaginator", nickname = "findChipsPaginator")
    @RequestMapping(method = RequestMethod.POST, path = "/findChipsPaginator", produces = "application/json")
    @ResponseBody
    public ObjectRequest findChipsPaginator(@RequestBody Map body) {

        Map<String, Object> mapBody = (Map<String, Object>) body.get("params");
        System.out.println(mapBody);
        ObjectRequest or = new ObjectRequest();
        boolean bandera = false;
        try {
            String nombre = null, iccid = null, nip = null, tipoRol = null, fechaInicio = null, fechaFin = null, dn = null;
            Integer idEstatus = null, idEstatusProceso = null, idEstatusPortabilidad = null;
            Integer idUsuario = null;
            Integer pagina = null;
            for (Map.Entry<String, Object> entry : mapBody.entrySet()) {
                switch (entry.getKey()) {
                    case "nombre":
                        nombre = (String) entry.getValue();
                        break;
                    case "iccid":
                        iccid = (String) entry.getValue();
                        break;
                    case "nip":
                        nip = (String) entry.getValue();
                        break;
                    case "tipoRol":
                        tipoRol = (String) entry.getValue();
                        break;
                    case "fechaInicio":
                        fechaInicio = (String) entry.getValue() + " 00:00:00";
                        break;
                    case "fechaFin":
                        fechaFin = (String) entry.getValue() + " 59:59:59";
                        break;
                    case "idEstatus":
                        idEstatus = (Integer) entry.getValue();
                        break;
                    case "idEstatusProceso":
                        idEstatusProceso = (Integer) entry.getValue();
                        break;
                    case "idEstatusPortabilidad":
                        idEstatusPortabilidad = (Integer) entry.getValue();
                        break;
                    case "idUsuario":
                        idUsuario = (Integer) entry.getValue();
                        break;
                    case "dn":
                        dn = (String) entry.getValue();
                        break;
                    case "pagina":
                        pagina = (Integer) entry.getValue();
                        System.out.println("Página: " + pagina);
                        break;
                }
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
            if (idUsuario == null || idUsuario <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error idUsuario es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }

            Session sess = (Session) entityManager.getDelegate();
            DetachedCriteria crit = DetachedCriteria.forClass(Chip.class);
            List<Map<String, Object>> lista = new ArrayList<>();
            if (iccid != null && iccid.length() != 0) {
                crit.add(Restrictions.ilike("serie", iccid.toUpperCase(), MatchMode.ANYWHERE));
            }
            if (nombre != null && nombre.length() != 0) {
                crit.add(Restrictions.ilike("nombreCliente", nombre, MatchMode.ANYWHERE));
            }
            if (nip != null && nip.length() != 0) {
                crit.add(Restrictions.ilike("nip", nip, MatchMode.ANYWHERE));
            }
            if (dn != null && dn.length() != 0) {
                crit.add(Restrictions.eq("dn", dn));
            }
            if (idEstatus != null && idEstatus != 0) {
                crit.add(Restrictions.eq("estatusId", idEstatus));
            }
            if (idEstatusPortabilidad != null && idEstatusPortabilidad != 0) {
                crit.add(Restrictions.eq("estatusPortabilidadId", idEstatusPortabilidad));
            }
            if (idEstatusProceso != null && idEstatusProceso != 0) {
                crit.add(Restrictions.eq("estatusProcesoId", idEstatusProceso));
            }

            if (fechaInicio != null && fechaInicio.length() >= 0 && fechaFin != null && fechaFin.length() >= 0) {
                Date fi, ff;
                fi = sdf.parse(fechaInicio);
                ff = sdf.parse(fechaFin);
                if (fi.after(ff)) {
                    or.setTimeRequest(sdf.format(new Date()));
                    or.setParameters(null);
                    or.setDescription("Error 'Fecha Inicio' no puede ser mayor a 'Fecha Fin'");
                    or.setDeviceId(null);
                    or.setIdTransaction(String.valueOf(1));
                    or.setStatus("I");
                    return or;
                } else {
                    System.out.println("fi: " + fechaInicio);
                    System.out.println("ff: " + fechaFin);
                    crit.add(Restrictions.between("fecha", fi, ff));
                }
            }

            //Datos del usuario
            if (!tipoRol.equals("Admin")) {
                if (tipoRol.equals("Supervisor") || tipoRol.equals("Dos")) {
                    crit.add(Restrictions.eq("usuarioSupervisorId", new Long(idUsuario)));
                    bandera = true;
                } else if (tipoRol.equals("Vendedor")) {
                    System.out.println("-----------------Vendedor------------------");
                    crit.add(Restrictions.eq("usuarioVendedorId", new Long(idUsuario)));
                } else if (tipoRol.equals("Coord")) {
                    System.out.println("-----------------Coordinador------------------");
                    crit.add(Restrictions.eq("usuarioCoordinadorId", new Long(idUsuario)));
                } else {
                    crit.add(Restrictions.eq("usuarioVendedorId", new Long(0)));
                }
            }
            //
            crit.addOrder(Order.desc("fechaUltModificacion"));
            Criteria executableCriteria = crit.getExecutableCriteria(sess);
            if (pagina == 1) {
                executableCriteria.setFirstResult(pagina - 1);
                System.out.println("pagina: " + pagina);
            } else {
                executableCriteria.setFirstResult((pagina - 1) * 10);
                System.out.println("pagina: " + (pagina - 1) * 10);
            }
            executableCriteria.setMaxResults(10);
            List<Chip> chips = executableCriteria.list();
            if (!chips.isEmpty()) {
                for (Chip c : chips) {
                    //
                    Map<String, Object> mapa = new LinkedHashMap<>();
                    mapa.put("id", c.getId());
                    mapa.put("nip", c.getNip());
                    mapa.put("nombreCliente", c.getNombreCliente());
                    //
                    if (c.getEstatusPortabilidad() != null) {
                        if (!c.getEstatusPortabilidad().getNombre().equals("PORTABILIDAD EXITOSA")) {
                            mapa.put("color", "red");
                            mapa.put("motivo", c.getObservacionErrorProceso());
                            mapa.put("estatus", c.getEstatusPortabilidad().getNombre());
                            mapa.put("editable",1);
                        } else {

                            if (c.getEstatusProceso() != null) {
                                if (c.getEstatusProceso().getNombre().equals("ERRÓNEA")) {
                                    mapa.put("color", "red");
                                    mapa.put("motivo", c.getObservacionErrorProceso());
                                    mapa.put("estatus", c.getEstatusProceso().getNombre());
                                    mapa.put("editable",0);
                                } else {
                                    mapa.put("color", "black");
                                    mapa.put("motivo", null);
                                    mapa.put("estatus", c.getEstatusProceso().getNombre());
                                    mapa.put("editable",0);
                                }
                            } else {
                                mapa.put("color", "black");
                                mapa.put("motivo", null);
                                mapa.put("estatus", c.getEstatusPortabilidad().getNombre());
                                mapa.put("editable",0);
                            }
                        }
                    } else {
                        mapa.put("estatus", c.getEstatus().getNombre());
                        mapa.put("color", "black");
                        mapa.put("motivo", null);
                        mapa.put("editable",0);
                    }
                    //
                    if (bandera) {
                        mapa.put("temporal", c.getDnTemporal());
                    }

                    mapa.put("iccid", c.getSerie());
                    mapa.put("numero", c.getDn());
                    lista.add(mapa);
                    //
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
                or.setDescription("No se encontraron chips");
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
    @ApiOperation(value = "findChipsErroneos", nickname = "findChipsErroneos")
    @RequestMapping(method = RequestMethod.POST, path = "/findChipsErroneos", produces = "application/json")
    @ResponseBody
    public ObjectRequest findChipsErroneos(@RequestBody Map body) {

        Map<String, Object> mapBody = (Map<String, Object>) body.get("params");
        System.out.println(mapBody);
        ObjectRequest or = new ObjectRequest();
        try {
            String tipoRol = null;
            Integer idUsuario = null;
            Integer pagina = null;
            for (Map.Entry<String, Object> entry : mapBody.entrySet()) {
                switch (entry.getKey()) {
                    case "tipoRol":
                        tipoRol = (String) entry.getValue();
                        break;
                    case "idUsuario":
                        idUsuario = (Integer) entry.getValue();
                        break;
                    case "pagina":
                        pagina = (Integer) entry.getValue();
                        System.out.println("Página: " + pagina);
                        break;
                }
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
            if (idUsuario == null || idUsuario <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error idUsuario es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }

            Session sess = (Session) entityManager.getDelegate();
            DetachedCriteria crit = DetachedCriteria.forClass(Chip.class);
            List<Map<String, Object>> lista = new ArrayList<>();
            //Datos del usuario
            if (tipoRol.equals("Supervisor") || tipoRol.equals("Dos")) {
                crit.add(Restrictions.eq("usuarioSupervisorId", new Long(idUsuario)));
            } else {
                crit.add(Restrictions.eq("usuarioSupervisorId", new Long(0)));
            }
            //
//            crit.add(Restrictions.eq("estatusProcesoId",14));
//            crit.add(Restrictions.ne("estatusPortabilidadId",11));

            crit.add(Restrictions.or(
                    Restrictions.ne("estatusPortabilidadId", Estatus.ID_ESTATUS_PORTABILIDAD_EXITOSA),
                    Restrictions.eq("estatusProcesoId", Estatus.ID_ESTATUS_PROCESO_ERRONEA)));
            //
            crit.add(Restrictions.isNotNull("usuarioVendedorId"));
            crit.addOrder(Order.desc("fechaUltModificacion"));
            Criteria executableCriteria = crit.getExecutableCriteria(sess);
            List<Chip> chips2 = executableCriteria.list();
            if (pagina == 1) {
                executableCriteria.setFirstResult(pagina - 1);
                System.out.println("pagina: " + pagina);
            } else {
                executableCriteria.setFirstResult((pagina - 1) * 10);
                System.out.println("pagina: " + (pagina - 1) * 10);
            }
            executableCriteria.setMaxResults(10);
            List<Chip> chips = executableCriteria.list();
            if (!chips.isEmpty()) {
                for (Chip c : chips) {
                    //
                    Map<String, Object> mapa = new LinkedHashMap<>();
                    mapa.put("id", c.getId());
                    mapa.put("nombreVendedor", c.getUsuarioVendedor().getNombre() + " " + c.getUsuarioVendedor().getPaterno() + " " + c.getUsuarioVendedor().getMaterno());
                    //
                    mapa.put("iccid", c.getSerie());
                    mapa.put("numero", c.getDn());
                    mapa.put("temporal", c.getDnTemporal());
                    mapa.put("latitud", c.getLatitudVenta());
                    mapa.put("longitud", c.getLongitudVenta());
                    mapa.put("nip", c.getNip());
                    lista.add(mapa);
                    //
                }
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(lista);
                or.setDescription("Existen " + chips2.size() + " chips con estatus de error");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(unixTime));
                or.setStatus("A");
            } else {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("No se encontraron chips");
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
    @ApiOperation(value = "editChip", nickname = "editChip")
    @RequestMapping(method = RequestMethod.POST, path = "/editChip", produces = "application/json")
    @ResponseBody
    public ObjectRequest editChip(@RequestBody Map body) {

        Map<String, Object> mapBody = (Map<String, Object>) body.get("params");
        System.out.println(mapBody);
        ObjectRequest or = new ObjectRequest();
        try {
            String nip = null, dn = null;
            Integer idChip = null;
            for (Map.Entry<String, Object> entry : mapBody.entrySet()) {
                switch (entry.getKey()) {
                    case "nip":
                        nip = (String) entry.getValue();
                        break;
                    case "dn":
                        dn = (String) entry.getValue();
                        break;
                    case "idChip":
                        idChip = (Integer) entry.getValue();
                        break;
                }
            }
            System.out.println("nip: "+nip);
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
            if (dn == null || dn.length() <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error dn es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }
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
            List<Map<String, Object>> lista = new ArrayList<>();
            crit.add(Restrictions.eq("id", new Long(idChip)));
            Criteria executableCriteria = crit.getExecutableCriteria(sess);
            List<Chip> chips = executableCriteria.list();
            if (!chips.isEmpty()) {
                Chip c = chips.get(0);
                c.setDn(dn);
                c.setNip(nip);
                c.setFechaUltModificacion(new Date());
                sess.flush();
                sess.persist(c);
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(lista);
                or.setDescription("Exitoso");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(unixTime));
                or.setStatus("A");
            } else {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("No se encontró el chip con id: "+idChip);
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
