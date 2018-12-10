package node.responsepojo;

public class FileSearchResponse {
    private String saveIp;
    private String filename;

    public FileSearchResponse(String saveIp, String filename) {
        this.saveIp = saveIp;
        this.filename = filename;
    }

    public String getSaveIp() {
        return saveIp;
    }

    public void setSaveIp(String saveIp) {
        this.saveIp = saveIp;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "FileSearchResponse{" +
                "saveIp='" + saveIp + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }
}
