package node;

import node.requestpojo.FileDownloadMessage;
import node.requestpojo.FileSaveMessage;
import node.requestpojo.FileSearchMessage;
import rpc.handler.FileDownloadServerHandler;
import rpc.handler.FileSaveServerHandler;
import rpc.handler.SearchFileServerHandler;
import rpc.handler.SearchNodeServerHandler;
import rpc.server.RPCServer;

public class NodeServer {
    public static void start(String ip) {
        RPCServer server = new RPCServer(ip, 45455, 2, 16);
        server.service("search", String.class, new SearchNodeServerHandler()).
                service("save", FileSaveMessage.class, new FileSaveServerHandler()).
                service("download", FileDownloadMessage.class, new FileDownloadServerHandler()).
                service("searchFile", FileSearchMessage.class, new SearchFileServerHandler());
        server.start();
    }
}