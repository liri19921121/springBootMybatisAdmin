
package com.admin.entity.raiseCoin;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;


/**
 * devEntity
 * @author rstyro
 * @version v1.0
 */


/**
 * if the table has 'creater_code','creater_name','create_time' columns ,  you can write 
 *  the class-head as this ' public class CoinPublishDetail extends CommonInfoModel implements java.io.Serializable  '
 */
public class CoinPublishDetail implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	


    /**
     * id       db_column: id 
     */ 	
	
	private Long id;
    /**
     * 编号       db_column: code 
     */ 	
	
	private String code;
    /**
     * 描述       db_column: depict 
     */ 	
	
	private String depict;
    /**
     * 众筹币名称       db_column: name 
     */ 	
	
	private String name;
    /**
     * 众筹币缩写名字       db_column: abbreviation_name 
     */ 	
	
	private String abbreviationName;
    /**
     * 发布数量       db_column: release_quantity 
     */ 	
	
	private BigDecimal releaseQuantity;
    /**
     * 单价       db_column: price 
     */ 	
	
	private BigDecimal price;
    /**
     * 状态，0预热中，1认购中，2已停止       db_column: state 
     */ 	
	
	private Byte state;
    /**
     * 发布时间       db_column: publish_time 
     */ 	
	
	private Date publishTime;
    /**
     * 认购数量       db_column: subscription_num 
     */ 	
	
	private BigDecimal subscriptionNum;
    /**
     * 认购用户数       db_column: user_num 
     */ 	
	
	private Long userNum;
    /**
     * 附件图片id       db_column: img_id 
     */ 	
	
	private Integer imgId;
    /**
     * 认购开始时间       db_column: publish_begin_time 
     */ 	
	
	private Date publishBeginTime;
    /**
     * 认购结束时间       db_column: publish_end_time 
     */ 	
	
	private Date publishEndTime;
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
    /**
     * isDeleted       db_column: is_deleted 
     */ 	
	
	private String isDeleted;
    /**
     * 完整数量       db_column: full_quantity 
     */ 	
	
	private BigDecimal fullQuantity;
	//columns END


	public CoinPublishDetail(){
		
	}

	public CoinPublishDetail(
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
	public String getCode() {
		return this.code;
	}
	
	public void setCode(String value) {
		this.code = value;
	}
	
//			
	public String getDepict() {
		return this.depict;
	}
	
	public void setDepict(String value) {
		this.depict = value;
	}
	
//			
	public String getName() {
		return this.name;
	}
	
	public void setName(String value) {
		this.name = value;
	}
	
//			
	public String getAbbreviationName() {
		return this.abbreviationName;
	}
	
	public void setAbbreviationName(String value) {
		this.abbreviationName = value;
	}
	
//			
	public BigDecimal getReleaseQuantity() {
		return this.releaseQuantity;
	}
	
	public void setReleaseQuantity(BigDecimal value) {
		this.releaseQuantity = value;
	}
	
//			
	public BigDecimal getPrice() {
		return this.price;
	}
	
	public void setPrice(BigDecimal value) {
		this.price = value;
	}
	
//			
	public Byte getState() {
		return this.state;
	}
	
	public void setState(Byte value) {
		this.state = value;
	}
	
//			
//
//	public String getPublishTimeString() {
//		if(getPublishTime()==null){
//			return "";
//		}
//		return DateUtils.getDateString(getPublishTime(), FORMAT_PUBLISH_TIME);
//	}
//	public void setPublishTimeString(String value) {
//		if(!StringUtils.isBlank(value)){
//			setPublishTime(DateUtils.stringToDate(value, FORMAT_PUBLISH_TIME));
//		}
//	}
//	
//			
	public Date getPublishTime() {
		return this.publishTime;
	}
	
	public void setPublishTime(Date value) {
		this.publishTime = value;
	}
	
//			
	public BigDecimal getSubscriptionNum() {
		return this.subscriptionNum;
	}
	
	public void setSubscriptionNum(BigDecimal value) {
		this.subscriptionNum = value;
	}
	
//			
	public Long getUserNum() {
		return this.userNum;
	}
	
	public void setUserNum(Long value) {
		this.userNum = value;
	}
	
//			
	public Integer getImgId() {
		return this.imgId;
	}
	
	public void setImgId(Integer value) {
		this.imgId = value;
	}
	
//			
//
//	public String getPublishBeginTimeString() {
//		if(getPublishBeginTime()==null){
//			return "";
//		}
//		return DateUtils.getDateString(getPublishBeginTime(), FORMAT_PUBLISH_BEGIN_TIME);
//	}
//	public void setPublishBeginTimeString(String value) {
//		if(!StringUtils.isBlank(value)){
//			setPublishBeginTime(DateUtils.stringToDate(value, FORMAT_PUBLISH_BEGIN_TIME));
//		}
//	}
//	
//			
	public Date getPublishBeginTime() {
		return this.publishBeginTime;
	}
	
	public void setPublishBeginTime(Date value) {
		this.publishBeginTime = value;
	}
	
//			
//
//	public String getPublishEndTimeString() {
//		if(getPublishEndTime()==null){
//			return "";
//		}
//		return DateUtils.getDateString(getPublishEndTime(), FORMAT_PUBLISH_END_TIME);
//	}
//	public void setPublishEndTimeString(String value) {
//		if(!StringUtils.isBlank(value)){
//			setPublishEndTime(DateUtils.stringToDate(value, FORMAT_PUBLISH_END_TIME));
//		}
//	}
//	
//			
	public Date getPublishEndTime() {
		return this.publishEndTime;
	}
	
	public void setPublishEndTime(Date value) {
		this.publishEndTime = value;
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
	
//			
	public String getIsDeleted() {
		return this.isDeleted;
	}
	
	public void setIsDeleted(String value) {
		this.isDeleted = value;
	}
	
//			
	public BigDecimal getFullQuantity() {
		return this.fullQuantity;
	}
	
	public void setFullQuantity(BigDecimal value) {
		this.fullQuantity = value;
	}
	

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("Code",getCode())
			.append("Depict",getDepict())
			.append("Name",getName())
			.append("AbbreviationName",getAbbreviationName())
			.append("ReleaseQuantity",getReleaseQuantity())
			.append("Price",getPrice())
			.append("State",getState())
			.append("PublishTime",getPublishTime())
			.append("SubscriptionNum",getSubscriptionNum())
			.append("UserNum",getUserNum())
			.append("ImgId",getImgId())
			.append("PublishBeginTime",getPublishBeginTime())
			.append("PublishEndTime",getPublishEndTime())
			.append("CreateTime",getCreateTime())
			.append("CreateBy",getCreateBy())
			.append("ModifyTime",getModifyTime())
			.append("ModifyBy",getModifyBy())
			.append("IsDeleted",getIsDeleted())
			.append("FullQuantity",getFullQuantity())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof CoinPublishDetail == false) return false;
		if(this == obj) return true;
		CoinPublishDetail other = (CoinPublishDetail)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
}

