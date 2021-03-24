package com.c.io.aio.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.util.CharsetUtil;

/**
 * 发送消息；使用 {@link ChannelOutboundInvoker#writeAndFlush(Object)} 接口
 * <p>
 * 在 {@link ChannelHandler#handlerAdded(ChannelHandlerContext)} 时保留
 * {@link ChannelHandlerContext}，可以在之后使用：比如发送消息
 */
public class MyClientHandler extends ChannelInboundHandlerAdapter {

    private String message;
    private ChannelHandlerContext ctx;

    public MyClientHandler() {
        this("inited~");
    }

    public MyClientHandler(String message) {
        this.message = message;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        this.ctx = ctx;
    }

    public void send(String message) {
        this.ctx.writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 发送消息到服务端
        ctx.writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));

        // Scanner scanner = new Scanner(System.in);
        // String next = scanner.next();
        // while (!"exit".equals(next)) {
        //     ctx.writeAndFlush(Unpooled.copiedBuffer(next, CharsetUtil.UTF_8));
        //     next = scanner.next();
        // }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收服务端发送过来的消息
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("收到服务端" + ctx.channel().remoteAddress() + "的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}