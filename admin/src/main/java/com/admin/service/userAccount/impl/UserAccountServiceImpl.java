
package com.admin.service.userAccount.impl;

import com.admin.dao.userAccount.UserAccountDao;
import com.admin.service.userAccountDetail.UserAccountDetailService;
import com.admin.util.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.druid.util.StringUtils;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.system.Const;
import com.admin.entity.system.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;

import com.admin.service.userAccount.UserAccountService;

/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private UserAccountDao userAccountMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserAccountDetailService userAccountDetailService;
	
	
	@Transactional
	 public  int deleteByPrimaryKey(java.lang.Long userAccountId){
		 return  userAccountMapper.deleteByPrimaryKey(userAccountId);
	 }
	
	@Transactional
	 public   int insert(ParameterMap pm){
		 return userAccountMapper.insert(pm);
	 }
	
	@Transactional
	 public  int insertSelective(ParameterMap pm){
		 return userAccountMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(java.lang.Long userAccountId){
		 return userAccountMapper.selectByPrimaryKey(userAccountId);
	 }
	 
	@Transactional
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  userAccountMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  userAccountMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.userAccountMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.userAccountMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional
	public boolean deleteUserAccount(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.userAccountMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.userAccountMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.userAccountMapper.getByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.userAccountMapper.getByConditionCount(pm);
	}

	public void updateAccount(String userId, String transUserId, String code, BigDecimal amount, String detailId, String state, String detailType) throws Exception {
		ParameterMap userAccountDetail = new ParameterMap();
		ParameterMap paramte = new ParameterMap();
		paramte.put("userId",userId);
		paramte.put("code",code);
		ParameterMap userAccount = this.getByCondition(paramte);
		userAccountDetail.put("beforMoney",userAccount.get("amount"));
		if("1".equals(state)){
			userAccountDetail.put("afterMoney",new BigDecimal(userAccount.getString("amount")).add(amount));
		}else{
			userAccountDetail.put("afterMoney",new BigDecimal(userAccount.getString("amount")).compareTo(amount) < 0?BigDecimal.ZERO:new BigDecimal(userAccount.getString("amount")).add(amount));
		}
		userAccountDetail.put("userId",userId);
		userAccountDetail.put("transUserId",transUserId);
		userAccountDetail.put("code",code);
		userAccountDetail.put("amount",amount);
		userAccountDetail.put("state",state);
		userAccountDetail.put("detailId",detailId);
		userAccountDetail.put("detailType",detailType);
		userAccountDetail.put("isDeleted","N");
		userAccountDetail.put("createTime",new Date());
		userAccountDetail.put("createBy",userId);
		userAccountDetail.put("modifyBy",userId);
		userAccountDetail.put("modifyTime",new Date());
		userAccount.put("amount",userAccountDetail.getString("afterMoney"));
		this.updateByPrimaryKeySelective(userAccount);
		userAccountDetailService.insertSelective(userAccountDetail);

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
			isdel = this.deleteUserAccount(ids);
			return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}

	@Override
	public List<ParameterMap> getListByCode(ParameterMap pm){
		return this.userAccountMapper.getListByCode(pm);
	}
	
}
