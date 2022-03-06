package program.xxx.rpc.transfer.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import program.xxx.rpc.hook.ShutDownHook;
import program.xxx.rpc.transfer.RpcServer;
import program.xxx.rpc.codec.CommonDecoder;
import program.xxx.rpc.codec.CommonEncoder;
import program.xxx.rpc.enumeration.RpcError;
import program.xxx.rpc.exception.RpcException;
import program.xxx.rpc.provider.ServiceProvider;
import program.xxx.rpc.provider.ServiceProviderImpl;
import program.xxx.rpc.registry.NacosServiceProvider;
import program.xxx.rpc.registry.ServiceRegistry;
import program.xxx.rpc.serializer.CommonSerializer;

import java.net.InetSocketAddress;

public class NettyRpcServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);

    private final ServiceProvider provider = new ServiceProviderImpl();
    private final ServiceRegistry registry = new NacosServiceProvider();

    private final CommonSerializer serializer;

    private String host;
    private int port;

    public NettyRpcServer(String host, int port, int serializeId) {
        this.host = host;
        this.port = port;
        this.serializer = CommonSerializer.getByCode(serializeId);
    }

    @Override
    public void start(int port) {
        ShutDownHook.getShutdownHook().addClearAllHook();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new CommonEncoder(serializer));
                            //pipeline.addLast(new CommonEncoder(new KryoSerializer()));
                            //pipeline.addLast(new CommonEncoder(new JsonSerializer()));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("启动服务器时有错误发生: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public <T> void publishService(T service, Class<T> serviceClass) {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        registry.register(serviceClass.getName(),new InetSocketAddress(host,port));
        provider.addServiceProvider(service,serviceClass);
        start(port);
    }
}
