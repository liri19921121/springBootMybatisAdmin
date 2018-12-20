package com.admin.entity;

public enum MoneyType {
    // 账户详情表的 type
    TRANSFER(1,"transfer","转账，对冲"),
    REPEAT(2,"repeat","复投"),
    PLUCK(3,"pluck","采集"),
    RELEASE(4,"release","系统释放"),
    RECHARGE(5,"recharge","充值"),
    RAISE(6,"raise","众筹"),
    TRANSFER_INCOME(7,"transfer_income","转账收益"),
    REPEAT_INCOME(8,"repeat_income","复投收益"),
    UMBRELLA_TRANSFER_INCOME(9,"umberlla_transfer_income","伞下转账收益"),
    UMBRELLA_REPEAT_INCOME(10,"umberlla_repeat_income","伞下复投收益"),
    REGISTER_INCOME(11,"register_income","注册获得的奖励"),
    REVIEW(12,"review","审核通知"),
    ;
    private int value;
    private String key;
    private String desc;

    public int getValue() {
        return value;
    }
    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    private MoneyType(int value,String key, String desc){
        this.value=value;
        this.desc=desc;
        this.key=key;
    }
}
