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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.val;
import mx.sharkit.web.DTO.ChipDTO;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.ChipHistoricoEstatus;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.restObject.ObjectRequest;
import mx.sharkit.web.service.ChipHistoricoService;
import mx.sharkit.web.service.ChipService;
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
public class AsignacionController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    @PersistenceContext
    private EntityManager entityManager;
    long unixTime;

    @Autowired
    private ChipService chipService;

    @Autowired
    private ChipHistoricoService chipHistoricoService;

    @CrossOrigin
    @ApiOperation(value = "asignacion", nickname = "asignacion")
    @RequestMapping(method = RequestMethod.POST, path = "/asignacion", produces = "application/json")
    @ResponseBody
    public ObjectRequest asignacion(@RequestBody Map body) {

        Map<String, Object> mapBody = (Map<String, Object>) body.get("params");
        ObjectRequest or = new ObjectRequest();
        try {
            ///AGREGAR VALIDACION DE SI EL SUPERVISOR TIENE ASIGNADO EL CHIP
            String serie = null, tipoRol = null, user = null;
            Integer idVendedor = null;
            Integer idUsuario = null;
            for (Map.Entry<String, Object> entry : mapBody.entrySet()) {
                switch (entry.getKey()) {
                    case "serie":
                        serie = (String) entry.getValue();
                        break;
                    case "idVendedor":
                        idVendedor = (Integer) entry.getValue();
                        break;
                    case "idUsuario":
                        idUsuario = (Integer) entry.getValue();
                        break;
                    case "tipoRol":
                        tipoRol = (String) entry.getValue();
                        break;
                    case "user":
                        user = (String) entry.getValue();
                        break;
                }
            }
            if (serie == null || serie.length() <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error serie es requerido");
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
            if (tipoRol == null || tipoRol.length() <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error tipoRol es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }
            if (idVendedor == null || idVendedor <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error idVendedor es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }
            Session sess = (Session) entityManager.getDelegate();
            DetachedCriteria crit = DetachedCriteria.forClass(Chip.class);
            crit.add(Restrictions.eq("serie", serie));

            //Datos del usuario
            if (!tipoRol.equals("Admin")) {
                if (tipoRol.equals("Supervisor") || tipoRol.equals("Dos")) {
                    crit.add(Restrictions.eq("usuarioSupervisorId", new Long(idUsuario)));
                }
            } else {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Operación disponible solo para Supervisores");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(unixTime));
                or.setStatus("I");
                return or;
            }
            //

            Criteria executableCriteria = crit.getExecutableCriteria(sess);
            List<Chip> chips = executableCriteria.list();
            if (chips != null && !chips.isEmpty()) {
                Chip chip = chips.get(0);
                if (chip.getUsuarioVendedorId() == null) {

                    if (chip.getUsuarioSupervisorId().equals(new Long(idUsuario))) {
                        chip.setUsuarioVendedorId(new Long(idVendedor));
                        chip.setFechaAsignacionVendedor(new Date());
                        chip.setEstatusId(3);
                        chip.setFechaUltModificacion(new Date());
//                    sess.flush();
//                    sess.persist(chip);
                        ChipHistoricoEstatus che = new ChipHistoricoEstatus();
                        che.setEstatusId(3);
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
                        or.setDescription("Supervisor no asignado al chip: " + serie);
                        or.setDeviceId(null);
                        or.setIdTransaction(String.valueOf(unixTime));
                        or.setStatus("I");
                    }
                } else {
                    or.setTimeRequest(sdf.format(new Date()));
                    or.setParameters(null);
                    or.setDescription("Chip ya asignado a " + chip.getUsuarioVendedor().getNombre() + " " + chip.getUsuarioVendedor().getPaterno());
                    or.setDeviceId(null);
                    or.setIdTransaction(String.valueOf(unixTime));
                    or.setStatus("I");
                }
            } else {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("No existe el chip con número de serie: " + serie);
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
    @ApiOperation(value = "asignacionMasiva", nickname = "asignacionMasiva")
    @RequestMapping(method = RequestMethod.POST, path = "/asignacionMasiva", produces = "application/json")
    @ResponseBody
    public ObjectRequest asignacionMasiva(@RequestBody Map body) {

        Map<String, Object> mapBody = (Map<String, Object>) body.get("params");
        System.out.println(mapBody);
        ObjectRequest or = new ObjectRequest();
        ObjectMapper mapper = new ObjectMapper();
        List<String> errores = new ArrayList<>();
        try {
            ///AGREGAR VALIDACION DE SI EL SUPERVISOR TIENE ASIGNADO EL CHIP
            List<ChipDTO> chipsEscaneados = new ArrayList<>();
            List<Chip> chipsEditables = new ArrayList<>();

            String serie = null, tipoRol = null;
            Integer idVendedor = null;
            Integer idUsuario = null;
            for (Map.Entry<String, Object> entry : mapBody.entrySet()) {
                switch (entry.getKey()) {
                    case "serie":
                        //serie = (String) entry.getValue();
//                        JSONParser parser = new JSONParser();
//                        parser.addTypeHint("serie[]", ChipDTO.class);
//                        Map<String, List<ChipDTO>> result1 = parser.parse(Map.class, (String) entry.getValue());
//                        for (Entry<String, List<ChipDTO>> ent : result1.entrySet()) {
//                            for (ChipDTO example : ent.getValue()) {
//                                System.out.println("VALUE :->" + example.getCodigo());
//                            }
//                        }
                        List<Map<String, Object>> lista = (List<Map<String, Object>>) entry.getValue();
                        for (Map<String, Object> mapChip : lista) {
                            ChipDTO c1 = new ChipDTO(Integer.parseInt(mapChip.get("id").toString()), mapChip.get("codigo").toString());
                            chipsEscaneados.add(c1);
                            System.out.println("Exit mapChip");
                        }
//                        chipsEscaneados = mapper.readValue((String) entry.getValue(), new TypeReference<List<ChipDTO>>() {
//                        });
                        break;
                    case "idVendedor":
                        idVendedor = (Integer) entry.getValue();
                        break;
                    case "idUsuario":
                        idUsuario = (Integer) entry.getValue();
                        break;
                    case "tipoRol":
                        tipoRol = (String) entry.getValue();
                        break;
                }
            }
            if (tipoRol == null || tipoRol.length() <= 0) {
                errores.add("Error tipoRol es requerido");
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(errores);
                or.setDescription("Error");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }
            if (idVendedor == null || idVendedor <= 0) {
                errores.add("Error idVendedor es requerido");
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(errores);
                or.setDescription("Error");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }
            Session sess = (Session) entityManager.getDelegate();
            Map<String, Object> ma = new LinkedHashMap<>();
            for (ChipDTO chipsEscaneado : chipsEscaneados) {
                if (ma.get(chipsEscaneado.getCodigo()) == null) {
                    DetachedCriteria crit = DetachedCriteria.forClass(Chip.class);
                    crit.add(Restrictions.eq("serie", chipsEscaneado.getCodigo().toUpperCase()));

                    //Datos del usuario
                    if (!tipoRol.equals("Admin")) {
                        if (tipoRol.equals("Supervisor") || tipoRol.equals("Dos")) {
                            crit.add(Restrictions.eq("usuarioSupervisorId", new Long(idUsuario)));
                        }
                    } else {
                        errores.add("Operación disponible solo para Supervisores");
                        or.setTimeRequest(sdf.format(new Date()));
                        or.setParameters(errores);
                        or.setDescription("Advertencia");
                        or.setDeviceId(null);
                        or.setIdTransaction(String.valueOf(unixTime));
                        or.setStatus("I");
                        return or;
                    }
                    //

                    Criteria executableCriteria = crit.getExecutableCriteria(sess);
                    List<Chip> chips = executableCriteria.list();

                    if (chips != null && !chips.isEmpty()) {
                        Chip chip = chips.get(0);
                        if (chip.getUsuarioVendedorId() == null) {

                            if (chip.getUsuarioSupervisorId().equals(new Long(idUsuario))) {
                                System.out.println("------> " + new Long(idVendedor));
                                chip.setUsuarioVendedorId(new Long(idVendedor));
                                chip.setFechaAsignacionVendedor(new Date());
                                chip.setEstatusId(3);
                                chip.setFechaUltModificacion(new Date());
                                chipsEditables.add(chip);
                                System.out.println(chip);
                                System.out.println("Supervisor asignado al chip " + chipsEscaneado.getCodigo());
                            } else {
                                errores.add("Supervisor no asignado al chip");
                            }

                        } else {
                            errores.add("El Chip " + chipsEscaneado.getCodigo() + " ya asignado a " + chip.getUsuarioVendedor().getNombre() + " " + chip.getUsuarioVendedor().getPaterno());
                        }
                    } else {
                        errores.add("No existe el chip con número de serie: " + chipsEscaneado.getCodigo() + " o aún no le ha sido asignado");
                    }
                    ma.put(chipsEscaneado.getCodigo(), chipsEscaneado);
                }
            }

            if (errores.isEmpty()) {
                String error = chipService.saveChips(chipsEditables);
                errores.add(error);
                if (error.length() == 0) {
                    or.setTimeRequest(sdf.format(new Date()));
                    or.setParameters(null);
                    or.setDescription("Exitoso");
                    or.setDeviceId(null);
                    or.setIdTransaction(String.valueOf(unixTime));
                    or.setStatus("A");
                } else {
                    or.setTimeRequest(sdf.format(new Date()));
                    or.setParameters(errores);
                    or.setDescription("Error");
                    or.setDeviceId(null);
                    or.setIdTransaction(String.valueOf(unixTime));
                    or.setStatus("I");
                }
            } else {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(errores);
                or.setDescription("Error en carga de chips");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(unixTime));
                or.setStatus("I");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errores.add(e.getMessage());
            or.setTimeRequest(sdf.format(new Date()));
            or.setParameters(errores);
            or.setDescription("Error ");
            or.setDeviceId(null);
            or.setIdTransaction(String.valueOf(unixTime));
            or.setStatus("I");
        } finally {
            return or;
        }
    }
}
