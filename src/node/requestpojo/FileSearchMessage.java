package node.requestpojo;

public class FileSearchMessage {
    private String messageId;
    private String key;

    public FileSearchMessage(String messageId, String key) {
        this.messageId = messageId;
        this.key = key;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
