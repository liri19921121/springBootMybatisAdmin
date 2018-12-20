
package com.admin.service.usrArea.impl;

import com.admin.dao.usrArea.UsrAreaDao;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.system.Const;
import com.admin.entity.system.User;
import com.admin.service.areaFather.AreaFatherService;
import com.admin.service.relationUmbrella.RelationUmbrellaService;
import com.admin.util.ParameterMap;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.druid.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;

import com.admin.service.usrArea.UsrAreaService;

/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional
public class UsrAreaServiceImpl implements UsrAreaService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private UsrAreaDao usrAreaMapper;

	@Autowired
	private RelationUmbrellaService relationUmbrellaService;

	@Autowired
	private UsrAreaService usrAreaService;

	@Autowired
	private AreaFatherService areaFatherService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Transactional
	 public  int deleteByPrimaryKey(Long usrAreaId){
		 return  usrAreaMapper.deleteByPrimaryKey(usrAreaId);
	 }
	
	@Transactional
	 public   int insert(ParameterMap pm){
		 return usrAreaMapper.insert(pm);
	 }
	
	@Transactional
	 public  int insertSelective(ParameterMap pm){
		 return usrAreaMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(Long usrAreaId){
		 return usrAreaMapper.selectByPrimaryKey(usrAreaId);
	 }
	 
	@Transactional
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  usrAreaMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  usrAreaMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.usrAreaMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.usrAreaMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional
	public boolean deleteUsrArea(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.usrAreaMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.usrAreaMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.usrAreaMapper.getByConditionList(argmap);
	}

	public List<ParameterMap> getPageList(ParameterMap pm)throws Exception{
		return this.usrAreaMapper.getPageList(pm);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.usrAreaMapper.getByConditionCount(pm);
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
	public Object del(ParameterMap pm, HttpSession session) throws Exception {
			User user = (User) session.getAttribute(Const.SESSION_USER);
			String adminId = user.getUserId();
			String id = pm.getString("id");
			pm.clear();
			pm.put("id",id);
			pm.put("isDeleted","Y");
			pm.put("modifyBy",adminId);
			pm.put("modifyTime",new Date());
			//删除分区
			this.updateByPrimaryKeySelective(pm);

			//这个区域内所有的用户区域属性变为该删除用的的上级区域属性
			pm.clear();
			pm.put("id",id);
			ParameterMap usrArea = this.getByCondition(pm);
			String userId = usrArea.getString("top_user");
			pm.clear();
			pm.put("userId",userId);
			ParameterMap relationUmbrella = relationUmbrellaService.getByCondition(pm);
			//上级id
			String recommendId = relationUmbrella.getString("recommend_id");

		    //更改关系表
			pm.clear();
			pm.put("areaId",id);
			List<Long> ruIds = relationUmbrellaService.getIdsByAreaId(pm);
			if (!"0".equals(recommendId)){
				pm.clear();
				pm.put("userId",recommendId);
				ParameterMap fatherRU = relationUmbrellaService.getByCondition(pm);
				//批量更新
				pm.clear();
				System.out.println( fatherRU.getString("group_user_id")+"---");
				System.out.println( fatherRU.getString("area_id")+"--==-");
				pm.put("groupUserId", fatherRU.getString("group_user_id"));
				pm.put("hasArea", fatherRU.getString("has_area"));
				pm.put("areaId", fatherRU.getString("area_id"));
				pm.put("modifyTime", new Date());
				pm.put("modifyBy", adminId);
				pm.put("ids", ruIds);
				relationUmbrellaService.updateByIds(pm);
			}else{
				//上级没有分区，所以该分区内的用户全部去除分区
				//批量更新
				pm.clear();
				pm.put("groupUserId", 0);
				pm.put("hasArea",0);
				pm.put("areaId", 0);
				pm.put("modifyTime", new Date());
				pm.put("modifyBy", adminId);
				pm.put("ids", ruIds);
				relationUmbrellaService.updateByIds(pm);
			}

			//删除父级表数据
			pm.clear();
			pm.put("areaId",usrArea.getString("id"));
			pm.put("idDelete","Y");
			pm.put("modifyBy",adminId);
			pm.put("modifyTime",new Date());
			areaFatherService.updateByAreaid(pm);
			return ResponseModel.getModel(ResultEnum.SUCCESS, null);
	}

	@Transactional
	public Object createArea(ParameterMap pm, HttpSession session) throws  Exception{
		String userId = pm.getString("userId");
		String areaName = pm.getString("areaName");
		if (StringUtil.isEmpty(userId)){
			return ResponseModel.getModel("请选择用户","666",null);
		}
		if (StringUtil.isEmpty(areaName)){
			return ResponseModel.getModel("请填写分区名","666",null);
		}
		ParameterMap temPm = new ParameterMap();
		temPm.put("userId",userId);
		ParameterMap relationUmbrella = relationUmbrellaService.getByCondition(temPm);
		if (relationUmbrella == null){
			return ResponseModel.getModel("用户关系不存在","666",null);
		}
		User user = (User) session.getAttribute(Const.SESSION_USER);
		String adminId = user.getUserId();

		temPm.clear();
		temPm.put("topUser",userId);
		temPm.put("isDeleted","N");
		ParameterMap areaIsPm = usrAreaService.getByCondition(temPm);
		if (areaIsPm != null){
			return ResponseModel.getModel("分区已存在("+areaIsPm.getString("area_name")+")，请勿重复添加","666",null);
		}

		//保存分区
		temPm.clear();
		temPm.put("topLevel",relationUmbrella.get("level"));
		temPm.put("topUser",userId);
		temPm.put("areaName",areaName);
		temPm.put("createTime",new Date());
		temPm.put("createBy",adminId);
		temPm.put("isDeleted","N");
		usrAreaService.insertSelective(temPm);

		Long areaId = Long.valueOf(temPm.getString("id"));

		//保存父级节点
		String umbrellaChain = relationUmbrella.getString("umbrella_chain");
		if (!StringUtils.isEmpty(umbrellaChain)){
			String[] ids = umbrellaChain.split(",");
			System.out.println(ids);
			List<ParameterMap> afdList = new ArrayList<>();
			temPm.clear();
			Date time = new Date();
			for (int i = 0; i< ids.length;i++){
				ParameterMap pmDto = new ParameterMap();
				pmDto.put("areaId",areaId);
				pmDto.put("userId",ids[i]);
				pmDto.put("createTime",time);
				pmDto.put("createBy",adminId);
				pmDto.put("isDeleted",'N');
				afdList.add(pmDto);
			}
			//批量插入
			areaFatherService.insertFathers(afdList);
		}

		//更新关系表
		//查询所有需要更新的数据
		String oldAreaId = relationUmbrella.getString("area_id");
		temPm.clear();
		temPm.put("userId",userId);
		temPm.put("areaId",oldAreaId);
		List<Long> ids = relationUmbrellaService.getIds(temPm);

		//批量更新
		temPm.clear();
		temPm.put("groupUserId", userId);
		temPm.put("hasArea", 1);
		temPm.put("areaId", areaId);
		temPm.put("modifyTime", new Date());
		temPm.put("modifyBy", adminId);
		temPm.put("ids", ids);
		relationUmbrellaService.updateByIds(temPm);

		return ResponseModel.getModel(ResultEnum.SUCCESS, null);
	}

}
