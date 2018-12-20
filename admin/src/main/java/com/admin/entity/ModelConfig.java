package com.admin.entity;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
public class ModelConfig {
    private String title;
    private String content;
    public ModelConfig(String configFile){
        Properties props = new Properties();
        InputStream inStream = this.getClass().getResourceAsStream(configFile);
        if(inStream == null){
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            props.load(inStream);
            this.title = new String(props.getProperty("message.title").getBytes("ISO-8859-1"), "utf-8");
            if(StringUtils.isNotBlank(this.title)) this.title = this.title.trim();
            this.content = new String(props.getProperty("message.content").getBytes("ISO-8859-1"), "utf-8");
            if(StringUtils.isNotBlank(this.content)) this.content = this.content.trim();
            inStream.close();
        } catch (IOException e) {
            try {
                throw new Exception("load wx.properties error,class, can't find wx.properties");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}

