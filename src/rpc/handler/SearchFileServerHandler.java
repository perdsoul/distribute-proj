package rpc.handler;

import io.netty.channel.ChannelHandlerContext;
import rpc.common.IMessageHandler;
import rpc.common.MessageOutput;

import java.util.Set;

import static node.NodeContext.*;

public class SearchFileServerHandler implements IMessageHandler<String> {

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, String messageId) {
        // if this message have searched before, ignore it
        if (messageSearched.containsKey(messageId)) {
            ctx.writeAndFlush(new MessageOutput(requestId, "searchFile_res", null));
            return;
        }
        messageSearched.put(messageId, 1);

        // get all filename
        Set<String> allFiles = searchFile(messageId);
        ctx.writeAndFlush(new MessageOutput(requestId, "searchFile_res", allFiles));
    }
}

