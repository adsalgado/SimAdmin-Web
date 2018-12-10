package mx.sharkit.web.service;

import java.util.Map;
import java.util.Observer;
import mx.sharkit.web.carga.CfgArchivoCarga;
import mx.sharkit.web.carga.FileData;
import mx.sharkit.web.carga.ResponseValidate;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author asalgado
 */
public interface ArchivoCargaService {
    CfgArchivoCarga getConfiguracionArchivoCarga(Integer tipoParametroId, String seccion, String claveParametro) throws Exception;
    Map<String, Map<String, String>> builConfiguracionArchivoCarga(String configuracionArchivo) throws Exception;
    XSSFWorkbook openArchivoXLSX(FileData fileData) throws Exception;
    HSSFWorkbook openArchivoXLS(FileData fileData) throws Exception;
    ResponseValidate validarRegistroArchivo(int numLine, XSSFSheet sheet, Map<String, Map<String, String>> mpConf);
    ResponseValidate validarRegistroArchivo(int numLine, HSSFSheet sheet, Map<String, Map<String, String>> mpConf);
    void validarArchivoCarga(FileData fileData, CfgArchivoCarga cfgArchivoCarga) throws Exception;
    byte[] doGeneraArchivoMuestra(int TIPO_PARAMETRO_PROCESOS, String sCveProceso, String sCveScript) throws Exception;
    void addObserver(Observer o);
}
