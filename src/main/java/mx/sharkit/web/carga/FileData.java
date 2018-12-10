package mx.sharkit.web.carga;

import javax.activation.DataHandler;

/**
 *
 * @author asalgado
 */
public class FileData {
    
    private String fileName;
    private String fileType;
    private String contentType;
    private DataHandler dataHandler;
    private long fileSize;

    public FileData() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "FileData{" + "fileName=" + fileName + ", fileType=" + fileType + ", contentType=" + contentType + ", fileSize=" + fileSize + '}';
    }
    
}
