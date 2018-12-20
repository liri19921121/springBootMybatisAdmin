package com.admin.config;

/**
 * 语言常量类
 */
public class LangName {
    //中文
    public final static String ZH_CN="zh_CN";
    //繁体
    public final static String ZH_TW="zh_TW";
    //英文
    public final static String EN_US="en_US";
    //韩文
    public final static String KO_KR="ko_KR";
    //日文
    public final static String JA_JP="ja_JP";

    public static String getChineseName(String temp){
        if (temp.equals("zh_CN")){
            return "中文";
        }else if (temp.equals("zh_TW")){
            return "繁体";
        }else if (temp.equals("en_US")){
            return "英文";
        }else if (temp.equals("ko_KR")){
            return "韩文";
        }else if (temp.equals("ja_JP")){
            return "日文";
        }else{
            return "error";
        }
    }


}
