package node;

import node.requestpojo.FileDownloadMessage;
import node.requestpojo.FileSaveMessage;
import node.requestpojo.FileSearchMessage;
import rpc.client.RPCClient;

import java.util.List;
import java.util.Set;

import static node.NodeContext.messageSearched;
import static node.NodeContext.neighbors;

/**
 * Client to do remote request
 */
public class NodeClient {

    private RPCClient client;

    public NodeClient(RPCClient client) {
        this.client = client;
        // there should register all
        this.client.rpc("search_res", List.class).
                rpc("save_res", Boolean.class).
                rpc("searchFile_res", Set.class).
                rpc("download_res", Boolean.class);
    }

    public List<String> searchNode(String messageId) {
        messageSearched.put(messageId, 1);
        return (List<String>) client.send("search", messageId);
    }

    public Set<String> searchFile(FileSearchMessage message) {
        messageSearched.put(message.getMessageId(), 1);
        return (Set<String>) client.send("searchFile", message);
    }

    public Boolean saveFile(FileSaveMessage message) {
        messageSearched.put(message.getMessageId(), 1);
        return (Boolean) client.send("save", message);
    }

    public Boolean downloadFile(FileDownloadMessage message) {
        return (Boolean) client.send("download", message);
    }

    /**
     * build a connect to serverIp:port
     *
     * @param serverIp
     * @param port
     */
    public static void start(String serverIp, int port) {
        // link to start LOCAL_IP,if this node haven't start LOCAL_IP,
        // skip this process(means it's the first node in the net)
        if (!(serverIp == null || serverIp.equals(""))) {
            // ignore if needn't connect
            if (neighbors.containsKey(serverIp) || NodeContext.LOCAL_IP.equals(serverIp)) {
                return;
            }
            NodeClient client = new NodeClient(new RPCClient(serverIp, port));
            neighbors.put(serverIp, client);
        }
    }
}

