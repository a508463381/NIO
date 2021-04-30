package com.hj.netty.simple;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        //客户端需要一个事件循环组
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            //创建客户端启动对象
            Bootstrap bootstrap = new Bootstrap();
            //设置相关参数
            bootstrap.group(group)  //设置线程组
                    .channel(NioSocketChannel.class)  //设置客户端通道的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            System.out.println("客户端启动完毕");

            //启动客户端连接服务器端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8888).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }
        finally {
            group.shutdownGracefully();
        }
    }
}