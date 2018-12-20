
package com.admin.controller.userExtendInfo;


import com.admin.util.ParameterMap;
import com.alibaba.druid.util.StringUtils;
import com.admin.annotation.Permission;
import com.admin.entity.PermissionType;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.system.User;
import com.admin.service.appuser.AppUserService;
import com.admin.service.message.MessageService;
import com.admin.service.userExtendLang.UserExtendLangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.service.userExtendInfo.UserExtendInfoService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.admin.controller.base.BaseController;
import com.admin.entity.system.Const;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * dev
 * @author rstyro
 * @version v1.0
 */

@Controller
@RequestMapping("/userExtendInfo")
public class UserExtendInfoController extends BaseController{
	
	private final static String qxurl="userExtendInfo/list";
	
	@Autowired
	private UserExtendInfoService userExtendInfoService;

	@Autowired
	private UserExtendLangService userExtendLangService;

	@Autowired
	private AppUserService appUserService;

	@Value("${home.url}")
	public String homeUrl;

	@Autowired
	public MessageService messageService;
	

	@GetMapping(path={"/list/{page}","/list"})
	public Object list(@PathVariable(value="page",required=false)Integer pageNo,Model model){
		try {
			if(pageNo == null){
				pageNo=1;
			}
			Map<String,String> reviewStatusList = new LinkedHashMap<>();
			reviewStatusList.put("1","待审核");
			reviewStatusList.put("2","审核通过");
			reviewStatusList.put("3","审核不通过");

			ParameterMap pm = this.getParameterMap();
			pm.put("isDeleted","N");
			Page<ParameterMap> page = PageHelper.startPage(pageNo, Const.PAGE_SIZE).doSelectPage(() -> {
				try {
					userExtendInfoService.getByConditionList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			String lang = (String) pm.get("lang");
			if(StringUtils.isEmpty(lang)){
				pm.put("lang","zh_CN");
			}

			model.addAttribute("nationlityList",userExtendLangService.getByConditionList(pm));
			model.addAttribute("reviewStatusList", reviewStatusList);
			model.addAttribute("userExtendInfo", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("keyword", pm.getString("keyword"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/userExtendInfo/list";
	}

	@GetMapping(path={"/toEdit"})
	public Object toAdd(Model model) throws Exception{
		User user = (User) this.getSession().getAttribute(Const.SESSION_USER);
		ParameterMap pm = this.getParameterMap();

		Map<String,String> reviewStatusList = new LinkedHashMap<>();
		reviewStatusList.put("1","待审核");
		reviewStatusList.put("2","审核通过");
		reviewStatusList.put("3","审核不通过");

		ParameterMap param = new ParameterMap();
		param.put("lang","zh_CN");
		model.addAttribute("nationlityList",userExtendLangService.getByConditionList(param));
		model.addAttribute("usrExtendInfo", userExtendInfoService.getByCondition(pm));
		model.addAttribute("reviewStatusList", reviewStatusList);
		model.addAttribute("id", pm.getString("id"));
		model.addAttribute("homeUrl", homeUrl);
		model.addAttribute("actionurl", "/userExtendInfo/edit?page="+pm.get("page"));
		model.addAttribute("user", user);
		return "page/userExtendInfo/edit";
	}

	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit(){
		try {
			ParameterMap pm = this.getParameterMap();
			userExtendInfoService.updateByPrimaryKeySelective(pm);
			if(pm.get("realAuditStatus") !=null && pm.get("realAuditStatus") != Byte.valueOf("1")){
				ParameterMap param = new ParameterMap();
				param.put("id",pm.get("userId"));
				param.put("authVerified",pm.get("realAuditStatus"));
				appUserService.updateByPrimaryKeySelective(param);
				//还差发短信或者邮箱通知
				String uid = (String) pm.get("userId");
				Long userId = Long.parseLong(uid);
				String reviewStatus = (String) pm.get("realAuditStatus");
				String banreason = (String) pm.get("banReason");
				messageService.saveMessageByReview(userId,reviewStatus,banreason);
			}

			return ResponseModel.getModel(ResultEnum.SUCCESS, pm.get("page"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}

	@GetMapping(path={"/toView"})
	public Object toView(Model model) throws Exception{
		ParameterMap pm = this.getParameterMap();
		Map<String,String> reviewStatusList = new LinkedHashMap<>();
		reviewStatusList.put("1","待审核");
		reviewStatusList.put("2","审核通过");
		reviewStatusList.put("3","审核不通过");

		ParameterMap param = new ParameterMap();
		param.put("lang","zh_CN");
		model.addAttribute("nationlityList",userExtendLangService.getByConditionList(param));
		model.addAttribute("usrExtendInfo", userExtendInfoService.getByCondition(pm));
		model.addAttribute("reviewStatusList", reviewStatusList);
		model.addAttribute("id", pm.getString("id"));
		model.addAttribute("homeUrl", homeUrl);
		model.addAttribute("page", pm.get("page"));
		return "page/userExtendInfo/view";
	}
}