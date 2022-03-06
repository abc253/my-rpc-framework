package program.xxx.test;

import program.xxx.rpc.api.HelloService;
import program.xxx.rpc.serializer.CommonSerializer;
import program.xxx.rpc.transfer.netty.server.NettyRpcServer;

public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyRpcServer server = new NettyRpcServer("127.0.0.1", 9999, 0);
        server.publishService(helloService, HelloService.class);
    }
}
