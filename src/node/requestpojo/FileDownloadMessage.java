package node.requestpojo;

public class FileDownloadMessage {
    private String messageId;
    private String filename;
    private String requestIp;

    public FileDownloadMessage(String messageId, String filename, String requestIp) {
        this.messageId = messageId;
        this.filename = filename;
        this.requestIp = requestIp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }
}
