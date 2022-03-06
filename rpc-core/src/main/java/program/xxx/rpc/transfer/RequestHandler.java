package program.xxx.rpc.transfer;

import com.alibaba.nacos.api.cmdb.pojo.PreservedEntityTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import program.xxx.rpc.entity.RpcRequest;
import program.xxx.rpc.entity.RpcResponse;
import program.xxx.rpc.enumeration.ResponseCode;
import program.xxx.rpc.provider.ServiceProvider;
import program.xxx.rpc.provider.ServiceProviderImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.alibaba.nacos.api.cmdb.pojo.PreservedEntityTypes.service;

public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RpcRequest rpcRequest) {
        Object service = RequestHandler.serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    public Object handle(RpcRequest rpcRequest, Object service) {
        return invokeTargetMethod(rpcRequest,service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            logger.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }
        return result;
    }
}
