package edu.nefu.herostory.cmdHandler;

import edu.nefu.herostory.Broadcaster;
import edu.nefu.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd> {
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd msg) {
        // 获取用户Id, 不能将userId放在客户端cmd中, 不安全, 容易被修改
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        // 防御式编程
        if (null == userId) {
            return;
        }
        GameMsgProtocol.UserMoveToCmd cmd = msg;
        GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
        resultBuilder.setMoveUserId(userId);
        resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
        resultBuilder.setMoveToPosY(cmd.getMoveToPosY());

        GameMsgProtocol.UserMoveToResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}
