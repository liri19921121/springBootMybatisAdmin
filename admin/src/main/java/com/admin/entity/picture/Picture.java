
package com.admin.entity.picture;
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
 *  the class-head as this ' public class Picture extends CommonInfoModel implements java.io.Serializable  '
 */
public class Picture implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	


    /**
     * picId       db_column: pic_id 
     */ 	
	
	private Long picId;
    /**
     * 上传图片的类型，       db_column: pic_type
     */

	private String picType;
    /**
     * 上次上次时的文件名       db_column: pic_name
     */

	private String picName;
    /**
     * 图片的相对路径       db_column: pic_path
     */

	private String picPath;
    /**
     * n       db_column: is_deleted
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
	//columns END

	private Long objectId;

	public Picture(){

	}

	public Picture(
		Long picId
	){
		this.picId = picId;
	}


	public void setObjectId(Long value) {
		this.objectId = value;
	}

	public Long getObjectId() {
		return this.objectId;
	}

	public void setPicId(Long value) {
		this.picId = value;
	}

	public Long getPicId() {
		return this.picId;
	}

//
	public String getPicType() {
		return this.picType;
	}

	public void setPicType(String value) {
		this.picType = value;
	}

//
	public String getPicName() {
		return this.picName;
	}

	public void setPicName(String value) {
		this.picName = value;
	}

//
	public String getPicPath() {
		return this.picPath;
	}

	public void setPicPath(String value) {
		this.picPath = value;
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
	

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("PicId",getPicId())
			.append("PicType",getPicType())
			.append("PicName",getPicName())
			.append("PicPath",getPicPath())
			.append("IsDeleted",getIsDeleted())
			.append("CreateTime",getCreateTime())
			.append("CreateBy",getCreateBy())
				.append("ObjectId",getObjectId())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getPicId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Picture == false) return false;
		if(this == obj) return true;
		Picture other = (Picture)obj;
		return new EqualsBuilder()
			.append(getPicId(),other.getPicId())
			.isEquals();
	}
}

