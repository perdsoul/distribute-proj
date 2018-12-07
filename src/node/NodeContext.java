package node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.client.RPCClient;
import rpc.common.RequestId;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeContext {
    private final static Logger LOG = LoggerFactory.getLogger(NodeContext.class);
    // first node to link
    public static final String START_IP = "";
    public static final int SERVER_POST = 45455;
    // this node's LOCAL_IP
    public static String LOCAL_IP = "";
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
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
            LOCAL_IP = addr.getHostAddress().toString(); //获取本机ip
            LOG.info("local IP : " + LOCAL_IP);
        } catch (UnknownHostException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * build topology
     */
    public static void buildTopology() {
        // use to collect other LOCAL_IP
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
            // ignore LOCAL_IP haven been linked
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
