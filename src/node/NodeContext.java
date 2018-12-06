package node;

import rpc.client.RPCClient;
import rpc.common.RequestId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeContext {
    private static final String START_IP = "";
    private static final int SERVER_POST = 45454;
    public static final String ip = "localhost";
    public static ConcurrentHashMap<String, NodeClient> neighbors;
    public static ConcurrentHashMap<String, Integer> messageSearched;

    static {
        neighbors = new ConcurrentHashMap<String, NodeClient>();
        messageSearched = new ConcurrentHashMap<String, Integer>();

        // add test data
        NodeClient client = new NodeClient(new RPCClient(START_IP, SERVER_POST));
        neighbors.put("124.323.222", client);
    }

    /**
     * build topology
     *
     */
    public static void buildTopology() {
        // use to collect other ip
        List<String> otherIp = new ArrayList<>();
        // search by neighbors
        for (Map.Entry<String, NodeClient> n : neighbors.entrySet()) {
            otherIp.addAll(n.getValue().searchNode(RequestId.next()));
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
