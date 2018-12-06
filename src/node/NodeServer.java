package node;

import rpc.handler.SearchNodeServerHandler;
import rpc.server.RPCServer;

public class NodeServer {
    public static void start() {
        RPCServer server = new RPCServer("192.168.31.215", 45455, 2, 16);
        server.service("search", String.class, new SearchNodeServerHandler());
        server.start();
    }
}
