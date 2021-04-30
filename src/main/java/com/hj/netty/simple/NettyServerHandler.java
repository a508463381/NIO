package com.hj.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.nio.charset.CharsetEncoder;

/**
 * 我们自定义一个Handler,需要继承netty规定好的某个HandlerAdapter
 *
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据（这里我们可以读取客户端发送的消息）

    /**
     * 1.ChannelHandlerContext ctx 上下文对象，含有 管道pipeline ,通道
     * 2.Object msg 就是客户端发送的数据 默认Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //System.out.println("服务器读取线程" + Thread.currentThread().getName());
        System.out.println("server ctx:"+ ctx);
        //System.out.println("channel和pipeline的关系");
        //Channel channel = ctx.channel();
        //ChannelPipeline pipeline = ctx.pipeline();  //本质是一个双向链表
        //将msg转为一个 ByteBuf
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("客户端发送消息："+ buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址："+ ctx.channel().remoteAddress());

    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        //将数据写入缓冲并刷出
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello -.-",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();

    }
}
