package mx.sharkit.web.service;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Adrián Salgado D.
 */
public interface DynScriptService {
    int TIPO_PARAMETRO_CONSULTAS = 1;
    int TIPO_PARAMETRO_PROCESOS = 2;
    int TIPO_PARAMETRO_REPORTES = 3;
    int TIPO_PARAMETRO_PARAMETROS_GRAL = 4;
    
    Map<String, Object> proccess(String sCveScript, String sOperacion, Map<String, Object> request) throws Exception;
    Map<String, Object> doTransaction(String sCveScript, String sOperacion, Map<String, Object> request) throws Exception;
    List<Map<String, Object>> getRegistros(final String sCveScript, final String sCveConsulta, final Map<String, Object> params) throws Exception;
    List<Map<String, Object>> getRegistros(final String sCveScript, final String sCveConsulta, final Object[] params) throws Exception;
    <TEntity> List<TEntity> getRegistros(final Class<TEntity> eClazz, final String sCveScript, final String sCveConsulta, final Map<String, Object> params) throws Exception;
    <TEntity> List<TEntity> getRegistros(final Class<TEntity> eClazz, final String sCveScript, final String sCveConsulta, final Object[] params) throws Exception;
    Map<String, Object> getRegistro(final String sCveScript, final String sCveConsulta, final Map<String, Object> params) throws Exception;
    <TEntity> TEntity getRegistro(final Class<TEntity> eClazz, final String sCveScript, final String sCveConsulta, final Map<String, Object> params) throws Exception;
    String getScriptSql(final String sCveScript, final String sCveConsulta, final Map<String, Object> params) throws Exception;
}

