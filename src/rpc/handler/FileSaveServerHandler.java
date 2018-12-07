package rpc.handler;

import io.netty.channel.ChannelHandlerContext;
import node.pojo.FileSaveMessage;
import rpc.common.IMessageHandler;
import rpc.common.MessageOutput;

import static node.NodeContext.*;

public class FileSaveServerHandler implements IMessageHandler<FileSaveMessage> {

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, FileSaveMessage message) {
        // if this message have searched before, ignore it
        String messageId = message.getRequestId();
        String filename = message.getFilename();
        String srcIp = message.getSrcIp();
        byte[] data = message.getData();

        if (messageSearched.containsKey(messageId)) {
            return;
        }

        messageSearched.put(messageId, 1);
        // save data
        saveFile(filename, data, srcIp);

        ctx.writeAndFlush(new MessageOutput(requestId, "save_res", true));
    }
}

