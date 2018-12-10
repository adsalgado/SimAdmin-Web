package mx.sharkit.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.sharkit.web.dao.SearchCriteria;
import mx.sharkit.web.model.DefinicionParametros;
import mx.sharkit.web.model.Opcion;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.security.CustomPasswordEncoder;
import mx.sharkit.web.service.DefinicionParametrosService;
import mx.sharkit.web.service.DynScriptService;
import mx.sharkit.web.service.OpcionService;
import mx.sharkit.web.service.UsuarioService;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JoinFacesExampleApplicationIT {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    OpcionService opcionService;
    
    @Autowired
    DynScriptService dynScriptService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    DefinicionParametrosService parametrosService;

//    @Autowired
//    private StandardPasswordEncoder passwordEncoder;
    @Test
    public void contextLoads() {
        System.out.println("Testing service...");
        System.out.println("tst: " + usuarioService.findAll());

        System.out.println("Opciones service...");
        List<Long> lstId = new ArrayList<>();
        lstId.add(1L);
        lstId.add(2L);
        System.out.println("tst: " + opcionService.findOpcionesByRolesId(lstId));

        System.out.println("tst: " + opcionService.findByOpcionIdIsNullOrderByOrden());

        System.out.println("bCrypt: " + bCryptPasswordEncoder.encode("Pfzj4264"));

        StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
        System.out.println("passwordEncoder: " + passwordEncoder.encode("Pfzj4264"));

        CustomPasswordEncoder customPasswordEncoder = new CustomPasswordEncoder();
        System.out.println("customPasswordEncoder: " + customPasswordEncoder.encode("Pfzj4264"));

        System.out.println("matches: " + customPasswordEncoder.matches("Pfzj4264", "zkNdLAAk+0v43rUZUvq36o8o7Fw="));
        
        System.out.println("dbecerril: " + customPasswordEncoder.encode("dbecerril"));
        
        System.out.println("matches dbecerril: " + customPasswordEncoder.matches("dbecerril", "WMFzqx1CZoTPlU3Uv1EaTV3ytck="));

        List<SearchCriteria> params = new ArrayList<>();
        System.out.println("parametrosService:" + parametrosService.search(params));

        System.out.println("usuarioService:" + usuarioService.search(params));

        System.out.println("opcionService:" + opcionService.search(params));

        Usuario exmpl = new Usuario();
        exmpl.setUserName("asalgado");
//        exmpl.setActivo(1);
        System.out.println("Usuario by Example:" + usuarioService.findOne(Example.of(exmpl)));

        List<Object> params2 = new ArrayList<>();
        params2.add("asalgado");
        List<Object[]> lstObjs = usuarioService.findAllByQueryNative(
                "select * from cfg_usuario where user_name = ?1", params2.toArray());
        System.out.println("Usuario by Query native:");
        for (Object[] arryObj : lstObjs) {
            for (Object object : arryObj) {
                System.out.print(object + "\t");
            }
            System.out.println("");
        }

        List<Usuario> usuarios = opcionService.findAllByQueryNativeToEntity(Usuario.class,
                "select * from cfg_usuario where user_name = ?1", params2.toArray());
        for (Usuario usuario : usuarios) {
            System.out.println("usuario: " + usuario);
        }

        params2 = new ArrayList<>();
        params2.add("admin");
        List<Map<String, Object>> lstMp = usuarioService.findAllByQueryNativeToMap(
                "select * from cfg_usuario where user_name = ?", params2.toArray());

        System.out.println("lstMp: " + lstMp);

        DetachedCriteria criteria = DetachedCriteria.forClass(Opcion.class);
        List<Opcion> lstOpc = usuarioService.findByCriteria(criteria);
        for (Opcion opcion : lstOpc) {
            System.out.println("opcion: " + opcion);
        }

        lstOpc = usuarioService.findByCriteria(criteria, 2, 6);
        System.out.println("Crt. max results: " );
        for (Opcion opcion : lstOpc) {
            System.out.println("opcion: " + opcion);
        }
        
        try {
            Map<String, Object> map = new HashMap<>();
            List<DefinicionParametros> lstDyn = 
                    dynScriptService.getRegistros(DefinicionParametros.class, "QUERY_PARAMS", "QUERY_1", map);
            for (DefinicionParametros dp : lstDyn) {
                System.out.println("dp: " + dp);
            }
            dynScriptService.proccess("DEMO", "SCRIPT_1", map);
        } catch (Exception ex) {
            Logger.getLogger(JoinFacesExampleApplicationIT.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
