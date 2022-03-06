package program.xxx.rpc.entity;

import lombok.*;

import java.io.Serializable;

/**
 * 服务端需要哪些信息，才能唯一确定服务端需要调用的接口的方法呢？
 *
 * 待调用接口的名称
 * 待调用方法名称
 * 待调用方法的参数（由于方法重载的缘故）
 * 待调用方法的参数类型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RpcRequest implements Serializable {
    /**
     * 请求号
     */
    private String requestId;
    /**
     * 待调用接口名称
     */
    private String interfaceName;
    /**
     * 待调用方法名称
     */
    private String methodName;
    /**
     * 调用方法的参数
     */
    private Object[] parameters;
    /**
     * 调用方法的参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 是否是心跳包
     */
    private Boolean heartBeat;

}
