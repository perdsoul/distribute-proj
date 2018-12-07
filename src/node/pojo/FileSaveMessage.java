package node.pojo;

public class FileSaveMessage {

    private String requestId;
    private String filename;
    private String srcIp;
    private byte[] data;

    public FileSaveMessage(String requestId, String filename, String srcIp, byte[] data) {
        this.requestId = requestId;
        this.filename = filename;
        this.srcIp = srcIp;
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}

