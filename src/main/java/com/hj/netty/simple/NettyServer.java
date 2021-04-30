package com.hj.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    public static void main(String[] args) throws InterruptedException {

        //创建BossGroup和WorkerGroup

        /**
         * 1.创建两个线程组 bossGroup和workerGroup
         * 2.bossGroup只处理连接请求，真正的客户端业务处理由workerGroup完成
         * 3.两个都是无限循环
         * 4.bossGroup和workerGroup 含有的子线程（NioEventLoop）的个数默认是cpu核数 * 2
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)  //设置两个线程组
                    .channel(NioServerSocketChannel.class)  //使用NioSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)  //设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)  //设置保存活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {  //创建一个通道测试对象
                        //给pipeline 设置处理器
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler2());
                        }
                    });  //给我们的workerGroup 的 EventLoop 对应的管道设置处理器

            System.out.println("服务器准备完毕");

            //绑定一个端口并且同步，生成了一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(8888).sync();

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    



}
