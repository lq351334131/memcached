package com.etocrm.memcached.pojo;

public enum ErrorCode {

    /**
     * key 为null 或 空字符串
     */
    NULL_KEY(10001),

    /**
     * key长度过长
     */
    EXTRA_LONG(10002),

    /**
     * key为中文或其他不支持的类型
     */
    ENCODE_ERROR(10003),

    /**
     * value不能为null
     */
    NULL_VALUE(10004),

    /**
     * 系统连接异常
     */
    CONNECTION_ERROR(10005),

    /**
     * add 方法key已存在
     */
    KEY_EXITS(10006),

    /**
     * replace key 不存在
     */
    KEY_NOTEXITS(10007),

    /**
     * 增量不正确
     */
    INCREMENT_ERROR(10008),

    /**
     *
     */
    VALUE_TYPE_ERROR(10009),

    /**
     * 成功
     */
    SUCCESS(0);

    public int code;

    ErrorCode(int code) {
        this.code = code;
    }

}
