package program.xxx.rpc.factory;

import java.util.HashMap;
import java.util.Map;

public class SingletonFactory {

    private static Map<Class,Object> objectMap = new HashMap<>();

    public static <T> T getInstance(Class<T> clazz) {
        Object instance = objectMap.get(clazz);
        synchronized (clazz) {
            if (instance == null) {
                try {
                    instance = clazz.newInstance();
                    objectMap.put(clazz,instance);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return clazz.cast(instance);
    }
}
