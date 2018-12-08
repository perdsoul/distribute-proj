package node;

import node.pojo.FileSaveMessage;
import node.pojo.FileSearchMessage;
import rpc.handler.FileSaveServerHandler;
import rpc.handler.SearchFileServerHandler;
import rpc.handler.SearchNodeServerHandler;
import rpc.server.RPCServer;

public class NodeServer {
    public static void start(String ip) {
        RPCServer server = new RPCServer(ip, 45455, 2, 16);
        server.service("search", String.class, new SearchNodeServerHandler()).
                service("save", FileSaveMessage.class, new FileSaveServerHandler()).
                service("searchFile", FileSearchMessage.class, new SearchFileServerHandler());
        server.start();
    }
}