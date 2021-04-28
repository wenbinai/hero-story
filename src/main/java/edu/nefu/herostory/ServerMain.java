package edu.nefu.herostory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {
    static private final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {
        // 日志对象
        // bossGroup 负责处理socket连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // workerGroup 负责读写连接消息(业务)
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class); // 服务器信道处理方式
        // 处理客户端逻辑
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        // 1. 解码
                        new HttpServerCodec(),
                        // 2. httpObject最大长度
                        new HttpObjectAggregator(65535),
                        // 3. 建立websocket 长连接
                        new WebSocketServerProtocolHandler("/websocket"),
                        // 4. 对消息进行解码
                        new GameMsgDecoder(),
                        // 5. 处理自己的业务逻辑
                        new GameMsgHandler()
                );
            }
        });
        try {
            ChannelFuture f = b.bind(12345).sync();
            if (f.isSuccess()) {
                LOGGER.info("服务器启动成功");
            }
            // 等待服务器信道关闭,即不要立即退出应用程序, 让应用程序可以一直提供服务
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
