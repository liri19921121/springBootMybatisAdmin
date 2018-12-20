package com.admin.config;

public enum ConfigName {
    VIP_BOUND("vip_bound","成为vip锁定积分界限"),
    BANNER_NUM("banner_num","前端banner图显示条数"),
    REPEAT_LEVER("repeat_lever","复投杠杆比例"),
    REPEAT_RECOMMEND_("repeat_recommend_","复投直推n级奖励,n是后面拼接的"),
    REPEAT_RECOMMEND_OTHER("repeat_recommend_other","复投直推后面级别奖励"),
    REPEAT_RECOMMEND_FIVES_EXTRA5_10("repeat_recommend_5_10","复投直推5-10额外奖励"),
    REPEAT_RECOMMEND_FIVES_EXTRA10_20("repeat_recommend_10_20","复投直推10-20额外级奖励"),
    REPEAT_RECOMMEND_FIVES_EXTRA20_30("repeat_recommend_20_30","复投直推20-30额外奖励"),
    REPEAT_RECOMMEND_FIVES_EXTRA_GT_30("repeat_recommend_gt_30","复投直推大于30额外奖励"),
    DAILY_RELEASE_LOCK_MONEY("daily_release_lock_money","每日锁定积分释放成自由积分的比例"),
    TRANSFER_VIP_("transfer_vip_","最近的VIP会员享受伞下所有用户余额互转（包含挂卖、消费、对冲）总额的 比例"),
    TRANSFER_FREE("transfer_free","转账获取的自由积分奖励 比例"),
    TRANSFER_LOCK("transfer_lock","转账获取的锁定积分奖励 比例"),
    ;
    private String key;
    private String desc;

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    private ConfigName(String key, String desc){
        this.desc=desc;
        this.key=key;
    }
}
