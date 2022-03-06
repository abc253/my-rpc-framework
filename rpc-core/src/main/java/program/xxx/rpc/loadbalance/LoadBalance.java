package program.xxx.rpc.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

public interface LoadBalance {
    Instance select(List<Instance> instances);
}
