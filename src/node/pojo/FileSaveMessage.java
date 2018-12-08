package node.pojo;

public class FileSaveMessage {

    private String messageId;
    private String filename;
    private String srcIp;
    private byte[] data;

    public FileSaveMessage(String messageId, String filename, String srcIp, byte[] data) {
        this.messageId = messageId;
        this.filename = filename;
        this.srcIp = srcIp;
        this.data = data;
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

