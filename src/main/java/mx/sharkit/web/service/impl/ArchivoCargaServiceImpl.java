package mx.sharkit.web.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.Unmarshaller;
import mx.sharkit.web.carga.CfgArchivoCarga;
import mx.sharkit.web.carga.FileData;
import mx.sharkit.web.carga.ResponseValidate;
import mx.sharkit.web.model.DefinicionParametros;
import mx.sharkit.web.service.ArchivoCargaService;
import mx.sharkit.web.service.DefinicionParametrosService;
import mx.sharkit.web.service.DynScriptService;
import mx.sharkit.web.view.util.SchemaBasedProperties;
import mx.sharkit.web.view.util.TypeCast;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Adrián Salgado D.
 */
@Component
public class ArchivoCargaServiceImpl extends Observable implements Serializable, ArchivoCargaService {

    @Autowired
    private DefinicionParametrosService parametrosService;

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public CfgArchivoCarga getConfiguracionArchivoCarga(Integer tipoParametroId, String seccion, String claveParametro) throws Exception {
        CfgArchivoCarga cfg = new CfgArchivoCarga();

        DefinicionParametros parametro = parametrosService.getParametroByTipoAndSeccionAndClave(
                DynScriptService.TIPO_PARAMETRO_PROCESOS, seccion, claveParametro);

        if (parametro == null) {
            throw new Exception(
                    String.format("El script [%s,%s] no se encontró en la base de datos.", seccion, claveParametro));
        }

        cfg.setId(parametro.getId());
        cfg.setParentId(parametro.getTipoParametroId());
        cfg.setTipoParametroId(parametro.getTipoParametroId());
        cfg.setClaveArchivo(parametro.getClaveParametro());
        cfg.setDescripcion(parametro.getDescripcion());
        cfg.setEncabezados(parametro.getCaracteristica1() != null ? parametro.getCaracteristica1() : "S");
        Integer filaInicio = TypeCast.toInteger(parametro.getCaracteristica2()) != null
                ? TypeCast.toInteger(parametro.getCaracteristica2()) : 1;
        cfg.setFilaInicio(filaInicio);
        Integer columnaInicio = TypeCast.toInteger(parametro.getCaracteristica3()) != null
                ? TypeCast.toInteger(parametro.getCaracteristica3()) : 1;
        cfg.setColumnaInicio(columnaInicio);
        cfg.setProcesaTodoValido(parametro.getCaracteristica1() != null ? parametro.getCaracteristica1() : "S");
        cfg.setCaracteristica5(parametro.getCaracteristica5());
        cfg.setConfiguracionArchivo(parametro.getConfiguracion());

        return cfg;

    }

    @Override
    public Map<String, Map<String, String>> builConfiguracionArchivoCarga(String configuracionArchivo) throws Exception {
        javax.xml.bind.JAXBContext jc = javax.xml.bind.JAXBContext.newInstance(SchemaBasedProperties.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        SchemaBasedProperties props = (SchemaBasedProperties) unmarshaller.unmarshal(
                new ByteArrayInputStream(configuracionArchivo.getBytes("UTF-8")));
        return props.getProperties();

    }

    @Override
    public XSSFWorkbook openArchivoXLSX(FileData fileData) throws Exception {
        //Caso excel Nuevo .xlxs
        XSSFWorkbook documento = new XSSFWorkbook(fileData.getDataHandler().getInputStream());
        return documento;
    }

    @Override
    public HSSFWorkbook openArchivoXLS(FileData fileData) throws Exception {
        //Caso excel Nuevo .xlx
        HSSFWorkbook documento = new HSSFWorkbook(fileData.getDataHandler().getInputStream());
        return documento;
    }

    @Override
    public void validarArchivoCarga(FileData fileData, CfgArchivoCarga cfgArchivoCarga) throws Exception {
        Map<String, Map<String, String>> mpConf = builConfiguracionArchivoCarga(cfgArchivoCarga.getConfiguracionArchivo());
        int filas = 0;
        int filaInicio = cfgArchivoCarga.getFilaInicio();
        String includeHeaders = cfgArchivoCarga.getEncabezados();
        int columnaInicio = cfgArchivoCarga.getColumnaInicio();

        XSSFWorkbook documento = null;
        XSSFSheet sheet = null;
        HSSFWorkbook docHSSF = null;
        HSSFSheet sheetHSSF = null;
        String excelType;
        if (StringUtils.equals("xlsx", FilenameUtils.getExtension(fileData.getFileName())) ) {
            //Caso excel Nuevo .xlxs
            excelType = "xlsx";
            documento = openArchivoXLSX(fileData);
            sheet = documento.getSheetAt(0);//Se lee la primer hoja del documento
            filas = sheet.getLastRowNum();
        } else {
            excelType = "xls";
            docHSSF = openArchivoXLS(fileData);
            sheetHSSF = docHSSF.getSheetAt(0);//Se lee la primer hoja del documento
            filas = sheetHSSF.getLastRowNum();
        }

        boolean skipRow = StringUtils.equals("S", includeHeaders);

        ResponseValidate response = null;
        for (int i = (filaInicio - 1); i <= filas; i++) {
            if (skipRow) {
                skipRow = false;
                continue;
            }

            if (StringUtils.equals("xlsx", excelType)) {
                response = validarRegistroArchivo(i, sheet, mpConf);
            } else {
                response = validarRegistroArchivo(i, sheetHSSF, mpConf);
            }
            setChanged();
            notifyObservers(response);

        }

    }

    @Override
    public ResponseValidate validarRegistroArchivo(int numLine, XSSFSheet sheet, Map<String, Map<String, String>> mpConf) {
        ResponseValidate response = new ResponseValidate();
        Map<String, Object> data = new HashMap<>();
        response.setData(data);
        response.setErrorLine((numLine + 1));

        XSSFRow row = sheet.getRow(numLine);

        if (row == null) {
            return response;
        }

        int j = 0;
        if (mpConf != null && mpConf.size() > 0) {
            for (Map.Entry<String, Map<String, String>> entry : mpConf.entrySet()) {
                String key = entry.getKey();
                Map<String, String> cnfValue = entry.getValue();
                String keyValue = cnfValue.get("key") != null ? cnfValue.get("key") : key;
                String nameValue = cnfValue.get("name") != null ? cnfValue.get("name") : keyValue;

                XSSFCell cell = row.getCell(j);

                String required = TypeCast.toString(cnfValue.get("required"));

                String sValue = null;
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            sValue = cell.getStringCellValue();
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            DataFormatter formatter = new DataFormatter();
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                sValue = formatter.formatCellValue(cell);
                            } else {
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                sValue = TypeCast.toString(cell.getStringCellValue());
                            }
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            sValue = null;
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            switch (cell.getCachedFormulaResultType()) {
                                case Cell.CELL_TYPE_NUMERIC:
                                    sValue = TypeCast.toString(cell.getNumericCellValue());
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    sValue = TypeCast.toString(cell.getRichStringCellValue());
                                    break;
                            }
                            break;
                        default:
                            sValue = cell.getStringCellValue();
                            break;
                    }
                }

                String type = TypeCast.toString(cnfValue.get("type"));
                if (StringUtils.equals("true", required)) {
                    if (StringUtils.isBlank(sValue)) {
                        return new ResponseValidate(false, (numLine + 1), "El campo '" + nameValue + "' es requerido");
                    }
                } else if (StringUtils.isBlank(sValue)) {
                    String sDefault = TypeCast.toString(cnfValue.get("default"));
                    if (StringUtils.isBlank(sDefault)) {
                        sValue = sDefault;
                    } else if (StringUtils.equals("Integer", type) || StringUtils.equals("int", type)) {
                        sValue = "0";
                    } else if (StringUtils.equals("Long", type) || StringUtils.equals("long", type)) {
                        sValue = "0";
                    } else if (StringUtils.equals("Float", type) || StringUtils.equals("float", type)) {
                        sValue = "0.0";
                    } else if (StringUtils.equals("Double", type) || StringUtils.equals("double", type)) {
                        sValue = "0.0";
                    } else if (StringUtils.equals("String", type) || StringUtils.equals("string", type)) {
                        sValue = "";
                    } else if (StringUtils.equals("BigDecimal", type) || StringUtils.equals("bigDecimal", type)) {
                        sValue = "0.0";
                    }
                }

                if (StringUtils.equals("Integer", type) || StringUtils.equals("int", type)) {
                    Integer iVal = TypeCast.toInteger(sValue);
                    if (iVal == null) {
                        response = new ResponseValidate(false, (numLine + 1),
                                String.format("Celda: %s, Tipo de dato inválido: <%s>", nameValue, type));
                        break;
                    }
                    response.getData().put(keyValue, iVal);

                } else if (StringUtils.equals("Long", type) || StringUtils.equals("long", type)) {
                    Long lVal = TypeCast.toLong(sValue);
                    if (lVal == null) {
                        response = new ResponseValidate(false, (numLine + 1),
                                String.format("Celda: %s, Tipo de dato inválido: <%s>", nameValue, type));
                        break;
                    }
                    response.getData().put(keyValue, lVal);

                } else if (StringUtils.equals("Float", type) || StringUtils.equals("float", type)) {
                    Float fVal = TypeCast.toFloat(sValue);
                    if (fVal == null) {
                        response = new ResponseValidate(false, (numLine + 1),
                                String.format("Celda: %s, Tipo de dato inválido: <%s>", nameValue, type));
                        break;
                    }
                    response.getData().put(keyValue, fVal);

                } else if (StringUtils.equals("Double", type) || StringUtils.equals("double", type)) {
                    Double dVal = TypeCast.toDouble(sValue);
                    if (dVal == null) {
                        response = new ResponseValidate(false, (numLine + 1),
                                String.format("Celda: %s, Tipo de dato inválido: <%s>", nameValue, type));
                        break;
                    }
                    response.getData().put(keyValue, dVal);

                } else if (StringUtils.equals("String", type) || StringUtils.equals("string", type)) {

                    String minLength = cnfValue.get("minLength") != null ? cnfValue.get("minLength") : "F";
                    if (!StringUtils.equals("F", minLength)) {
                        Integer iMinLength = TypeCast.toInteger(minLength);
                        if (iMinLength != null && iMinLength > 0) {
                            if (StringUtils.length(sValue) < iMinLength) {
                                response = new ResponseValidate(false, (numLine + 1),
                                        String.format("Celda: %s, minLength: %d", nameValue, iMinLength));
                                break;
                            }
                        }
                    }
                    String maxLength = cnfValue.get("maxLength") != null ? cnfValue.get("maxLength") : "F";
                    if (!StringUtils.equals("F", maxLength)) {
                        Integer iMaxLength = TypeCast.toInteger(maxLength);
                        if (iMaxLength != null && iMaxLength > 0) {
                            if (StringUtils.length(sValue) > iMaxLength) {
                                response = new ResponseValidate(false, (numLine + 1),
                                        String.format("Celda: %s, maxLength: %d", nameValue, iMaxLength));
                                break;
                            }
                        }
                    }
                    String pattern = cnfValue.get("pattern") != null ? cnfValue.get("pattern") : "F";
                    if (!StringUtils.equals("F", pattern)) {
                        Pattern patt = Pattern.compile(pattern);
                        Matcher matcher = patt.matcher(sValue);
                        if (!matcher.matches()) {
                            response = new ResponseValidate(false, (numLine + 1),
                                    String.format("Celda: %s, dato inválido, regex: %s", nameValue, pattern));
                            break;
                        }
                    }
                    String soloNumeros = cnfValue.get("soloNumeros") != null ? cnfValue.get("soloNumeros") : "F";
                    if (StringUtils.equals("true", soloNumeros)) {
                        if (!StringUtils.isNumeric(sValue)) {
                            response = new ResponseValidate(false, (numLine + 1),
                                    String.format("Celda: %s, soloNumeros: %s", nameValue, "false"));
                            break;
                        }
                    }
                    response.getData().put(keyValue, sValue);

                } else if (StringUtils.equals("BigDecimal", type) || StringUtils.equals("bigDecimal", type)) {
                    BigDecimal bdVal = TypeCast.toBigDecimal(sValue);
                    if (bdVal == null) {
                        response = new ResponseValidate(false, (numLine + 1),
                                String.format("Celda: %s, Tipo de dato inválido: <%s>", nameValue, type));
                        break;
                    }
                    response.getData().put(keyValue, bdVal);

                } else if (StringUtils.equals("Date", type) || StringUtils.equals("date", type)) {
                    Date date = null;
                    String pattern = cnfValue.get("pattern") != null ? cnfValue.get("pattern") : "yyyy-MM-dd";

                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        String dateValue = cell.getStringCellValue();
                        date = TypeCast.toDate(dateValue, pattern);
                    } else if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        date = cell.getDateCellValue();
                    }

                    if (date == null) {
                        response = new ResponseValidate(false, (numLine + 1),
                                String.format("Celda: %s, Tipo de dato inválido: <%s>, pattern: '%s'", nameValue, type, pattern));
                        break;
                    }
                    response.getData().put(keyValue, date);

                } else {
//                    row.createCell(j).setCellValue(TypeCast.toString(columnValue));
                }
                j++;
            }
        }
        return response;
    }

    @Override
    public ResponseValidate validarRegistroArchivo(int numLine, HSSFSheet sheet, Map<String, Map<String, String>> mpConf) {
        ResponseValidate response = new ResponseValidate();
        Map<String, Object> data = new HashMap<>();
        response.setData(data);
        response.setErrorLine((numLine + 1));

        HSSFRow row = sheet.getRow(numLine);

        if (row == null) {
            return response;
        }

        int j = 0;
        if (mpConf != null && mpConf.size() > 0) {
            for (Map.Entry<String, Map<String, String>> entry : mpConf.entrySet()) {
                String key = entry.getKey();
                Map<String, String> cnfValue = entry.getValue();
                String keyValue = cnfValue.get("key") != null ? cnfValue.get("key") : key;
                String nameValue = cnfValue.get("name") != null ? cnfValue.get("name") : keyValue;

                HSSFCell cell = row.getCell(j);

                String required = TypeCast.toString(cnfValue.get("required"));

                String sValue = null;
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            sValue = cell.getStringCellValue();
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            DataFormatter formatter = new DataFormatter();
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                sValue = formatter.formatCellValue(cell);
                            } else {
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                sValue = TypeCast.toString(cell.getStringCellValue());
                            }
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            sValue = null;
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            switch (cell.getCachedFormulaResultType()) {
                                case Cell.CELL_TYPE_NUMERIC:
                                    sValue = TypeCast.toString(cell.getNumericCellValue());
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    sValue = TypeCast.toString(cell.getRichStringCellValue());
                                    break;
                            }
                            break;
                        default:
                            sValue = cell.getStringCellValue();
                            break;
                    }
                }

                if (StringUtils.equals("true", required)) {
                    if (StringUtils.isBlank(sValue)) {
                        return new ResponseValidate(false, (numLine + 1), "El campo '" + nameValue + "' es requerido");
                    }
                }

                String type = TypeCast.toString(cnfValue.get("type"));
                if (StringUtils.equals("true", required)) {
                    if (StringUtils.isBlank(sValue)) {
                        return new ResponseValidate(false, (numLine + 1), "El campo '" + nameValue + "' es requerido");
                    }
                } else if (StringUtils.isBlank(sValue)) {
                    String sDefault = TypeCast.toString(cnfValue.get("default"));
                    if (StringUtils.isBlank(sDefault)) {
                        sValue = sDefault;
                    } else if (StringUtils.equals("Integer", type) || StringUtils.equals("int", type)) {
                        sValue = "0";
                    } else if (StringUtils.equals("Long", type) || StringUtils.equals("long", type)) {
                        sValue = "0";
                    } else if (StringUtils.equals("Float", type) || StringUtils.equals("float", type)) {
                        sValue = "0.0";
                    } else if (StringUtils.equals("Double", type) || StringUtils.equals("double", type)) {
                        sValue = "0.0";
                    } else if (StringUtils.equals("String", type) || StringUtils.equals("string", type)) {
                        sValue = "";
                    } else if (StringUtils.equals("BigDecimal", type) || StringUtils.equals("bigDecimal", type)) {
                        sValue = "0.0";
                    } else if (StringUtils.equals("BigDecimal", type) || StringUtils.equals("bigDecimal", type)) {
                        sValue = "0.0";
                    }
                }

                if (StringUtils.equals("Integer", type) || StringUtils.equals("int", type)) {
                    Integer iVal = TypeCast.toInteger(sValue);
                    if (iVal == null) {
                        response = new ResponseValidate(false, (numLine + 1),
                                String.format("Celda: %s, Tipo de dato inválido: <%s>", nameValue, type));
                        break;
                    }
                    response.getData().put(keyValue, iVal);

                } else if (StringUtils.equals("Long", type) || StringUtils.equals("long", type)) {
                    Long lVal = TypeCast.toLong(sValue);
                    if (lVal == null) {
                        response = new ResponseValidate(false, (numLine + 1),
                                String.format("Celda: %s, Tipo de dato inválido: <%s>", nameValue, type));
                        break;
                    }
                    response.getData().put(keyValue, lVal);

                } else if (StringUtils.equals("Float", type) || StringUtils.equals("float", type)) {
                    Float fVal = TypeCast.toFloat(sValue);
                    if (fVal == null) {
                        response = new ResponseValidate(false, (numLine + 1),
                                String.format("Celda: %s, Tipo de dato inválido: <%s>", nameValue, type));
                        break;
                    }
                    response.getData().put(keyValue, fVal);

                } else if (StringUtils.equals("Double", type) || StringUtils.equals("double", type)) {
                    Double dVal = TypeCast.toDouble(sValue);
                    if (dVal == null) {
                        response = new ResponseValidate(false, (numLine + 1),
                                String.format("Celda: %s, Tipo de dato inválido: <%s>", nameValue, type));
                        break;
                    }
                    response.getData().put(keyValue, dVal);

                } else if (StringUtils.equals("String", type) || StringUtils.equals("string", type)) {

                    String minLength = cnfValue.get("minLength") != null ? cnfValue.get("minLength") : "F";
                    if (!StringUtils.equals("F", minLength)) {
                        Integer iMinLength = TypeCast.toInteger(minLength);
                        if (iMinLength != null && iMinLength > 0) {
                            if (StringUtils.length(sValue) < iMinLength) {
                                response = new ResponseValidate(false, (numLine + 1),
                                        String.format("Celda: %s, minLength: %d", nameValue, iMinLength));
                                break;
                            }
                        }
                    }
                    String maxLength = cnfValue.get("maxLength") != null ? cnfValue.get("maxLength") : "F";
                    if (!StringUtils.equals("F", maxLength)) {
                        Integer iMaxLength = TypeCast.toInteger(maxLength);
                        if (iMaxLength != null && iMaxLength > 0) {
                            if (StringUtils.length(sValue) > iMaxLength) {
                                response = new ResponseValidate(false, (numLine + 1),
                                        String.format("Celda: %s, maxLength: %d", nameValue, iMaxLength));
                                break;
                            }
                        }
                    }
                    String pattern = cnfValue.get("pattern") != null ? cnfValue.get("pattern") : "F";
                    if (!StringUtils.equals("F", pattern)) {
                        Pattern patt = Pattern.compile(pattern);
                        Matcher matcher = patt.matcher(sValue);
                        if (!matcher.matches()) {
                            response = new ResponseValidate(false, (numLine + 1),
                                    String.format("Celda: %s, dato inválido, regex: %s", nameValue, pattern));
                            break;
                        }
                    }
                    String soloNumeros = cnfValue.get("soloNumeros") != null ? cnfValue.get("soloNumeros") : "F";
                    if (StringUtils.equals("true", soloNumeros)) {
                        if (!StringUtils.isNumeric(sValue)) {
                            response = new ResponseValidate(false, (numLine + 1),
                                    String.format("Celda: %s, soloNumeros: %s", nameValue, "false"));
                            break;
                        }
                    }
                    response.getData().put(keyValue, sValue);

                } else if (StringUtils.equals("BigDecimal", type) || StringUtils.equals("bigDecimal", type)) {
                    BigDecimal bdVal = TypeCast.toBigDecimal(sValue);
                    if (bdVal == null) {
                        response = new ResponseValidate(false, (numLine + 1),
                                String.format("Celda: %s, Tipo de dato inválido: <%s>", nameValue, type));
                        break;
                    }
                    response.getData().put(keyValue, bdVal);

                } else if (StringUtils.equals("Date", type) || StringUtils.equals("date", type)) {
                    Date date = null;
                    String pattern = cnfValue.get("pattern") != null ? cnfValue.get("pattern") : "yyyy-MM-dd";

                    if (cell != null) {
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            String dateValue = cell.getStringCellValue();
                            date = TypeCast.toDate(dateValue, pattern);
                        } else if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            date = cell.getDateCellValue();
                        }
                    }

                    if (StringUtils.equals("true", required) && date == null) {
                        response = new ResponseValidate(false, (numLine + 1),
                                String.format("Celda: %s, Tipo de dato inválido: <%s>, pattern: '%s'", nameValue, type, pattern));
                        break;
                    }
                    response.getData().put(keyValue, date);

                } else {
//                    row.createCell(j).setCellValue(TypeCast.toString(columnValue));
                }
                j++;
            }
        }
        return response;
    }

    @Override
    public byte[] doGeneraArchivoMuestra(int tipoParametro, String seccion, String claveParametro) throws Exception {

        DefinicionParametros parametro = parametrosService.getParametroByTipoAndSeccionAndClave(
                DynScriptService.TIPO_PARAMETRO_PROCESOS, seccion, claveParametro);

        if (parametro == null) {
            throw new Exception(
                    String.format("El script [%s,%s] no se encontró en la base de datos.", seccion, claveParametro));
        }

        Map<String, Map<String, String>> mpConf = null;

        if (parametro.getConfiguracion() != null) {
            javax.xml.bind.JAXBContext jc = javax.xml.bind.JAXBContext.newInstance(SchemaBasedProperties.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            SchemaBasedProperties props = (SchemaBasedProperties) unmarshaller.unmarshal(
                    new ByteArrayInputStream(parametro.getConfiguracion().getBytes("UTF-8")));
            mpConf = props.getProperties();
        }

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Hoja 1");

        Row row = sheet.createRow(0);

        int j = 0;
        if (mpConf != null && mpConf.size() > 0) {
            for (Map.Entry<String, Map<String, String>> entry : mpConf.entrySet()) {
                Map<String, String> value = entry.getValue();
                String columnName = value.get("name");
                row.createCell(j).setCellValue(StringUtils.defaultIfEmpty(columnName, ""));
                j++;
            }
        }

        ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
        wb.write(fileOut);
        fileOut.close();

        return fileOut.toByteArray();

    }

}
