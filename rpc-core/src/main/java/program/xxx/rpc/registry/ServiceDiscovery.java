package program.xxx.rpc.registry;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {
    /**
     * 发现服务
     * @param serviceName
     * @return
     */
    InetSocketAddress lookupService(String serviceName);
}
