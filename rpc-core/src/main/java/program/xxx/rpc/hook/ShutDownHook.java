package program.xxx.rpc.hook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import program.xxx.rpc.factory.ThreadPoolFactory;
import program.xxx.rpc.util.NacosUtil;

import java.util.concurrent.Executors;

public class ShutDownHook {

    private static final Logger logger = LoggerFactory.getLogger(ShutDownHook.class);

    private static final ShutDownHook shutdownHook = new ShutDownHook();

    public static ShutDownHook getShutdownHook() {
        return shutdownHook;
    }

    public void addClearAllHook() {
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            ThreadPoolFactory.shutDownAll();
        }));
    }

}
