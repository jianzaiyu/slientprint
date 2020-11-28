package com.gw.print.netty;

import com.gw.print.constants.ServerConstants;
import com.gw.print.support.ConsolePrinter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.InetSocketAddress;

/**
 * Http服务
 */
public class HttpServer {
    private final static int PORT = ServerConstants.SERVER_PORT;

    public static void start() {
        long start = System.currentTimeMillis();
        final HttpHandler httpHandler = new HttpHandler();
        // 创建EventLoopGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                //指定所使用的NIO传输Channel
                .channel(NioServerSocketChannel.class)
                //使用指定的端口设置套接字地址
                .localAddress(new InetSocketAddress(PORT))
                // 添加Handler到Channle的ChannelPipeline
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        // 获取管道
                        socketChannel.pipeline()
                                // 解码+编码
                                .addLast(new HttpServerCodec())
                                /* aggregator，消息聚合器，处理POST请求的消息 */
                                .addLast(new HttpObjectAggregator(1024 * 1024 * 2))
                                /*
                                 * 压缩
                                 * Compresses an HttpMessage and an HttpContent in gzip or deflate encoding
                                 * while respecting the "Accept-Encoding" header.
                                 * If there is no matching encoding, no compression is done.
                                 */
                                .addLast("gzip", new HttpContentCompressor())
                                // HttpHandler被标注为@shareable,所以我们可以总是使用同样的案例
                                .addLast(httpHandler);
                    }
                });
        try {
            long end = System.currentTimeMillis();
            ConsolePrinter.info("打印服务启动，耗时:" + (end - start) + "ms,版本v1.5");
            // 异步地绑定服务器;调用sync方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind().sync();
            // 获取Channel的CloseFuture，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭EventLoopGroup，释放所有的资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
