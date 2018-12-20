
package com.admin.service.coinInfo.impl;

import com.admin.config.LangName;
import com.admin.dao.coinInfo.CoinInfoDao;
import com.admin.service.coinInfo.CoinInfoService;
import com.admin.service.coinInfoName.CoinInfoNameService;
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
import java.util.Date;
import java.util.HashMap;
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
@Transactional
public class CoinInfoServiceImpl implements CoinInfoService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private CoinInfoDao coinInfoMapper;

	@Autowired
	private CoinInfoNameService coinInfoNameService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Transactional
	 public  int deleteByPrimaryKey(Long coinInfoId){
		 return  coinInfoMapper.deleteByPrimaryKey(coinInfoId);
	 }
	
	@Transactional
	 public   int insert(ParameterMap pm){
		 return coinInfoMapper.insert(pm);
	 }
	
	@Transactional
	 public  int insertSelective(ParameterMap pm){
		 return coinInfoMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(Long coinInfoId){
		 return coinInfoMapper.selectByPrimaryKey(coinInfoId);
	 }
	 
	@Transactional
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  coinInfoMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  coinInfoMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.coinInfoMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.coinInfoMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional
	public boolean deleteCoinInfo(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.coinInfoMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.coinInfoMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.coinInfoMapper.getByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.coinInfoMapper.getByConditionCount(pm);
	}
	
	@Transactional
	public Object add(ParameterMap pm,HttpSession session) throws Exception {
		try {
			if(StringUtils.isEmpty(pm.getString("id"))){
				pm.remove("id");
			}
			pm.put("chineseName",pm.get("name"));
			pm.put("createTime", LocalDateTime.now());
			pm.put("createBy", ((User) session.getAttribute(Const.SESSION_USER)).getUserId());
			//插入中文版本
			ParameterMap lang = new ParameterMap();
			lang.put("coin",pm.get("code"));
			lang.put("lang", LangName.ZH_CN);
			lang.put("chineseLang",LangName.getChineseName(LangName.ZH_CN));
			lang.put("name",pm.get("name"));
			if (pm.getString("status").equals("Y")){
				lang.put("state",1);
			}else{
				lang.put("state",0);
			}
			lang.put("isDeleted",'N');
			lang.put("createTime", LocalDateTime.now());
			lang.put("createBy", ((User) session.getAttribute(Const.SESSION_USER)).getUserId());

			coinInfoNameService.insertSelective(lang);

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
			HashMap<String, Object> map = new HashMap<>();
			map.put("detail",detail);
			pm.clear();
			pm.put("coin",detail.get("code"));
			map.put("internaList",this.coinInfoMapper.getNameList(pm));
			return ResponseModel.getModel(ResultEnum.SUCCESS, map);
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
			System.out.println(pm.get("wei")+"--------");

			String banben = pm.getString("banben");
			String banbenIsUser = pm.getString("banbenIsUser");
			String banbenName = pm.getString("banbenName");
			if (!StringUtils.isEmpty(banben) && !banben.equals("-1")) {
				if (StringUtils.isEmpty(banbenIsUser) || StringUtils.isEmpty(banbenName)) {
					return ResponseModel.getModel("编辑版本，版本参数不能为空", "666", null);
				}

				ParameterMap langPm = new ParameterMap();
				langPm.put("coin", pm.get("code"));
				langPm.put("lang", banben);
				ParameterMap langPmpd = coinInfoNameService.getByCondition(langPm);
				if (LangName.getChineseName(banben.toString()).equals("error")) {
					return ResponseModel.getModel("添加失败，请联系管理人员！", "666", null);
				}

				langPm.put("chineseLang", LangName.getChineseName(banben.toString()));
				langPm.put("name", banbenName);
				langPm.put("state", banbenIsUser);
				langPm.put("isDeleted", "N");
				langPm.put("modifyTime", new Date());
				langPm.put("modifyBy", user.getUserId());
				if (langPmpd == null) {
					coinInfoNameService.insertSelective(langPm);
				} else {
					langPm.put("id",langPmpd.get("id"));
					coinInfoNameService.updateByPrimaryKeySelective(langPm);
				}
				if (banben.toString().equals(LangName.ZH_CN)){
					pm.put("name",banbenName);
					pm.put("chineseName",banbenName);
				}
			}

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
			isdel = this.deleteCoinInfo(ids);
			return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}
	@Transactional
	public List<ParameterMap> getAllCoinName(ParameterMap pm)throws Exception{
          return this.coinInfoMapper.getAllCoinName(pm);
	}

	public List<ParameterMap> selectList(ParameterMap pm){
		return this.coinInfoMapper.selectList(pm);
	}
	
}
