package node;

import rpc.client.RPCClient;
import rpc.common.RequestId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeContext {
    // first node to link
    public static final String START_IP = "";
    public static final int SERVER_POST = 45455;
    // this node's ip
    public static final String ip = "localhost";
    // all neighbors
    public static ConcurrentHashMap<String, NodeClient> neighbors;
    // all message id which had received
    public static ConcurrentHashMap<String, Integer> messageSearched;

    /**
     * init NodeContext, set start node to link and build topology automatic
     */
    static {
        neighbors = new ConcurrentHashMap<String, NodeClient>();
        messageSearched = new ConcurrentHashMap<String, Integer>();
    }

    /**
     * build topology
     */
    public static void buildTopology() {
        // use to collect other ip
        List<String> otherIp = new ArrayList<>();

        // search by neighbors us identity message id
        String messageId = RequestId.next();
        for (Map.Entry<String, NodeClient> n : neighbors.entrySet()) {
            otherIp.addAll(n.getValue().searchNode(messageId));
        }

        // build no more than three link
        int linkNum = neighbors.size();
        for (String ip : otherIp) {
            // no more than three
            if (linkNum >= 3) {
                break;
            }
            // ignore ip haven been linked
            if (neighbors.containsKey(ip)) {
                continue;
            } else {
                // add new neighbor
                NodeClient client = new NodeClient(new RPCClient(ip, SERVER_POST));
                neighbors.put(ip, client);
                linkNum++;
            }
        }
    }
}