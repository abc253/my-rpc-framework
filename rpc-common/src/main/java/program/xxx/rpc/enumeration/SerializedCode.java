package program.xxx.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SerializedCode {
    KRYO(0),
    JSON(1),
    HESSIAN(2);

    private final int code;

}
