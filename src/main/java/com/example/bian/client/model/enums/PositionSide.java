package com.example.bian.client.model.enums;

/**
 * @author : wangwanlu
 * @since : 2020/3/25, Wed
 **/
public enum PositionSide {

    BOTH("BOTH"),
    NET("NET"),//ok的单向

    SHORT("SHORT"),

    LONG("LONG"),
    ;

    private final String code;

    PositionSide(String side) {
        this.code = side;
    }

    @Override
    public String toString() {
        return code;
    }
}
