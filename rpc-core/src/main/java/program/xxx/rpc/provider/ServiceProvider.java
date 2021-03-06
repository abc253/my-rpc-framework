package program.xxx.rpc.provider;

public interface ServiceProvider {
    /**
     * 将一个服务注册进注册表
     *
     * @param service 待注册的服务实体
     * @param <T>     服务实体类
     */
    @Deprecated
    default <T> void register(T service) {

    }

    /**
     * 根据服务名称获取服务实体
     *
     * @param serviceName 服务名称
     * @return 服务实体
     */
    @Deprecated
    default Object getService(String serviceName) {
        return null;
    }

    <T> void addServiceProvider(T service, Class<T> serviceClass);

    Object getServiceProvider(String serviceName);


}
