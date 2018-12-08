package rpc.handler;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.common.IMessageHandler;
import rpc.common.MessageOutput;

import java.util.Set;

import static node.NodeContext.*;

public class SearchFileServerHandler implements IMessageHandler<String> {
    private final static Logger LOG = LoggerFactory.getLogger(SearchFileServerHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, String messageId) {
        // if this message have searched before, ignore it
        if (messageSearched.containsKey(messageId)) {
            ctx.writeAndFlush(new MessageOutput(requestId, "searchFile_res", null));
            return;
        }
        messageSearched.put(messageId, 1);

        // get all filename
        LOG.info("start search file");
        Set<String> allFiles = searchFile(messageId);
        LOG.info("search file complete");
        ctx.writeAndFlush(new MessageOutput(requestId, "searchFile_res", allFiles));
    }
}

