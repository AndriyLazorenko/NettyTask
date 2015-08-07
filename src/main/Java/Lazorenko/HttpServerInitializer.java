package Lazorenko;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Initializer class for HttpServer class. Handles overriding of the initChannel method.
 * Depends on logic of HttpServerHandler class that it introduces.
 * @author andriylazorenko
 */

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * Variables
     */

    private final SslContext sslCtx;
    private static EventExecutorGroup executor = new DefaultEventExecutorGroup(1);
    public static ChannelGroup channels = new DefaultChannelGroup(executor.next());

    /**
     * Constructor for init of server
     * @param sslCtx - SSLContext object
     */

    public HttpServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    /**
     * Method for initialization of <code>Channel</code> object
     * @param ch - SocketChannel object
     */

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new HttpServerCodec());
        p.addLast("aggregator", new HttpObjectAggregator(1048576));
        p.addLast(executor,new HttpServerHandler());
        channels.add(ch);
    }

}
