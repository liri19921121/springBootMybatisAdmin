
package com.admin.service.userExtendInfo.impl;

import com.admin.util.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.admin.dao.userExtendInfo.UserExtendInfoDao;
import com.admin.service.userExtendInfo.UserExtendInfoService;

/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional
public class UserExtendInfoServiceImpl implements UserExtendInfoService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private UserExtendInfoDao userExtendInfoMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${home.url}")
	public String homeUrl;

	@Value("${api.url}")
	public String apiUrl;
	
	
	@Transactional
	 public  int deleteByPrimaryKey(Long userExtendInfoId){
		 return  userExtendInfoMapper.deleteByPrimaryKey(userExtendInfoId);
	 }
	
	@Transactional
	 public   int insert(ParameterMap pm){
		 return userExtendInfoMapper.insert(pm);
	 }
	
	@Transactional
	 public  int insertSelective(ParameterMap pm){
		 return userExtendInfoMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(Long userExtendInfoId){
		 return userExtendInfoMapper.selectByPrimaryKey(userExtendInfoId);
	 }
	 
	@Transactional
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  userExtendInfoMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  userExtendInfoMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.userExtendInfoMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.userExtendInfoMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional
	public boolean deleteUserExtendInfo(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.userExtendInfoMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		ParameterMap map = this.userExtendInfoMapper.getByCondition(pm);
		String idPersonCardImg = (String) map.get("id_person_card_img");
		if(!StringUtils.isEmpty(idPersonCardImg)){
			map.put("id_person_card_img",apiUrl+map.get("id_person_card_img"));
		}
		String idCardReverseImg = (String) map.get("id_card_reverse_img");
		if(!StringUtils.isEmpty(idCardReverseImg)){
			map.put("id_card_reverse_img",apiUrl+map.get("id_card_reverse_img"));
		}
		String idCardPersonImg = (String) map.get("id_card_person_img");
		if(!StringUtils.isEmpty(idCardPersonImg)){
			map.put("id_card_person_img",apiUrl+map.get("id_card_person_img"));
		}
		return map;
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.userExtendInfoMapper.getByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.userExtendInfoMapper.getByConditionCount(pm);
	}
	
	@Transactional
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

	@Transactional
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

	@Transactional
	public Object del(ParameterMap pm) throws Exception {
		boolean isdel = false;
		try {
			String ids = pm.getString("id");
			isdel = this.deleteUserExtendInfo(ids);
			return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}
	
}
