
package com.admin.service.config.impl;

import com.admin.dao.config.ConfigDao;
import com.admin.service.config.ConfigService;
import com.admin.util.ParameterMap;
import com.alibaba.druid.util.StringUtils;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.system.Const;
import com.admin.entity.system.User;
import com.admin.service.redis.RedisService;
import com.sys.domain.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional(readOnly = true)
public class ConfigServiceImpl implements ConfigService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private ConfigDao configMapper;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RedisService redisService;
	
	
	@Transactional(readOnly = false)
	 public  int deleteByPrimaryKey(java.lang.Long configId){
		 return  configMapper.deleteByPrimaryKey(configId);
	 }
	
	@Transactional(readOnly = false)
	 public   int insert(ParameterMap pm){
		 return configMapper.insert(pm);
	 }
	
	@Transactional(readOnly = false)
	 public  int insertSelective(ParameterMap pm){
		 return configMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(java.lang.Long configId){
		 return configMapper.selectByPrimaryKey(configId);
	 }
	 
	@Transactional(readOnly = false)
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  configMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional(readOnly = false)
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  configMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional(rollbackFor=Exception.class)  
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.configMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.configMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional(rollbackFor=Exception.class)
	public boolean deleteConfig(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			if(id != null && id.length > 0){
				for(String configId:id){
					//删除缓存
					ParameterMap parame = new ParameterMap();
					parame.put("id",configId);
					ParameterMap config = this.getByCondition(parame);
					System.out.println("config======================"+config);
					if(config != null && !StringUtils.isEmpty(config.getString("id"))){
						delCacheKey(config);
					}
				}
			}
			flag = this.configMapper.deleteByIds(id)> 0 ? true : false;

		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.configMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.configMapper.getByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.configMapper.getByConditionCount(pm);
	}

	@Transactional(readOnly = false)
	public Object add(ParameterMap pm,HttpSession session) throws Exception {
		try {
			if(StringUtils.isEmpty(pm.getString("id"))){
				pm.remove("id");
			}
			cacheConfig(pm);
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
			cacheConfig(pm);
			this.updateByPrimaryKeySelective(pm);
			return ResponseModel.getModel(ResultEnum.SUCCESS, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}

	//放入缓存
	public void cacheConfig(ParameterMap pm){
		String redisKey = String.format("CONFIG|%s|%s",pm.getString("configType"),pm.getString("configName"));
		Config config = new Config();
		config.setConfigType(pm.getString("configType"));
		config.setConfigName(pm.getString("configName"));
		config.setConfigValue(pm.getString("configValue"));
		config.setConfigDesc(pm.getString("configDesc"));
		redisService.stringSetObject(redisKey,config);
	}

	//删除缓存
	public void delCacheKey(ParameterMap pm){
		String redisKey = String.format("CONFIG|%s|%s",pm.getString("config_type"),pm.getString("config_name"));
		redisService.deleteFromRedis(redisKey);
	}

	@Transactional(readOnly = false)
	public Object del(ParameterMap pm) throws Exception {
		boolean isdel = false;
		try {
			String ids = pm.getString("id");
			isdel = this.deleteConfig(ids);
			return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}

}
