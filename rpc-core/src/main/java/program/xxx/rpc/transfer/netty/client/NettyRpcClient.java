package program.xxx.rpc.transfer.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import program.xxx.rpc.enumeration.SerializedCode;
import program.xxx.rpc.factory.SingletonFactory;
import program.xxx.rpc.loadbalance.LoadBalance;
import program.xxx.rpc.loadbalance.RandomLoadBalancer;
import program.xxx.rpc.transfer.RpcClient;
import program.xxx.rpc.codec.CommonDecoder;
import program.xxx.rpc.codec.CommonEncoder;
import program.xxx.rpc.entity.RpcRequest;
import program.xxx.rpc.entity.RpcResponse;
import program.xxx.rpc.enumeration.RpcError;
import program.xxx.rpc.exception.RpcException;
import program.xxx.rpc.registry.NacosServiceDiscovery;
import program.xxx.rpc.registry.ServiceDiscovery;
import program.xxx.rpc.serializer.CommonSerializer;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

public class NettyRpcClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);

    private static final EventLoopGroup group;
    private static final Bootstrap bootstrap;

    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class);
    }

    private final ServiceDiscovery serviceDiscovery;
    private final CommonSerializer serializer;

    private final UnprocessedRequests unprocessedRequests;

    public NettyRpcClient() {
        this(SerializedCode.KRYO.getCode(), new RandomLoadBalancer());
    }
    public NettyRpcClient(LoadBalance loadBalancer) {
        this(SerializedCode.KRYO.getCode(), loadBalancer);
    }
    public NettyRpcClient(Integer serializer) {
        this(serializer, new RandomLoadBalancer());
    }
    public NettyRpcClient(Integer serializer, LoadBalance loadBalancer) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer = CommonSerializer.getByCode(serializer);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    public CompletableFuture<RpcResponse> sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
            if (!channel.isActive()) {
                group.shutdownGracefully();
                return null;
            }
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    logger.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                } else {
                    future1.channel().close();
                    resultFuture.completeExceptionally(future1.cause());
                    logger.error("发送消息时有错误发生: ", future1.cause());
                }
            });
        } catch (InterruptedException e) {
            unprocessedRequests.remove(rpcRequest.getRequestId());
            logger.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return resultFuture;
    }

}
