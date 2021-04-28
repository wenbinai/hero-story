package edu.nefu.herostory;

import edu.nefu.herostory.cmdHandler.UserEntryCmdHandler;
import edu.nefu.herostory.cmdHandler.UserMoveToCmdHandler;
import edu.nefu.herostory.cmdHandler.WhoElseIsHereCmdHandler;
import edu.nefu.herostory.model.UserManager;
import edu.nefu.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;


/**
 * 消息协议
 * 消息长度: short int 2 字节
 * 消息编号: short int 2 字节
 * <p>
 * 消息粘包
 */
public class GameMsgHandler extends SimpleChannelInboundHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Broadcaster.addChannel(ctx.channel());
    }

    /**
     * 客户端断开移出用户
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        // 取消群发消息
        Broadcaster.removeChannel(ctx.channel());
        // 获取用户id
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }
        UserManager.removeUserById(userId);
        GameMsgProtocol.UserQuitResult.Builder resultBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
        resultBuilder.setQuitUserId(userId);

        GameMsgProtocol.UserQuitResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到客户端消息, msgClazz = " + msg.getClass().getName() + "msg = " + msg);

        if (msg instanceof GameMsgProtocol.UserEntryCmd) {
            (new UserEntryCmdHandler()).handle(ctx, (GameMsgProtocol.UserEntryCmd) msg);
        } else if (msg instanceof GameMsgProtocol.WhoElseIsHereCmd) {
            (new WhoElseIsHereCmdHandler()).handle(ctx, (GameMsgProtocol.WhoElseIsHereCmd) msg);
        } else if (msg instanceof GameMsgProtocol.UserMoveToCmd) {
            (new UserMoveToCmdHandler()).handle(ctx, (GameMsgProtocol.UserMoveToCmd) msg);
        }

    }


}
