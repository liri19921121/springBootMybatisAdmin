
package com.admin.service.picture;

import com.admin.entity.picture.Picture;

import java.util.HashMap;
import java.util.List;


/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

public interface PictureService {
	
        /**
         * 根据主键删除
         * @param pictureId
         * @return
         */
     	public int deleteByPrimaryKey(Long pictureId) throws Exception;

	    /**
	     * 插入一条全新的记录
	     * @param record
	     * @return
	     */
     	public int insert(Picture record) throws Exception;

	    /**
	     * 插入一条全新的记录  （带字段选择）
	     * @param record
	     * @return
	     */
     	public int insertSelective(Picture record) throws Exception;

        /**
         * 根据主键查询记录
         * @param pictureId
         * @return
         */
     	public Picture selectByPrimaryKey(Long pictureId) throws Exception;

	    /**
	     * 更新记录（带字段）
	     * @param record
	     * @return
	     */
     	public int updateByPrimaryKeySelective(Picture record) throws Exception;

	    /**
	     * 更新记录（全字段）
	    * @param record
	     * @return
	     */
     	public int updateByPrimaryKey(Picture record) throws Exception;


     	/**
     	 * 保存或新增
     	 * @param record
     	 * @param operateType
     	 * @return
     	 * @throws Exception
     	 */
    	public boolean saveOrUpdate(Picture record, String operateType) throws Exception;

    	/**
    	 * 根据主键取对象
    	 * @author wzz
    	 * @param id
    	 * @return
    	 * @throws Exception
    	 */
    	public Picture getById(Long id) throws Exception;

    	/**
    	 * 根据 主键
    	 * @author wzz
    	 * @param ids
    	 * @return
    	 * @throws Exception
    	 */
    	public boolean deletePicture(String ids) throws Exception;

       	/**
    	 * 根据条件查询 对象
    	 * @param argmap
    	 * @return
    	 */
	    public Picture getByCondition(HashMap<String, Object> argmap)throws Exception;


	    /**
	     * 根据条件查询 列表集合
	     * @param argmap
	     * @return
	     */
	    public List<Picture> getByCondition4List(HashMap<String, Object> argmap)throws Exception;

	    public List<Picture> getByConditionList(HashMap<String, Object> argmap)throws Exception;
	    /**
	     * 根据条件查询集合数量
	     * @param argmap
	     * @return
	     * @throws Exception
	     */
	    public long getByCondition4Count(HashMap<String, Object> argmap) throws Exception;


}
