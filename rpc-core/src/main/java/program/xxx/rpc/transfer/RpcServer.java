package program.xxx.rpc.transfer;

public interface RpcServer {
    void start(int port);

    default <T> void publishService(T service, Class<T> serviceClass) {
    }
}
