
package com.sys.domain;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;


/**
 * devEntity
 * @author rstyro
 * @version v1.0
 */


/**
 * if the table has 'creater_code','creater_name','create_time' columns ,  you can write 
 *  the class-head as this ' public class Config extends CommonInfoModel implements java.io.Serializable  '
 */
public class Config implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
    /**
     * id       db_column: id 
     */ 	
	
	private Long id;
    /**
     * 配置类型       db_column: config_type 
     */ 	
	
	private String configType;
    /**
     * 配置项名称       db_column: config_name 
     */ 	
	
	private String configName;
    /**
     * 配置项值       db_column: config_value 
     */ 	
	
	private String configValue;
    /**
     * 配置项描述       db_column: config_desc 
     */ 	
	
	private String configDesc;
    /**
     * isDeleted       db_column: is_deleted 
     */ 	
	
	private String isDeleted;
    /**
     * createTime       db_column: create_time 
     */ 	
	
	private Date createTime;
    /**
     * createBy       db_column: create_by 
     */ 	
	
	private Long createBy;
    /**
     * modifyTime       db_column: modify_time 
     */ 	
	
	private Date modifyTime;
    /**
     * modifyBy       db_column: modify_by 
     */ 	
	
	private Long modifyBy;
	//columns END


	public Config(){
		
	}

	public Config(
		Long id
	){
		this.id = id;
	}

	
	
			
	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
	
//			
	public String getConfigType() {
		return this.configType;
	}
	
	public void setConfigType(String value) {
		this.configType = value;
	}
	
//			
	public String getConfigName() {
		return this.configName;
	}
	
	public void setConfigName(String value) {
		this.configName = value;
	}
	
//			
	public String getConfigValue() {
		return this.configValue;
	}
	
	public void setConfigValue(String value) {
		this.configValue = value;
	}
	
//			
	public String getConfigDesc() {
		return this.configDesc;
	}
	
	public void setConfigDesc(String value) {
		this.configDesc = value;
	}
	
//			
	public String getIsDeleted() {
		return this.isDeleted;
	}
	
	public void setIsDeleted(String value) {
		this.isDeleted = value;
	}
	
//			
//
//	public String getCreateTimeString() {
//		if(getCreateTime()==null){
//			return "";
//		}
//		return DateUtils.getDateString(getCreateTime(), FORMAT_CREATE_TIME);
//	}
//	public void setCreateTimeString(String value) {
//		if(!StringUtils.isBlank(value)){
//			setCreateTime(DateUtils.stringToDate(value, FORMAT_CREATE_TIME));
//		}
//	}
//	
//			
	public Date getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTime(Date value) {
		this.createTime = value;
	}
	
//			
	public Long getCreateBy() {
		return this.createBy;
	}
	
	public void setCreateBy(Long value) {
		this.createBy = value;
	}
	
//			
//
//	public String getModifyTimeString() {
//		if(getModifyTime()==null){
//			return "";
//		}
//		return DateUtils.getDateString(getModifyTime(), FORMAT_MODIFY_TIME);
//	}
//	public void setModifyTimeString(String value) {
//		if(!StringUtils.isBlank(value)){
//			setModifyTime(DateUtils.stringToDate(value, FORMAT_MODIFY_TIME));
//		}
//	}
//	
//			
	public Date getModifyTime() {
		return this.modifyTime;
	}
	
	public void setModifyTime(Date value) {
		this.modifyTime = value;
	}
	
//			
	public Long getModifyBy() {
		return this.modifyBy;
	}
	
	public void setModifyBy(Long value) {
		this.modifyBy = value;
	}
	

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("ConfigType",getConfigType())
			.append("ConfigName",getConfigName())
			.append("ConfigValue",getConfigValue())
			.append("ConfigDesc",getConfigDesc())
			.append("IsDeleted",getIsDeleted())
			.append("CreateTime",getCreateTime())
			.append("CreateBy",getCreateBy())
			.append("ModifyTime",getModifyTime())
			.append("ModifyBy",getModifyBy())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Config == false) return false;
		if(this == obj) return true;
		Config other = (Config)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
}

