package edu.nefu.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import edu.nefu.herostory.msg.GameMsgProtocol;

/**
 * 指令处理器工厂
 */
public final class CmdHandlerFactory {
    private CmdHandlerFactory() {
    }

    static public ICmdHandler<? extends GeneratedMessageV3> create(Object msg) {
        if (msg instanceof GameMsgProtocol.UserEntryCmd) {
            return new UserEntryCmdHandler();
        } else if (msg instanceof GameMsgProtocol.WhoElseIsHereCmd) {
            return new WhoElseIsHereCmdHandler();
        } else if (msg instanceof GameMsgProtocol.UserMoveToCmd) {
            return new UserMoveToCmdHandler();
        } else {
            return null;
        }
    }


}
