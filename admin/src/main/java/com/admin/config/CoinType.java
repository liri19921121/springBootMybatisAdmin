package com.admin.config;

public enum CoinType {
    // 详情表的 type
    FREE("free","自由积分"),
    LOCK("lock","锁定积分"),
    ZCB("zcb","众筹币"),
    PPTR("pptr","PPTR"),
    ;
    private String key;
    private String desc;

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    private CoinType(String key, String desc){
        this.desc=desc;
        this.key=key;
    }
}
