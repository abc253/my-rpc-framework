package program.xxx.rpc.transfer;

import program.xxx.rpc.entity.RpcRequest;

public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
