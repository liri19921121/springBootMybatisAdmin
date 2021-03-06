package com.admin.config;

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


    //计划表 source
    PLAN_TRANSFER(101,"transfer","转账计划"),
    PLAN_REPEAT(102,"repeat","复投计划"),
    PLAN_REPEAT_AWARD(1020,"repeat_award","复投计划"),
    PLAN_PLUCK(103,"pluck","采集计划"),
    PLAN_RELEASE(104,"release","每日释放计划"),
    PLAN_RECHARGE(105,"recharge","充值计划"),
    PLAN_RAISE(106,"raise","众筹计划"),
    PLAN_SELL_PUBLISH(107,"sell_publish","买卖广告"),
    PLAN_SELL_DETAIL(108,"sell_detail","买卖交易"),
    PLAN_STATISTICS(109,"statistics","统计入账"),
    PLAN_QUICKER_RELEASE(110,"quicker_release","加速释放"),
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

    private MoneyType(int value, String key, String desc){
        this.desc=desc;
        this.key=key;
        this.value=value;
    }

    public static String getKeyByValue(int value){
        MoneyType[] moneyTypes = MoneyType.values();
        for(MoneyType moneyType:moneyTypes) {
            if (value == moneyType.getValue()) {
                return moneyType.getKey();
            }
        }
        return "";
    }
}
