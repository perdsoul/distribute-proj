package node;

import rpc.client.RPCClient;

import javax.xml.soap.Node;
import java.util.List;
import static node.NodeContext.*;

/**
 * Client to do remote request
 */
public class NodeClient {

    private RPCClient client;

    public NodeClient(RPCClient client) {
        this.client = client;
        // there should register all
        this.client.rpc("search_res", List.class);
    }

    public List<String> searchNode(String messageId) {
        return (List<String>) client.send("search", messageId);
    }

    /**
     * build a connect to serverIp:port
     *
     * @param serverIp
     * @param port
     */
    public static void start(String serverIp, int port) {
        // ignore if needn't connect
        if (neighbors.containsKey(serverIp) || NodeContext.LOCAL_IP.equals(serverIp)) {
            return;
        }
        // link to start LOCAL_IP,if this node haven't start LOCAL_IP,
        // skip this process(means it's the first node in the net)
        if (!(serverIp == null || serverIp.equals(""))) {
            NodeClient client = new NodeClient(new RPCClient(serverIp, port));
            neighbors.put(serverIp, client);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        NodeServer.start(NodeContext.LOCAL_IP);
        buildTopology();
        System.out.println("--------------");
        System.out.println(neighbors);
        NodeClient.start(NodeContext.START_IP, NodeContext.SERVER_POST);
    }

}

