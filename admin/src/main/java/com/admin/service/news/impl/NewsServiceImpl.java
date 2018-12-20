
package com.admin.service.news.impl;

import com.admin.dao.news.NewsDao;
import com.admin.service.news.NewsService;
import com.admin.util.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * service
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private NewsDao newsMapper;
	
	
	@Transactional(readOnly = false)
	 public  int deleteByPrimaryKey(java.lang.Long newsId){
		 return  newsMapper.deleteByPrimaryKey(newsId);
	 }
	
	@Transactional(readOnly = false)
	 public   int insert(ParameterMap pm){
		 return newsMapper.insert(pm);
	 }
	
	@Transactional(readOnly = false)
	 public  int insertSelective(ParameterMap pm){
		 return newsMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(java.lang.Long newsId){
		 return newsMapper.selectByPrimaryKey(newsId);
	 }
	 
	@Transactional(readOnly = false)
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  newsMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional(readOnly = false)
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  newsMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional(rollbackFor=Exception.class,readOnly = false)  
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.newsMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.newsMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional(rollbackFor=Exception.class,readOnly = false)
	public boolean deleteNew(Long id) throws Exception {
		boolean flag = false;
		flag = this.newsMapper.deleteByPrimaryKey(id)> 0 ? true : false;
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.newsMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.newsMapper.getByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.newsMapper.getByConditionCount(pm);
	}
	
}
