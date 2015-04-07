package pojo;

import java.io.Serializable;

/**
 * Created by gandy on 27.09.14.
 *
 */
public enum FlagsEnum implements Serializable {

    GET_DATA(1),
    LOG_IN(2),
    LOG_OUT(3),
    REG_USER(4),
    CHECK_CONNECTION(5);

    private int value;

    FlagsEnum(int i) {
        this.value = i;
    }
}
