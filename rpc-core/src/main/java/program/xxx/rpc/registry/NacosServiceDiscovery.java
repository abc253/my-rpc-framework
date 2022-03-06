package program.xxx.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import program.xxx.rpc.enumeration.RpcError;
import program.xxx.rpc.exception.RpcException;
import program.xxx.rpc.loadbalance.LoadBalance;
import program.xxx.rpc.loadbalance.RandomLoadBalancer;
import program.xxx.rpc.util.NacosUtil;

import java.net.InetSocketAddress;
import java.util.List;

public class NacosServiceDiscovery implements ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceDiscovery.class);

    private final LoadBalance loadBalance;

    public NacosServiceDiscovery(LoadBalance loadBalance) {
        if (loadBalance == null) this.loadBalance = new RandomLoadBalancer();
        else this.loadBalance = loadBalance;
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = NacosUtil.getAllInstance(serviceName);
            Instance instance = loadBalance.select(instances);
            return new InetSocketAddress(instance.getIp(),instance.getPort());
        } catch (NacosException e) {
            logger.error("服务发现失败: " + e);
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
    }
}
