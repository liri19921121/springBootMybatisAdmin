
package com.admin.service.relationUmbrella.impl;

import com.admin.util.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.druid.util.StringUtils;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.system.Const;
import com.admin.entity.system.User;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;

import com.admin.dao.relationUmbrella.RelationUmbrellaDao;
import com.admin.service.relationUmbrella.RelationUmbrellaService;

/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional(readOnly = true)
public class RelationUmbrellaServiceImpl implements RelationUmbrellaService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private RelationUmbrellaDao relationUmbrellaMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Transactional(readOnly = false)
	 public  int deleteByPrimaryKey(Long relationUmbrellaId){
		 return  relationUmbrellaMapper.deleteByPrimaryKey(relationUmbrellaId);
	 }
	
	@Transactional(readOnly = false)
	 public   int insert(ParameterMap pm){
		 return relationUmbrellaMapper.insert(pm);
	 }
	
	@Transactional(readOnly = false)
	 public  int insertSelective(ParameterMap pm){
		 return relationUmbrellaMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(Long relationUmbrellaId){
		 return relationUmbrellaMapper.selectByPrimaryKey(relationUmbrellaId);
	 }
	 
	@Transactional(readOnly = false)
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  relationUmbrellaMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional(readOnly = false)
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  relationUmbrellaMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional(rollbackFor=Exception.class)  
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.relationUmbrellaMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.relationUmbrellaMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional(rollbackFor=Exception.class)
	public boolean deleteRelationUmbrella(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.relationUmbrellaMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.relationUmbrellaMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.relationUmbrellaMapper.getByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.relationUmbrellaMapper.getByConditionCount(pm);
	}

	public List<String> getUserIdByRecommendId(String userId) throws Exception{
		return this.relationUmbrellaMapper.getUserIdByRecommendId(userId);
	}

	public List<Long> getIds(ParameterMap pm) throws Exception{
		return this.relationUmbrellaMapper.getIds(pm);
	}
	
	@Transactional(readOnly = false)
	public Object add(ParameterMap pm,HttpSession session) throws Exception {
		try {
			if(StringUtils.isEmpty(pm.getString("id"))){
				pm.remove("id");
			}
			pm.put("createTime", LocalDateTime.now());
			pm.put("createBy", ((User) session.getAttribute(Const.SESSION_USER)).getUserId());
			this.insertSelective(pm);
			return ResponseModel.getModel(ResultEnum.SUCCESS, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}

	@Override
	public Object getDetail(ParameterMap pm) throws Exception {
		try {
			ParameterMap detail = null;
			detail = this.selectByPrimaryKey(Long.valueOf(pm.getString("id")));
			return ResponseModel.getModel(ResultEnum.SUCCESS, detail);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}

	@Transactional(readOnly = false)
	public Object edit(ParameterMap pm, HttpSession session) throws Exception {
		try {
			User user = (User) session.getAttribute(Const.SESSION_USER);
			pm.put("modifyTime", LocalDateTime.now());
			pm.put("modifyBy",user.getUserId());
			this.updateByPrimaryKeySelective(pm);
			return ResponseModel.getModel(ResultEnum.SUCCESS, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}

	@Transactional(readOnly = false)
	public Object del(ParameterMap pm) throws Exception {
		boolean isdel = false;
		try {
			String ids = pm.getString("id");
			isdel = this.deleteRelationUmbrella(ids);
			return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}

	@Transactional(readOnly = false)
	public int updateByIds(ParameterMap pm) throws Exception{
		return this.relationUmbrellaMapper.updateByIds(pm);
	}

	public List<Long> getIdsByAreaId(ParameterMap pm) throws Exception{
		return this.relationUmbrellaMapper.getIdsByAreaId(pm);
	}
	
}
