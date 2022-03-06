package program.xxx.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import program.xxx.rpc.enumeration.RpcError;
import program.xxx.rpc.exception.RpcException;
import program.xxx.rpc.provider.ServiceProvider;
import program.xxx.rpc.util.NacosUtil;

import java.net.InetSocketAddress;

public class NacosServiceProvider implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceProvider.class);

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName,inetSocketAddress);
        } catch (NacosException e) {
            logger.error("服务注册失败: " + e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }
}
