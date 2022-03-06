package program.xxx.rpc.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import program.xxx.rpc.enumeration.RpcError;
import program.xxx.rpc.exception.RpcException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceProviderImpl implements ServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);

    private static final Map<String,Object> serverMap = new ConcurrentHashMap<>();
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    /**
     * 我们将服务名与提供服务的对象的对应关系保存在一个 ConcurrentHashMap 中，
     * 并且使用一个 Set 来保存当前有哪些对象已经被注册。
     * 在注册服务时，默认采用这个对象实现的接口的完整类名作为服务名，
     * 例如某个对象 A 实现了接口 X 和 Y，那么将 A 注册进去后，会有两个服务名 X 和 Y 对应于 A 对象。
     * 这种处理方式也就说明了某个接口只能有一个对象提供服务。
     *
     * 获得服务的对象就更简单了，直接去 Map 里查找就行了。
     */
    @Override
    public synchronized <T> void register(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if (registeredService.contains(serviceName)) return;
        registeredService.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class i : interfaces) {
            serverMap.put(i.getCanonicalName(),service);
        }
        logger.info("向接口: {} 注册服务: {}", interfaces, serviceName);
    }

    @Override
    public synchronized Object getService(String serviceName) {
        Object service = serverMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }

    @Override
    public <T> void addServiceProvider(T service, Class<T> serviceClass) {
        String serviceName = serviceClass.getCanonicalName();
        if (registeredService.contains(serviceName)) return;
        registeredService.add(serviceName);
        serverMap.put(serviceName,service);
        logger.info("向接口: {} 注册服务: {}", serviceClass.getCanonicalName(), service);
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serverMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
