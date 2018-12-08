package rpc.handler;

import io.netty.channel.ChannelHandlerContext;
import node.requestpojo.FileSearchMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.common.IMessageHandler;
import rpc.common.MessageOutput;

import java.util.Set;

import static node.NodeContext.*;

public class SearchFileServerHandler implements IMessageHandler<FileSearchMessage> {
    private final static Logger LOG = LoggerFactory.getLogger(SearchFileServerHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, FileSearchMessage message) {
        String messageId = message.getMessageId();
        String key = message.getKey();

        // if this message have searched before, ignore it
        if (messageSearched.containsKey(messageId)) {
            ctx.writeAndFlush(new MessageOutput(requestId, "searchFile_res", null));
            return;
        }
        messageSearched.put(messageId, 1);

        // get all filename
        LOG.info("start search file");
        Set<String> allFiles = searchFile(messageId, key);
        LOG.info("search file complete");
        ctx.writeAndFlush(new MessageOutput(requestId, "searchFile_res", allFiles));
    }
}

