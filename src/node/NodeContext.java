package node;

import node.pojo.FileSaveMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.client.RPCClient;
import rpc.common.RequestId;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeContext {
    private static final String DIR_PATH = "files";
    private static final Logger LOG = LoggerFactory.getLogger(NodeContext.class);
    // first node to link
    public static final String START_IP = "";//"100.66.228.198";
    public static final int SERVER_POST = 45455;
    // this node's LOCAL_IP
    public static final String LOCAL_IP = getLocalHostLANIp();
    // all neighbors
    public static ConcurrentHashMap<String, NodeClient> neighbors;
    // all message id which had received
    public static ConcurrentHashMap<String, Integer> messageSearched;
    // all files upload
    public static ConcurrentHashMap<String, List<String>> filenameAndAddress;

    /**
     * init NodeContext, set start node to link and build topology automatic
     */
    static {
        neighbors = new ConcurrentHashMap<String, NodeClient>();
        messageSearched = new ConcurrentHashMap<String, Integer>();
        filenameAndAddress = new ConcurrentHashMap<String, List<String>> ();
        LOG.info("local IP : " + LOCAL_IP);
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
            if (neighbors.containsKey(ip) || LOCAL_IP.equals(ip)) {
                continue;
            } else {
                // add new neighbor
                NodeClient client = new NodeClient(new RPCClient(ip, SERVER_POST));
                neighbors.put(ip, client);
                linkNum++;
            }
        }
    }

    // 正确的IP拿法，即优先拿site-local地址
    private static String getLocalHostLANIp() {
        try {
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements(); ) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    String ip = addresses.nextElement().getHostAddress();
                    // only get LAN, do limits
                    if (ip != null && ip.length() > 8 && ip.length() < 16) {
                        return ip;
                    }
                }
            }
        } catch (SocketException e) {
            LOG.error("Error when getting host ip address: <{}>.", e.getMessage());
        }
        return null;
    }

    /**
     * upload file to system
     *
     * @param path
     */
    public static void uploadFile(String path) {
        // get filename
        String[] pathSplits = path.split("[/\\\\]");
        String filename = pathSplits[pathSplits.length - 1];

        BufferedInputStream bufIn = null;
        List<Byte> data = new ArrayList<Byte>();
        try {
            bufIn = new BufferedInputStream(new FileInputStream(path));
            byte[] b = new byte[1024];
            int length = 0;
            while ((length = bufIn.read(b)) != -1) {
                for (int i = 0; i < length; i++) {
                    data.add(b[i]);
                }
            }
        } catch (FileNotFoundException e) {
            LOG.info("please ensure hte path is exist");
        } catch (IOException e) {
            LOG.error(e.getMessage());
        } finally {
            if (bufIn != null) {
                try {
                    bufIn.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage());
                }
            }
        }

        byte[] bytes = new byte[data.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = data.get(i);
        }

        /** distribute **/
        List<String> fileAddress = new ArrayList<String>();
        // if more than 10M,split the file and store to other node.
        if (bytes.length > 10 * 1024 * 1024 * 8) {

        } else {
            // save file in other nodes
            String messageId = RequestId.next();
            int i = 0;
            for (Map.Entry<String, NodeClient> n : neighbors.entrySet()) {
                // most save two copy
                if (i >= 2) {
                    break;
                }
                FileSaveMessage message = new FileSaveMessage(messageId, filename, NodeContext.LOCAL_IP, bytes);
                n.getValue().saveFile(message);
                fileAddress.add(n.getKey());
            }
        }

        saveFile(filename, bytes, "localhost");
        fileAddress.add("localhost");


        filenameAndAddress.put(filename, new ArrayList<>());
    }

    public static void saveFile(String filename, byte[] data, String srcIp) {
        String newName = filename + "-|-*-|-" + srcIp;
        BufferedOutputStream bufOut = null;
        try {
            bufOut = new BufferedOutputStream(new FileOutputStream(DIR_PATH + "/" + newName));
            bufOut.write(data);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        } finally {
            if (bufOut != null) {
                try {
                    bufOut.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage());
                }
            }
        }
    }
}
