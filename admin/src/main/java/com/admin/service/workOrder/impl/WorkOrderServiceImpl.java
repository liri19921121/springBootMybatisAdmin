
package com.admin.service.workOrder.impl;

import com.admin.dao.workOrder.WorkOrderDao;
import com.admin.service.workOrder.WorkOrderService;
import com.admin.service.workOrderDetail.WorkOrderDetailService;
import com.admin.util.ParameterMap;
import com.admin.entity.dto.WorkOrderDTO;
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
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;

/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional(readOnly = true)
public class WorkOrderServiceImpl implements WorkOrderService {

	@Value("${home.url}")
	public String home_url;

	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private WorkOrderDao workOrderMapper;

	@Autowired
	private WorkOrderDetailService workOrderDetailService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Transactional(readOnly = false)
	 public  int deleteByPrimaryKey(Long workOrderId){
		 return  workOrderMapper.deleteByPrimaryKey(workOrderId);
	 }
	
	@Transactional(readOnly = false)
	 public   int insert(ParameterMap pm){
		 return workOrderMapper.insert(pm);
	 }
	
	@Transactional(readOnly = false)
	 public  int insertSelective(ParameterMap pm){
		 return workOrderMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(Long workOrderId){
		 return workOrderMapper.selectByPrimaryKey(workOrderId);
	 }
	 
	@Transactional(readOnly = false)
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  workOrderMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional(readOnly = false)
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  workOrderMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional(rollbackFor=Exception.class)  
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.workOrderMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.workOrderMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional(rollbackFor=Exception.class)
	public boolean deleteWorkOrder(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.workOrderMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.workOrderMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.workOrderMapper.getByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.workOrderMapper.getByConditionCount(pm);
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

	@Transactional(readOnly = false)
	public Object addReplyContent(ParameterMap pm,HttpSession session) throws Exception {
		try {
			String replyContent = pm.getString("replyContent");
			Long orderId = Long.valueOf(pm.get("orderId").toString());
			Long userId = Long.valueOf(pm.get("userId").toString());

			//更新order表
			ParameterMap pmOrder = new ParameterMap();
			pmOrder.put("id",orderId);
			pmOrder.put("state",1);
			pmOrder.put("modifyTime", LocalDateTime.now());
			pmOrder.put("modifyBy", ((User) session.getAttribute(Const.SESSION_USER)).getUserId());
			this.updateByPrimaryKeySelective(pmOrder);
			//添加明细
			ParameterMap pmOrderDetail = new ParameterMap();
			pmOrderDetail.put("orderId",orderId);
			pmOrderDetail.put("userId",userId);
			pmOrderDetail.put("content",replyContent);
			pmOrderDetail.put("state",1);
			pmOrderDetail.put("isDeleted","N");
			pmOrderDetail.put("isRead",0);
			pmOrderDetail.put("createTime", new Date());
			pmOrderDetail.put("createBy",  ((User) session.getAttribute(Const.SESSION_USER)).getUserId());
			workOrderDetailService.insertSelective(pmOrderDetail);
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
			isdel = this.deleteWorkOrder(ids);
			return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}

	@Transactional(readOnly = false)
	public  List<WorkOrderDTO> selectWorkOrderList(Long workOrderId) {
		ParameterMap pm = new ParameterMap();
		pm.put("workOrderId",workOrderId);
		pm.put("homeUrl",home_url);
		List<WorkOrderDTO> list = this.workOrderMapper.selectWorkOrderList(pm);
		return list;
	}

	@Transactional(readOnly = false)
	public ParameterMap getDetailById(ParameterMap pm) throws Exception{
		return this.workOrderMapper.getDetailById(pm);
	}
	
}
