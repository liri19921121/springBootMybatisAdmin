
package com.admin.service.news.impl;

import java.util.List;

import com.admin.dao.news.NewsContentDao;
import com.admin.util.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admin.service.news.NewsContentService;

/**
 * service
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional(readOnly = true)
public class NewsContentServiceImpl implements NewsContentService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private NewsContentDao newsContentMapper;
	
	
	@Transactional(readOnly = false)
	 public  int deleteByPrimaryKey(java.lang.Long newsContentId){
		 return  newsContentMapper.deleteByPrimaryKey(newsContentId);
	 }
	
	@Transactional(readOnly = false)
	 public   int insert(ParameterMap pm){
		 return newsContentMapper.insert(pm);
	 }
	
	@Transactional(readOnly = false)
	 public  int insertSelective(ParameterMap pm){
		 return newsContentMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(java.lang.Long newsContentId){
		 return newsContentMapper.selectByPrimaryKey(newsContentId);
	 }
	 
	@Transactional(readOnly = false)
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  newsContentMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional(readOnly = false)
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  newsContentMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional(rollbackFor=Exception.class)  
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.newsContentMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.newsContentMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional(rollbackFor=Exception.class)
	public boolean deleteNewsContent(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.newsContentMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.newsContentMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.newsContentMapper.getByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.newsContentMapper.getByConditionCount(pm);
	}
	
}
