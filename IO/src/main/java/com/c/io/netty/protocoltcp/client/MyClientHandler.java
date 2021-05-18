package com.c.io.netty.protocoltcp.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

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
        byte[] bytes = message.getBytes(CharsetUtil.UTF_8);
        int length = bytes.length;
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(bytes);
        messageProtocol.setLen(length);

        this.ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        byte[] bytes = message.getBytes(CharsetUtil.UTF_8);
        int length = bytes.length;
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(bytes);
        messageProtocol.setLen(length);

        this.ctx.writeAndFlush(messageProtocol);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();

        String s = new String(content, StandardCharsets.UTF_8);
        System.out.println(new Date(System.currentTimeMillis()) + "\t收到服务器" + ctx.channel().remoteAddress() + "发送的消息：" + s);
    }


}