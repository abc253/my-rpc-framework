package program.xxx.rpc.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalance {
    @Override
    public Instance select(List<Instance> instances) {
        // nextInt():生成一个介于[0, instances.size())的int型随机值
        return instances.get(new Random().nextInt(instances.size()));
    }
}
