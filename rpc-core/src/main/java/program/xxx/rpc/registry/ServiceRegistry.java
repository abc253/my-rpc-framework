package program.xxx.rpc.registry;

import java.net.InetSocketAddress;

public interface ServiceRegistry {
    /**
     * 服务注册
     *
     * @param serviceName       服务名称
     * @param inetSocketAddress 服务地址
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);
}
