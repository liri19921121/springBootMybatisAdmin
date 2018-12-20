package com.admin.entity;

import com.admin.config.LangName;
import com.admin.entity.dto.MessageModel;
import org.springframework.util.StringUtils;

public class MessageFactory {
    private static MessageFactory messageFactory;
    private MessageFactory(){ }
    public MessageModel getModel(String lang,String type){
        ModelConfig config = null;
        MessageModel model = new MessageModel();
        if(StringUtils.isEmpty(lang)){
            lang = LangName.ZH_CN;
        }
        //审核通过对应的模板
        if(type.equals("2")){

            if(lang.equalsIgnoreCase(LangName.ZH_CN)){
                config = new ModelConfig(ConfigFile.FILE_ZH_CN);
            }else if(lang.equalsIgnoreCase(LangName.ZH_TW)){
                config = new ModelConfig(ConfigFile.FILE_ZH_TW);
            }else if(lang.equalsIgnoreCase(LangName.EN_US)){
                config = new ModelConfig(ConfigFile.FILE_EN_US);
            }
            //审核不通过对应的模板
        }else{
            if(lang.equalsIgnoreCase(LangName.ZH_CN)){
                config = new ModelConfig(ConfigFile.UNPASS_FILE_ZH_CN);
            }else if(lang.equalsIgnoreCase(LangName.ZH_TW)){
                config = new ModelConfig(ConfigFile.UNPASS_FILE_ZH_TW);
            }else if(lang.equalsIgnoreCase(LangName.EN_US)){
                config = new ModelConfig(ConfigFile.UNPASS_FILE_EN_US);
            }
        }
        model.setTitle(config.getTitle());
        model.setContent(config.getContent());
        return model;
    }

    public static synchronized MessageFactory getInstance(){
        if(messageFactory == null){
            messageFactory = new MessageFactory();
        }
        return messageFactory;
    }
}
