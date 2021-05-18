package com.c.io.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 自定义的 Handler 需要继承 Netty 规定好的接口; 才能被 Netty 框架所关联
 **/
public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();

        String s = new String(content, StandardCharsets.UTF_8);
        System.out.println(new Date(System.currentTimeMillis()) + "\t收到客户端" + ctx.channel().remoteAddress() + "发送的消息：" + s);
    }

}