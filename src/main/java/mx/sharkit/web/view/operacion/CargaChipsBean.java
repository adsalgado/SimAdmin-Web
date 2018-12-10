package mx.sharkit.web.view.operacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import mx.sharkit.web.carga.CfgArchivoCarga;
import mx.sharkit.web.carga.FileData;
import mx.sharkit.web.carga.ResponseValidate;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.service.ArchivoCargaService;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.DynScriptService;
import mx.sharkit.web.view.util.PFMessages;
import mx.sharkit.web.view.util.TypeCast;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Adrián Salgado D.
 */
@Named
@ViewScoped
public class CargaChipsBean implements Serializable, Observer {

    private static final Logger LOGGER = Logger.getLogger(CargaChipsBean.class.getName());
    private static final String CLAVE_PROCESO = "ARCHIVOS_CARGA";
    private static final String CLAVE_SCRIPT = "CARGA_CHIPS";

    @Autowired
    ArchivoCargaService archivoCargaService;
    @Autowired
    ChipService chipService;

    /**
     * Carga de archivo de chips
     */
    private FileData fileData;
    private List<ResponseValidate> errores;
    private ArrayList<Chip> chips;
    private boolean showPanelChips;

    @PostConstruct
    public void init() {
        ocultaPanelChips();
    }

    public void cleanCarga() {
        fileData = null;
    }
    
    public void limpiarBase() {
        try {
            chipService.limpiarBase();
            PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurrió un error al limpiar la base de datos. ", e);
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al limpiar la base de datos. " + e.getMessage());
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {

            UploadedFile file = event.getFile();

            if (fileData == null) {
                if ((file != null) && (file.getSize() > 0)) {

                    fileData = new FileData();
                    DataSource source = new ByteArrayDataSource(TypeCast.toBytes(file.getInputstream()), file.getContentType());
                    fileData.setFileName(file.getFileName());
                    fileData.setFileType(FilenameUtils.getExtension(file.getFileName()));
                    fileData.setContentType(file.getContentType());
                    fileData.setDataHandler(new DataHandler(source));
                    fileData.setFileSize(file.getSize());

                }
            } else {
//                PFMessages.showMessageError("Ya se cargó el archivo.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Ya se cargó el archivo."));
            }

        } catch (Exception ex) {
//            PFMessages.showMessageErrorBackEnd("Ocurrió un error al cargar el documento. " + ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ocurrió un error al cargar el documento. ", ex.getMessage()));
        }

    }

    public void doProcesaArchivo() {

        try {
            ocultaPanelChips();

            CfgArchivoCarga cfgArchivo = archivoCargaService.getConfiguracionArchivoCarga(DynScriptService.TIPO_PARAMETRO_PROCESOS, CLAVE_PROCESO, CLAVE_SCRIPT);

            errores = new ArrayList<>();
            chips = new ArrayList<>();

            archivoCargaService.addObserver(this);
            archivoCargaService.validarArchivoCarga(fileData, cfgArchivo);

            if (errores.isEmpty()) {
                if (!chips.isEmpty()) {

                    chipService.save(chips);
                    muestraPanelChips();
                    RequestContext.getCurrentInstance().addCallbackParam("saved", true);
//                    PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "LA OPERACIÓN SE REALIZÓ CORRECTAMENTE."));

                } else {
//                    PFMessages.showMessageError("NO SE IMPORTÓ NINGÚN REGISTRO.");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "NO SE IMPORTÓ NINGÚN REGISTRO."));
                }

            } else {
                //Archivo con errores   idDevoluciones
                RequestContext.getCurrentInstance().execute("PF('dlgFile').show();");
                RequestContext.getCurrentInstance().execute("PF('idCarga').hide();");
//                PFMessages.showMessageError("EXISTEN ERRORES EN EL ARCHIVO DE CARGA.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "EXISTEN ERRORES EN EL ARCHIVO DE CARGA."));
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            if (ex.getCause() != null && ex.getCause().getCause() != null) {
//                PFMessages.showMessageErrorBackEnd(ex.getCause().getCause().getMessage());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", ex.getCause().getCause().getMessage()));
            } else {
//                PFMessages.showMessageErrorBackEnd(ex.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", ex.getCause().getCause().getMessage()));
            }
        }

    }

    @Override
    public void update(Observable observable, Object arg) {
        LOGGER.log(Level.FINE, String.format("Observable: %s, Object: %s", observable, arg));

        ResponseValidate response = (ResponseValidate) arg;
        if (!response.isValid()) {
            errores.add(response);
        } else {
            String serie = TypeCast.toString(response.getData().get("serie"));
//            Chip chip = chipService.findBySerie(serie + "F");
            LOGGER.log(Level.FINE, String.format("serie: %s", serie));

            Chip chip = new Chip();
            chip.setSerie(serie + "F");
            chip.setEstatusId(Estatus.ID_ESTATUS_CHIP_NUEVO);
            chip.setFecha((Date) response.getData().get("fecha"));
            chip.setDocumento(TypeCast.toString(response.getData().get("documento")));
            chip.setReferencia(TypeCast.toString(response.getData().get("referencia")));
            chip.setArticulo(TypeCast.toString(response.getData().get("articulo")));
            chip.setDescripcion(TypeCast.toString(response.getData().get("descripcion")));
            chip.setModelo(TypeCast.toString(response.getData().get("modelo")));
            chip.setCantidad(TypeCast.toInteger(response.getData().get("cantidad")));
            chip.setCostoCompra(TypeCast.toBigDecimal(response.getData().get("costo")));
            chips.add(chip);

        }

    }

    public void muestraPanelChips() {
        showPanelChips = true;
    }

    public void ocultaPanelChips() {
        showPanelChips = false;
    }

    public ArchivoCargaService getArchivoCargaService() {
        return archivoCargaService;
    }

    public void setArchivoCargaService(ArchivoCargaService archivoCargaService) {
        this.archivoCargaService = archivoCargaService;
    }

    public ChipService getChipService() {
        return chipService;
    }

    public void setChipService(ChipService chipService) {
        this.chipService = chipService;
    }

    public FileData getFileData() {
        return fileData;
    }

    public void setFileData(FileData fileData) {
        this.fileData = fileData;
    }

    public List<ResponseValidate> getErrores() {
        return errores;
    }

    public void setErrores(List<ResponseValidate> errores) {
        this.errores = errores;
    }

    public ArrayList<Chip> getChips() {
        return chips;
    }

    public void setChips(ArrayList<Chip> chips) {
        this.chips = chips;
    }

    public boolean isShowPanelChips() {
        return showPanelChips;
    }

    public void setShowPanelChips(boolean showPanelChips) {
        this.showPanelChips = showPanelChips;
    }

}
