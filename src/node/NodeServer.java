package node;

import rpc.handler.SearchNodeServerHandler;
import rpc.server.RPCServer;

public class NodeServer {
    public static void start(String ip) {
        RPCServer server = new RPCServer(ip, 45455, 2, 16);
        server.service("search", String.class, new SearchNodeServerHandler());
        server.start();
    }
}
