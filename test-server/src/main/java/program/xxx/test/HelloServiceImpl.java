package program.xxx.test;

import lombok.extern.slf4j.Slf4j;
import program.xxx.rpc.api.HelloObject;
import program.xxx.rpc.api.HelloService;

@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(HelloObject object) {
        log.info("接收到：{}", object.getMessage());
        return "这是调用的返回值，id=" + object.getId();
    }
}
