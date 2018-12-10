package mx.sharkit.web.service.impl;

import bsh.Interpreter;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import mx.sharkit.web.model.DefinicionParametros;
import mx.sharkit.web.service.DefinicionParametrosService;
import mx.sharkit.web.service.DynScriptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado D.
 */
@Service
@Transactional
public class DynScriptServiceImpl implements DynScriptService, Serializable {

    private static final Logger LOGGER = Logger.getLogger(DynScriptServiceImpl.class.getName());

    @Autowired
    DefinicionParametrosService parametrosService;

    @Override
    public List<Map<String, Object>> getRegistros(String sCveScript, String sCveConsulta, Map<String, Object> params) throws Exception {
        DefinicionParametros parametro = parametrosService.getParametroByTipoAndSeccionAndClave(
                DynScriptService.TIPO_PARAMETRO_CONSULTAS, sCveScript, sCveConsulta);
        if (parametro == null) {
            throw new Exception("El script '" + sCveConsulta + "' no se encontró en la base de datos.");
        }

        if (StringUtils.isBlank(parametro.getConfiguracion())) {
            throw new Exception("El código sql es nulo.");
        }

        try {
            Configuration cfg = new Configuration();
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            cfg.setTemplateUpdateDelay(0);
            StringReader sr = new StringReader(parametro.getConfiguracion());
            Template temp = new Template("code", sr, cfg);
            StringWriter strw = new StringWriter();
            temp.process(params, strw);

            String sSql = strw.toString();

            return parametrosService.findAllByQueryNativeToMap(sSql);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurri\u00f3 un error al ejecutar la consulta. {0}", e.getMessage());
            throw new Exception("Ocurrió un error al ejecutar la consulta. " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getRegistros(String sCveScript, String sCveConsulta, Object[] params) throws Exception {
        DefinicionParametros parametro = parametrosService.getParametroByTipoAndSeccionAndClave(
                DynScriptService.TIPO_PARAMETRO_CONSULTAS, sCveScript, sCveConsulta);
        if (parametro == null) {
            throw new Exception("El script '" + sCveScript + "' no se encontró en la base de datos.");
        }

        if (StringUtils.isBlank(parametro.getConfiguracion())) {
            throw new Exception("El código sql es nulo.");
        }

        try {
            Configuration cfg = new Configuration();
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            cfg.setTemplateUpdateDelay(0);
            StringReader sr = new StringReader(parametro.getConfiguracion());
            Template temp = new Template("code", sr, cfg);
            StringWriter strw = new StringWriter();
            temp.process(params, strw);

            String sSql = strw.toString();

            return parametrosService.findAllByQueryNativeToMap(sSql, params);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurri\u00f3 un error al ejecutar la consulta. {0}", e.getMessage());
            throw new Exception("Ocurrió un error al ejecutar la consulta. " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> proccess(String sCveScript, String sOperacion, Map<String, Object> request) throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "");

        DefinicionParametros parametro = parametrosService.getParametroByTipoAndSeccionAndClave(
                DynScriptService.TIPO_PARAMETRO_PROCESOS, sCveScript, sOperacion);
        if (parametro == null) {
            throw new Exception("El script '" + sCveScript + "' no se encontró en la base de datos.");
        }

        if (StringUtils.isBlank(parametro.getConfiguracion())) {
            throw new Exception("El código del proceso es nulo.");
        }

        try {
            Interpreter interpreter = new Interpreter();
            if (request != null) {
                Iterator<String> iterator = request.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    interpreter.set(key, request.get(key));
                }
            } else {
                request = new HashMap<>();
            }

            interpreter.set("request", request);
            interpreter.set("response", response);
            interpreter.set("dynScriptService", this);

            if (StringUtils.equals(parametro.getCaracteristica1(), "S")) {
                Scanner scanner = new Scanner(parametro.getConfiguracion());
                System.out.println("Debugging script...");
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    System.out.println(line);
                    interpreter.eval(line);
                }
                scanner.close();
            } else {
                interpreter.eval(parametro.getConfiguracion());
            }
            return response;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurri\u00f3 un error al ejecutar el proceso. {0}", e.getMessage());
            throw new Exception("Ocurrió un error al ejecutar el proceso. " + e.getMessage(), e);
        }

    }

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public Map<String, Object> doTransaction(String sCveScript, String sOperacion, Map<String, Object> request) throws Exception {
        return proccess(sCveScript, sOperacion, request);
    }

    @Override
    public <TEntity> List<TEntity> getRegistros(final Class<TEntity> eClazz, final String sCveScript, final String sCveConsulta, final Map<String, Object> params) throws Exception {
        DefinicionParametros parametro = parametrosService.getParametroByTipoAndSeccionAndClave(
                DynScriptService.TIPO_PARAMETRO_CONSULTAS, sCveScript, sCveConsulta);
        if (parametro == null) {
            throw new Exception("El script '" + sCveScript + "' no se encontró en la base de datos.");
        }

        if (StringUtils.isBlank(parametro.getConfiguracion())) {
            throw new Exception("El código sql es nulo.");
        }

        try {
            Configuration cfg = new Configuration();
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            cfg.setTemplateUpdateDelay(0);
            StringReader sr = new StringReader(parametro.getConfiguracion());
            Template temp = new Template("code", sr, cfg);
            StringWriter strw = new StringWriter();
            temp.process(params, strw);

            String sSql = strw.toString();

            return parametrosService.findAllByQueryNativeToEntity(eClazz, sSql);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurri\u00f3 un error al ejecutar la consulta. {0}", e.getMessage());
            throw new Exception("Ocurrió un error al ejecutar la consulta. " + e.getMessage(), e);
        }

    }

    @Override
    public <TEntity> List<TEntity> getRegistros(Class<TEntity> eClazz, String sCveScript, String sCveConsulta, Object[] params) throws Exception {
        DefinicionParametros parametro = parametrosService.getParametroByTipoAndSeccionAndClave(
                DynScriptService.TIPO_PARAMETRO_CONSULTAS, sCveScript, sCveConsulta);
        if (parametro == null) {
            throw new Exception("El script '" + sCveScript + "' no se encontró en la base de datos.");
        }

        if (StringUtils.isBlank(parametro.getConfiguracion())) {
            throw new Exception("El código sql es nulo.");
        }

        try {
            Configuration cfg = new Configuration();
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            cfg.setTemplateUpdateDelay(0);
            StringReader sr = new StringReader(parametro.getConfiguracion());
            Template temp = new Template("code", sr, cfg);
            StringWriter strw = new StringWriter();
            temp.process(params, strw);

            String sSql = strw.toString();

            return parametrosService.findAllByQueryNativeToEntity(eClazz, sSql, params);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurri\u00f3 un error al ejecutar la consulta. {0}", e.getMessage());
            throw new Exception("Ocurrió un error al ejecutar la consulta. " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getRegistro(String sCveScript, String sCveConsulta, Map<String, Object> params) throws Exception {
        List<Map<String, Object>> rs = getRegistros(sCveScript, sCveConsulta, params);
        if (rs != null && !rs.isEmpty()) {
            return rs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public <TEntity> TEntity getRegistro(Class<TEntity> eClazz, String sCveScript, String sCveConsulta, Map<String, Object> params) throws Exception {
        List<TEntity> rs = getRegistros(eClazz, sCveScript, sCveConsulta, params);
        if (rs != null && !rs.isEmpty()) {
            return rs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public String getScriptSql(String sCveScript, String sCveConsulta, Map<String, Object> params) throws Exception {
        DefinicionParametros parametro = parametrosService.getParametroByTipoAndSeccionAndClave(
                DynScriptService.TIPO_PARAMETRO_CONSULTAS, sCveScript, sCveConsulta);
        if (parametro == null) {
            throw new Exception("El script '" + sCveConsulta + "' no se encontró en la base de datos.");
        }

        if (StringUtils.isBlank(parametro.getConfiguracion())) {
            throw new Exception("El código sql es nulo.");
        }

        try {
            Configuration cfg = new Configuration();
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            cfg.setTemplateUpdateDelay(0);
            StringReader sr = new StringReader(parametro.getConfiguracion());
            Template temp = new Template("code", sr, cfg);
            StringWriter strw = new StringWriter();
            temp.process(params, strw);

            return strw.toString();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurri\u00f3 un error al ejecutar la consulta. {0}", e.getMessage());
            throw new Exception("Ocurrió un error al ejecutar la consulta. " + e.getMessage(), e);
        }
    }

}
