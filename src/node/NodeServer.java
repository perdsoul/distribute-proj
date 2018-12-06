package node;

import rpc.handler.SearchNodeServerHandler;
import rpc.server.RPCServer;

public class NodeServer {
    public static void main(String[] args) {
        RPCServer server = new RPCServer("localhost", 45454, 2, 16);
        server.service("search", String.class, new SearchNodeServerHandler());
        server.start();
    }
}
