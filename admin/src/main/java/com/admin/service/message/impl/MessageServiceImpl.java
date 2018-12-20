
package com.admin.service.message.impl;

import com.admin.config.LangName;
import com.admin.dao.message.MessageDao;
import com.admin.service.messageLang.MessageLangService;
import com.admin.util.ParameterMap;
import com.admin.entity.MessageFactory;
import com.admin.entity.MoneyType;
import com.admin.entity.dto.MessageModel;
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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;

import com.admin.service.message.MessageService;


/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional
public class MessageServiceImpl implements MessageService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private MessageDao messageMapper;

	@Autowired
	private MessageLangService messageLangService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Transactional
	 public  int deleteByPrimaryKey(Long messageId){
		 return  messageMapper.deleteByPrimaryKey(messageId);
	 }
	
	@Transactional
	 public   int insert(ParameterMap pm){
		 return messageMapper.insert(pm);
	 }
	
	@Transactional
	 public  int insertSelective(ParameterMap pm){
		 return messageMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(Long messageId){
		 return messageMapper.selectByPrimaryKey(messageId);
	 }
	 
	@Transactional
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  messageMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  messageMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.messageMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.messageMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional
	public boolean deleteMessage(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.messageMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.messageMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		return this.messageMapper.getByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.messageMapper.getByConditionCount(pm);
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
			isdel = this.deleteMessage(ids);
			return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void saveMessageByReview(Long toUserId, String reviewStatus,String banReason) throws Exception {
		ParameterMap pm = new ParameterMap();
		pm.put("userId",toUserId);
		pm.put("type", MoneyType.REVIEW.getValue());
		pm.put("isRead",0);
		pm.put("createBy",toUserId);
		pm.put("createTime",new Date());
		pm.put("isDeleted","N");
		this.insertSelective(pm);
		Long mid = (Long) pm.get("id");

		saveMessageLang(mid,toUserId,reviewStatus,banReason);
	}

	public  void saveMessageLang(Long messageId, Long userId,String type,String banReason) throws Exception {
		ParameterMap pm = new ParameterMap();
		pm.put("messageId",messageId);
		pm.put("userId",userId);
		pm.put("isDeleted","N");
		pm.put("createBy",userId);
		pm.put("createTime",new Date());

		saveMessageByLanguage(pm, LangName.ZH_CN,type,banReason);

		saveMessageByLanguage(pm,LangName.ZH_TW,type,banReason);
		saveMessageByLanguage(pm, LangName.EN_US,type,banReason);
	}
	public void saveMessageByLanguage(ParameterMap pm,String lang,String type,String banReason) throws Exception {
		MessageModel model = MessageFactory.getInstance().getModel(lang,type);
		pm.put("title",model.getTitle());
		String content = String.format(model.getContent(),banReason);
		pm.put("content",content);
		pm.put("lang",lang);
		messageLangService.insertSelective(pm);
	}

}
