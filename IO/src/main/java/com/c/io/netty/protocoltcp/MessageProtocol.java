package com.c.io.netty.protocoltcp;


import io.netty.buffer.ByteBuf;

/**
 * 协议包；将消息长度和消息内容封装成一个对象
 * <p>
 * - 消息发送方 使用 Encoder 将一个 {@link MessageProtocol} 的 {@link MessageProtocol#len}
 * 和 {@link MessageProtocol#content} 一起写入 {@link ByteBuf}
 * <p>
 * - 消息接收方 使用 Decoder 先读取 {@link ByteBuf#readInt()} 作为一个即将读取内容的长度 len，
 * 再使用 {@link ByteBuf#readBytes(byte[])} )} 读取长度为 len 的字节数组；封装成一个协议包
 * {@link MessageProtocol} 并写入消息列表
 */
public class MessageProtocol {
    private int len; //关键
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
