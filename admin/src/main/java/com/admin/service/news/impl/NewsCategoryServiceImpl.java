
package com.admin.service.news.impl;

import java.util.List;

import com.admin.dao.news.NewsCategoryDao;
import com.admin.service.news.NewsCategoryService;
import com.admin.util.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * service
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional(readOnly = true)
public class NewsCategoryServiceImpl implements NewsCategoryService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private NewsCategoryDao newsCategoryMapper;
	
	
	@Transactional(readOnly = false)
	 public  int deleteByPrimaryKey(java.lang.Long newsCategoryId){
		 return  newsCategoryMapper.deleteByPrimaryKey(newsCategoryId);
	 }
	
	@Transactional(readOnly = false)
	 public   int insert(ParameterMap pm){
		 return newsCategoryMapper.insert(pm);
	 }
	
	@Transactional(readOnly = false)
	 public  int insertSelective(ParameterMap pm){
		 return newsCategoryMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(java.lang.Long newsCategoryId){
		 return newsCategoryMapper.selectByPrimaryKey(newsCategoryId);
	 }
	 
	@Transactional(readOnly = false)
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  newsCategoryMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional(readOnly = false)
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  newsCategoryMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional(rollbackFor=Exception.class)  
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.newsCategoryMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.newsCategoryMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional(rollbackFor=Exception.class)
	public boolean deleteNewsCategory(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.newsCategoryMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.newsCategoryMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.newsCategoryMapper.getByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.newsCategoryMapper.getByConditionCount(pm);
	}
	
}
