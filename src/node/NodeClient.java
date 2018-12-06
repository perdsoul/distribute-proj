package node;

import rpc.client.RPCClient;
import rpc.client.RPCException;
import rpc.common.RequestId;

import java.util.List;

/**
 * Client to do remote request
 */
public class NodeClient {

    private RPCClient client;

    public NodeClient(RPCClient client) {
        this.client = client;
        // there should register all information
        this.client.rpc("search_res", List.class);
    }

    public List<String> searchNode(String messageId) {
        return (List<String>) client.send("search", messageId);
    }

    public static void main(String[] args) throws InterruptedException {
        RPCClient client = new RPCClient("localhost", 45454);
        NodeClient demo = new NodeClient(client);
        for (int i = 0; i < 1; i++) {
            try {
                System.out.printf("search_res(%d) = %s\n", i, demo.searchNode(RequestId.next()));
                Thread.sleep(100);
            } catch (RPCException e) {
                i--; // retry
            }
        }
        client.close();
    }

}

