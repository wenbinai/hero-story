package edu.nefu.herostory;

import com.google.protobuf.GeneratedMessageV3;
import edu.nefu.herostory.msg.GameMsgProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * 对消息进行解码
 */
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof BinaryWebSocketFrame)) {
            return;
        }

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();

        //消息长度
        byteBuf.readShort();
        // 消息编号
        short msgCode = byteBuf.readShort();
        // 消息体
        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        GeneratedMessageV3 cmd = null;

        switch (msgCode) {
            case GameMsgProtocol
                    .MsgCode.USER_ENTRY_CMD_VALUE:
                cmd = GameMsgProtocol.UserEntryCmd.parseFrom(msgBody);
                break;
        }

        if (null != cmd) {
            ctx.fireChannelRead(cmd);
        }


    }
}
