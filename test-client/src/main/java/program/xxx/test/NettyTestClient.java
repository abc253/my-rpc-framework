package program.xxx.test;

import program.xxx.rpc.serializer.CommonSerializer;
import program.xxx.rpc.transfer.RpcClient;
import program.xxx.rpc.transfer.RpcClientProxy;
import program.xxx.rpc.api.HelloObject;
import program.xxx.rpc.api.HelloService;
import program.xxx.rpc.transfer.netty.client.NettyRpcClient;

public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyRpcClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
