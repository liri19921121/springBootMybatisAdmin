
package com.admin.service.userAccount;

import java.math.BigDecimal;
import java.util.List;

import com.admin.util.ParameterMap;

import javax.servlet.http.HttpSession;

/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

public interface UserAccountService {
	
        /**
         * 根据主键删除
         * @param userAccountId
         * @return
         */
     	public int deleteByPrimaryKey(java.lang.Long userAccountId) throws Exception;

	    /**
	     * 插入一条全新的记录
	     * @param record
	     * @return
	     */
     	public int insert(ParameterMap pm) throws Exception;

	    /**
	     * 插入一条全新的记录  （带字段选择）
	     * @param record
	     * @return
	     */
     	public int insertSelective(ParameterMap pm) throws Exception;

        /**
         * 根据主键查询记录
         * @param userAccountId
         * @return
         */
     	public ParameterMap selectByPrimaryKey(java.lang.Long userAccountId) throws Exception;

	    /**
	     * 更新记录（带字段）
	     * @param record
	     * @return
	     */
     	public int updateByPrimaryKeySelective(ParameterMap pm) throws Exception;

	    /**
	     * 更新记录（全字段）
	    * @param record
	     * @return
	     */
     	public int updateByPrimaryKey(ParameterMap pm) throws Exception;
     	
     	
     	/**
     	 * 保存或新增
     	 * @param record
     	 * @param operateType
     	 * @return
     	 * @throws Exception
     	 */
    	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception;
    	
    	/**
    	 * 根据 主键 
    	 * @author wzz
    	 * @param ids
    	 * @return
    	 * @throws Exception
    	 */
    	public boolean deleteUserAccount(String ids) throws Exception;
    	
       	/**
    	 * 根据条件查询 对象
    	 * @param argmap
    	 * @return
    	 */
	    public ParameterMap getByCondition(ParameterMap pm)throws Exception;
	    
	    
	    /**
	     * 根据条件查询 列表集合
	     * @param argmap
	     * @return
	     */
	    public List<ParameterMap> getByConditionList(ParameterMap pm)throws Exception;
	    /**
	     * 根据条件查询集合数量
	     * @param argmap
	     * @return
	     * @throws Exception
	     */
	    public long getByConditionCount(ParameterMap pm) throws Exception;

		public void updateAccount(String userId, String transUserId, String code, BigDecimal amount, String detailId, String state, String detailType) throws Exception;
	    
	    public Object add(ParameterMap pm, HttpSession session) throws  Exception;
	    public Object getDetail(ParameterMap pm) throws  Exception;
	    public Object edit(ParameterMap pm,HttpSession session) throws  Exception;
	    public Object del(ParameterMap pm) throws  Exception;
	    public List<ParameterMap> getListByCode(ParameterMap pm);
}
