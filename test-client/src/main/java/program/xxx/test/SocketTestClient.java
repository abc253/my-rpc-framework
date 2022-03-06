package program.xxx.test;

import program.xxx.rpc.api.HelloObject;
import program.xxx.rpc.api.HelloService;
import program.xxx.rpc.transfer.RpcClientProxy;
import program.xxx.rpc.transfer.socket.client.SocketRpcClient;

public class SocketTestClient {
    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy = new RpcClientProxy(new SocketRpcClient(9000,"127.0.0.1"));
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new HelloObject(94, "您好"));
        System.out.println(hello);
    }
}
