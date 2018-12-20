
package com.admin.service.appuser.impl;

import com.admin.config.ConfigName;
import com.admin.dao.appuser.AppUserDao;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.system.Const;
import com.admin.entity.system.User;
import com.admin.service.appuser.AppUserService;
import com.admin.service.config.ConfigService;
import com.admin.util.EncryptUtil;
import com.admin.util.ParameterMap;
import com.alibaba.druid.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional(readOnly = true)
public class AppUserServiceImpl implements AppUserService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private AppUserDao userMapper;

	@Autowired
	private ConfigService configService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Transactional(readOnly = false)
	 public  int deleteByPrimaryKey(java.lang.Long userId){
		 return  userMapper.deleteByPrimaryKey(userId);
	 }
	
	@Transactional(readOnly = false)
	 public   int insert(ParameterMap pm){
		 return userMapper.insert(pm);
	 }
	
	@Transactional(readOnly = false)
	 public  int insertSelective(ParameterMap pm){
		 return userMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(java.lang.Long userId){
		 return userMapper.selectByPrimaryKey(userId);
	 }
	 
	@Transactional(readOnly = false)
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  userMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional(readOnly = false)
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  userMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional(rollbackFor=Exception.class)  
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.userMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.userMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional(rollbackFor=Exception.class)
	public boolean deleteUser(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.userMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.userMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.userMapper.getByConditionList(argmap);
	}

	public List<ParameterMap> getAppUserByConditionList(ParameterMap argmap) throws Exception {


		return this.userMapper.getAppUserByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.userMapper.getByConditionCount(pm);
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

			ParameterMap map = new ParameterMap();
			map.put("configName", ConfigName.VIP_BOUND.getKey());
			Long vipBound = Long.valueOf(configService.getByCondition(map).get("config_value").toString());
			map.clear();
			map.put("vipBound",vipBound);
			map.put("userId",pm.getString("id").toString());
			detail = this.getDeteilByUserId(map);

			/*detail = this.selectByPrimaryKey(Long.valueOf(pm.getString("id")));*/
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

			ParameterMap map = selectByPrimaryKey(Long.valueOf(pm.get("id").toString()));

			ParameterMap pm2 = new ParameterMap();
			if (pm.get("password") != null && !pm.get("password").toString().equals("")){
				if (map.get("password") == null){
					String password = EncryptUtil.sha256Encrypt(pm.get("password").toString());
					pm2.put("password",password);
				}else{
					if (!map.get("password").toString().equals(pm.get("password").toString())){
						String password = EncryptUtil.sha256Encrypt(pm.get("password").toString());
						pm2.put("password",password);
					}
				}

			}
			if (pm.get("dealPassword") != null && !pm.get("dealPassword").toString().equals("")){
				if (map.get("deal_password") == null){
					String dealPassword = EncryptUtil.sha256Encrypt(pm.get("dealPassword").toString());
					pm2.put("dealPassword",dealPassword);
				}else{
					if (!map.get("deal_password").toString().equals(pm.get("dealPassword").toString())){
						String dealPassword = EncryptUtil.sha256Encrypt(pm.get("dealPassword").toString());
						pm2.put("dealPassword",dealPassword);
					}
				}
			}
			if (pm.get("authGoogle") != null && !pm.get("authGoogle").toString().equals("")){
				System.out.println(pm.get("authGoogle")+"-----------");
				pm2.put("authGoogle",pm.get("authGoogle"));
			}
			pm2.put("id",pm.get("id"));
			pm2.put("modifyTime", LocalDateTime.now());
			pm2.put("modifyBy",user.getUserId());
			this.updateByPrimaryKeySelective(pm2);
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
			isdel = this.deleteUser(ids);
			return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}

	@Transactional(readOnly = false)
	public ParameterMap getDeteilByUserId(ParameterMap pm) throws Exception{
		return this.userMapper.getDeteilByUserId(pm);
	}

}
