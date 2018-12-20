
package com.admin.service.rechargeDetail.impl;

import com.admin.service.rechargeDetail.RechargeDetailService;
import com.admin.util.ParameterMap;
import com.admin.entity.MoneyType;
import com.admin.service.appuser.AppUserService;
import com.admin.service.userAccount.UserAccountService;
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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;

import com.admin.dao.rechargeDetail.RechargeDetailDao;

/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional
public class RechargeDetailServiceImpl implements RechargeDetailService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private RechargeDetailDao rechargeDetailMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private AppUserService appUserService;

	
	@Transactional(readOnly = false)
	 public  int deleteByPrimaryKey(java.lang.Long rechargeDetailId){
		 return  rechargeDetailMapper.deleteByPrimaryKey(rechargeDetailId);
	 }
	
	@Transactional(readOnly = false)
	 public   int insert(ParameterMap pm){
		 return rechargeDetailMapper.insert(pm);
	 }
	
	@Transactional(readOnly = false)
	 public  int insertSelective(ParameterMap pm){
		 return rechargeDetailMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(java.lang.Long rechargeDetailId){
		 return rechargeDetailMapper.selectByPrimaryKey(rechargeDetailId);
	 }
	 
	@Transactional(readOnly = false)
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  rechargeDetailMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional(readOnly = false)
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  rechargeDetailMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional(rollbackFor=Exception.class)  
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.rechargeDetailMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.rechargeDetailMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional(rollbackFor=Exception.class)
	public boolean deleteRechargeDetail(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.rechargeDetailMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.rechargeDetailMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.rechargeDetailMapper.getByConditionList(argmap);
	}

	public List<ParameterMap> getRechargeList(ParameterMap argmap) throws Exception {
		return this.rechargeDetailMapper.getRechargeList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.rechargeDetailMapper.getByConditionCount(pm);
	}
	
	@Transactional
	public Object add(ParameterMap pm,HttpSession session) throws Exception {
		try {
			if(StringUtils.isEmpty(pm.getString("id"))){
				pm.remove("id");
			}
			System.out.println("pm======="+pm);
			//通过会员号，获取user 对象
			ParameterMap parameter = new ParameterMap();
			parameter.put("userName",pm.getString("userName"));
			ParameterMap appUser = appUserService.getByCondition(parameter);
			if(appUser == null || StringUtils.isEmpty(appUser.getString("id"))){
				return ResponseModel.getModel(ResultEnum.USER_EXISTENT, null);
			}

			User user = (User) session.getAttribute(Const.SESSION_USER);
			pm.put("createTime", LocalDateTime.now());
			pm.put("createBy", user.getUserId());
			pm.put("userId",appUser.getString("id"));
			this.insertSelective(pm);

			parameter.clear();
			parameter.put("userId",appUser.getString("id"));
			parameter.put("code",pm.getString("type"));
			ParameterMap userAccount = userAccountService.getByCondition(parameter);
			userAccountService.updateAccount(appUser.getString("id"),"-1",pm.getString("type"),new BigDecimal(pm.getString("amount")),pm.getString("id"),"1", MoneyType.RECHARGE.getValue()+"");
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
			isdel = this.deleteRechargeDetail(ids);
			return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}
	
}
