/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.sharkit.web.controllers.rest;

import mx.sharkit.web.restObject.ObjectRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
import mx.sharkit.web.security.SHA1;
import org.apache.tomcat.util.codec.binary.Base64;
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
public class LoginController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    @PersistenceContext
    private EntityManager entityManager;

    @CrossOrigin
    @ApiOperation(value = "getLogin", nickname = "getLogin")
    @RequestMapping(method = RequestMethod.GET, path = "/getLogin", produces = "application/json")
    public ObjectRequest getLogin(String user, String password) {
        ObjectRequest or = new ObjectRequest();
        DetachedCriteria dc = DetachedCriteria.forClass(Usuario.class);
        try {
            if (user == null || user.length() <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error user es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }
            if (password == null || password.length() <= 0) {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(null);
                or.setDescription("Error password es requerido");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("I");
                return or;
            }
            String pass = SHA1.encriptarBase64(password);
            Session sess = (Session) entityManager.getDelegate();
            DetachedCriteria crit = DetachedCriteria.forClass(Usuario.class);
            crit.add(Restrictions.eq("userName", user));
            crit.add(Restrictions.eq("password", pass));
            Criteria executableCriteria = crit.getExecutableCriteria(sess);
            List<Usuario> usuarios = executableCriteria.list();
            if (usuarios != null && !usuarios.isEmpty() && usuarios.get(0) != null) {
                Usuario c = usuarios.get(0);
                List<UsuarioRol> roles = c.getUsuarioRolList();
                if (!roles.isEmpty()) {
                    List<Integer> rolesIntegers = new ArrayList<>();
                    for (UsuarioRol rol : roles) {
                        rolesIntegers.add(rol.getRolId().intValue());
                    }
                    List<Map<String, Object>> menus = new ArrayList<>();
                    Map<String, Object> one = new LinkedHashMap<>();
                    Map<String, Object> two = new LinkedHashMap<>();
                    Map<String, Object> three = new LinkedHashMap<>();
                    Map<String, Object> four = new LinkedHashMap<>();
                    String tipoRol = "No";
                    int editableFunction = 0;
                    if (rolesIntegers.contains(1)) {
//                        one.put("icon", "cpu.png");
//                        one.put("title", "Asignación de Chip");
//                        one.put("goto", "menu.asignacion");
//                        menus.add(one);
//
//                        two.put("icon", "edit.png");
//                        two.put("title", "Alta de Cliente");
//                        two.put("goto", "menu.alta");
//                        menus.add(two);
                        
                        four.put("icon", "discount.png");
                        four.put("title", "Estatus de Sims");
                        four.put("goto", "menu.busquedaVenta");
                        four.put("edit", 0);
                        tipoRol = "Admin";
                    }else if(rolesIntegers.contains(5)){
                        four.put("icon", "discount.png");
                        four.put("title", "Estatus de Sims");
                        four.put("goto", "menu.busquedaVenta");
                        four.put("edit", 0);
                        tipoRol = "Coord";
                    }else if (rolesIntegers.contains(4) && rolesIntegers.contains(3) ) {
                        
                        one.put("icon", "cpu.png");
                        one.put("title", "Asignación de Sim");
                        one.put("goto", "menu.asignacion");
                        menus.add(one);

                        two.put("icon", "edit.png");
                        two.put("title", "Alta de Cliente");
                        two.put("goto", "menu.alta");
                        menus.add(two);
                        
                        four.put("icon", "discount.png");
                        four.put("title", "Estatus de Sims");
                        four.put("goto", "menu.busquedaVenta");
                        menus.add(four);
                        tipoRol = "Dos";
                        editableFunction = 1;
                    } else if (rolesIntegers.contains(3)) {
                        one.put("icon", "cpu.png");
                        one.put("title", "Asignación de Sim");
                        one.put("goto", "menu.asignacion");
                        menus.add(one);
                        
                        four.put("icon", "discount.png");
                        four.put("title", "Estatus de Sims");
                        four.put("goto", "menu.busquedaVenta");
                        menus.add(four);
                        tipoRol = "Supervisor";
                        editableFunction = 1;
                    } else if (rolesIntegers.contains(4)) {
                        two.put("icon", "edit.png");
                        two.put("title", "Alta de Cliente");
                        two.put("goto", "menu.alta");
                        menus.add(two);
                        
                        four.put("icon", "discount.png");
                        four.put("title", "Estatus de Sims");
                        four.put("goto", "menu.busquedaVenta");
                        menus.add(four);
                        tipoRol = "Vendedor";
                        editableFunction = 1;
                    }else{
                        tipoRol = "Sin";
                    }
                    three.put("icon", "settings.png");
                    three.put("title", "Acerca de");
                    three.put("goto", "menu.acercade");
                    menus.add(three);

                    Map<String, Object> mapaUsuario = new LinkedHashMap<>();
                    Map<String, Object> mapaCompleto = new LinkedHashMap<>();
                    mapaUsuario.put("id", c.getId());
                    mapaUsuario.put("nombre", c.getNombre());
                    mapaUsuario.put("apellidoPaterno", c.getPaterno());
                    mapaUsuario.put("apellidoMaterno", c.getMaterno());
                    mapaUsuario.put("email", c.getEmail());
                    mapaUsuario.put("user", c.getUserName());
                    mapaUsuario.put("tipoRol", tipoRol);
                    mapaUsuario.put("editable", editableFunction);

                    mapaCompleto.put("usuario", mapaUsuario);
                    mapaCompleto.put("menu", menus);
                    or.setTimeRequest(sdf.format(new Date()));
                    or.setParameters(mapaCompleto);
                    or.setDescription("Exitoso");
                    or.setDeviceId(null);
                    or.setIdTransaction(String.valueOf(1));
                    or.setStatus("A");
                } else {
                    List<Map<String, Object>> menus = new ArrayList<>();
                    Map<String, Object> three = new LinkedHashMap<>();
                    three.put("icon", "settings.png");
                    three.put("title", "Acerca de");
                    three.put("goto", "menu.acercade");
                    menus.add(three);
                    
                    Map<String, Object> mapaUsuario = new LinkedHashMap<>();
                    Map<String, Object> mapaCompleto = new LinkedHashMap<>();
                    mapaUsuario.put("id", c.getId());
                    mapaUsuario.put("nombre", c.getNombre());
                    mapaUsuario.put("apellidoPaterno", c.getPaterno());
                    mapaUsuario.put("apellidoMaterno", c.getMaterno());
                    mapaUsuario.put("email", c.getEmail());

                    mapaCompleto.put("usuario", mapaUsuario);
                    mapaCompleto.put("menu", menus);
                    or.setTimeRequest(sdf.format(new Date()));
                    or.setParameters(mapaCompleto);
                    or.setDescription("No tiene Rol supervisor ni vendedor");
                    or.setDeviceId(null);
                    or.setIdTransaction(String.valueOf(1));
                    or.setStatus("A");
                }
            } else {
                or.setTimeRequest(sdf.format(new Date()));
                or.setParameters(0);
                or.setDescription("Exitoso");
                or.setDeviceId(null);
                or.setIdTransaction(String.valueOf(1));
                or.setStatus("A");
            }
        } catch (Exception e) {
            e.printStackTrace();
            or.setTimeRequest(sdf.format(new Date()));
            or.setParameters(new ArrayList<>());
            or.setDescription("Error " + e.getMessage());
            or.setDeviceId(null);
            or.setIdTransaction(String.valueOf(1));
            or.setStatus("I");
        } finally {
            return or;
        }
    }
}
