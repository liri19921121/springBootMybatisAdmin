
package com.admin.service.dailyTotal.impl;

import com.admin.config.MoneyType;
import com.admin.dao.dailyTotal.DailyTotalDao;
import com.admin.service.accountPlan.AccountPlanService;
import com.admin.service.dailyTotal.DailyTotalService;
import com.admin.util.ParameterMap;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.druid.util.StringUtils;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.system.Const;
import com.admin.entity.system.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;

import org.springframework.web.client.RestTemplate;

/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional
public class DailyTotalServiceImpl implements DailyTotalService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private DailyTotalDao dailyTotalMapper;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AccountPlanService accountPlanService;

	@Autowired
	private RestTemplate restTemplate;


	@Value("${mq.addPlanUrl}")
	private String url;

	@Transactional
	 public  int deleteByPrimaryKey(java.lang.Long dailyTotalId){
		 return  dailyTotalMapper.deleteByPrimaryKey(dailyTotalId);
	 }
	
	@Transactional
	 public   int insert(ParameterMap pm){
		 return dailyTotalMapper.insert(pm);
	 }
	
	@Transactional
	 public  int insertSelective(ParameterMap pm){
		 return dailyTotalMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(java.lang.Long dailyTotalId){
		 return dailyTotalMapper.selectByPrimaryKey(dailyTotalId);
	 }
	 
	@Transactional
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  dailyTotalMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  dailyTotalMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.dailyTotalMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.dailyTotalMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional
	public boolean deleteDailyTotal(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.dailyTotalMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.dailyTotalMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.dailyTotalMapper.getByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.dailyTotalMapper.getByConditionCount(pm);
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

	/**
	 * 审核
	 * @param pm
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object auditing(ParameterMap pm, HttpSession session) throws Exception {
		User user = (User) session.getAttribute(Const.SESSION_USER);
		ParameterMap plan = new ParameterMap();
		plan.put("objectId",-1);
		plan.put("userId",-1);
		plan.put("tranUserId",-1);
		plan.put("description",pm.getString("id"));
		plan.put("intoAccount","0");
		plan.put("createBy",user.getUserId());
		plan.put("createTime",new Date());
		plan.put("operateSource", MoneyType.PLAN_QUICKER_RELEASE.getValue());
		accountPlanService.insertSelective(plan);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url + "?id=" + plan.getString("id"), String.class);
        System.out.println("responseEntity="+responseEntity);
        JSONObject obj = JSONObject.parseObject(responseEntity.getBody());
        if(obj.getString("status").equals("200")){
            ParameterMap dailyTotal = new ParameterMap();
            dailyTotal.put("id",pm.getString("id"));
            dailyTotal.put("status",1);
            dailyTotal.put("modifyTime",new Date());
            dailyTotal.put("modifyBy",user.getUserId());
            this.updateByPrimaryKeySelective(dailyTotal);
            return ResponseModel.getModel(ResultEnum.SUCCESS, null);
        }else {
            return  ResponseModel.getModel(obj.getString("message"),obj.getString("status"),null);
        }

	}

	@Transactional
	public Object del(ParameterMap pm) throws Exception {
		boolean isdel = false;
		try {
			String ids = pm.getString("id");
			isdel = this.deleteDailyTotal(ids);
			return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}
	
}
