package com.c.io.netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * 发送消息；使用 {@link ChannelOutboundInvoker#writeAndFlush(Object)} 接口
 */
public class MyClient {

    private String host;
    private int port;

    private MyClientHandler clientHandler;

    private ChannelFuture channelFuture;

    private NioEventLoopGroup eventExecutors;

    public MyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        eventExecutors = new NioEventLoopGroup();
        clientHandler = new MyClientHandler();
        try {
            // 创建bootstrap对象，配置参数
            Bootstrap bootstrap = new Bootstrap();
            // 设置线程组
            bootstrap.group(eventExecutors)
                    // 设置客户端的通道实现类型
                    .channel(NioSocketChannel.class)
                    // 使用匿名内部类初始化通道
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 添加客户端通道的处理器
                            // ch.pipeline().addLast(new StringEncoder());
                            // ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(clientHandler);
                        }
                    });
            System.out.println("客户端准备就绪~");
            // 连接服务端
            // bootstrap.connect(host, port).sync();
            channelFuture = bootstrap.connect(host, port).sync();
        } catch (Exception e) {
            // e.printStackTrace();
            // 关闭线程组
            eventExecutors.shutdownGracefully();
        }
    }

    public void shutdown() {
        if (eventExecutors != null && !eventExecutors.isShutdown()) {
            eventExecutors.shutdownGracefully();
        }
    }

    public void sendMsg(String message) {
        System.out.println("try to send message: \t" + message);
        // this.channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
        this.clientHandler.send(message);
    }

    public void send(String message) {
        System.out.println("try to send message: \t" + message);
        this.channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
    }

    public static void main(String[] args) throws InterruptedException {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 8080;

        MyClient myClient = new MyClient(host, port);
        myClient.run();

        Thread.sleep(1000);
        myClient.sendMsg("logic");
        myClient.send("eto");

        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            myClient.sendMsg("logic");
        }
        myClient.shutdown();
    }

}