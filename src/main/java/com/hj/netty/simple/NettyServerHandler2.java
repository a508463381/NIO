package com.hj.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 我们自定义一个Handler,需要继承netty规定好的某个HandlerAdapter
 *
 */
public class NettyServerHandler2 extends ChannelInboundHandlerAdapter {


    //读取数据（这里我们可以读取客户端发送的消息）
    /**
     * 1.ChannelHandlerContext ctx 上下文对象，含有 管道pipeline ,通道
     * 2.Object msg 就是客户端发送的数据 默认Object
     */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {

        //taskQueue自定义任务（异步）   提交到 NIOEventLoop 中的 taskQueue
        ctx.channel().eventLoop().execute(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("来自server的消息（5秒）",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(10*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("来自server的消息（10秒）",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

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
