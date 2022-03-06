package program.xxx.test;

import program.xxx.rpc.api.HelloService;
import program.xxx.rpc.provider.ServiceProviderImpl;
import program.xxx.rpc.provider.ServiceProvider;
import program.xxx.rpc.transfer.socket.server.SocketRpcServer;

public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        // 创建服务注册的实现类注册服务，RpcServer注入服务，然后启动服务
        ServiceProvider serviceProvider = new ServiceProviderImpl();
        serviceProvider.register(helloService);
        SocketRpcServer socketRpcServer = new SocketRpcServer(serviceProvider);
        socketRpcServer.start(9000);
    }
}
