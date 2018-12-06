package node;

import rpc.client.RPCClient;
import rpc.client.RPCException;
import rpc.common.RequestId;

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

    public static void main(String[] args) throws InterruptedException {
        // link to start ip,if this node haven't start ip,
        // skip this process(means it's the first node in the net)
        if (!(START_IP == null || START_IP.equals(""))) {
            NodeClient client = new NodeClient(new RPCClient(START_IP, SERVER_POST));
            neighbors.put(START_IP, client);
            buildTopology();
        }
        System.out.println(neighbors);
    }

}

