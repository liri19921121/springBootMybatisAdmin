
package com.admin.controller.user;


import com.admin.controller.base.BaseController;
import com.admin.util.ParameterMap;
import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.system.Const;
import com.admin.service.appuser.AppUserService;
import com.admin.service.redis.RedisService;
import com.admin.service.userExtendInfo.UserExtendInfoService;
import com.admin.service.userExtendLang.UserExtendLangService;
import com.admin.util.EncryptUtil;
import com.admin.util.Jurisdiction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * dev
 * @author rstyro
 * @version v1.0
 */

@Controller
@RequestMapping("/appUser")
public class AppUserController extends BaseController {
	
	private String qxurl="appUser/list";
	
	@Autowired
	private AppUserService userService;

	@Autowired
	private UserExtendInfoService userExtendInfoService;

	@Autowired
	private UserExtendLangService userExtendLangService;

	@Autowired
	private RedisService redisService;

	private static final String USER_TOKEN_RECIRD = "UW_USER_TOKEN_RECIRD";

	private static final String TOKEN_FORMAT = "TOKEN-%s";

	@Value("${home.url}")
	public String homeUrl;

	@Value("${api.url}")
	public String apiUrl;

	@GetMapping(path={"/list/{page}","/list"})
	public Object list(@PathVariable(value="page",required=false)Integer pageNo,Model model){
		try {
			if(pageNo == null){
				pageNo=1;
			}
			ParameterMap pm = this.getParameterMap();

			ParameterMap map = new ParameterMap();
			pm.put("isDeleted","N");
			Page<ParameterMap> page = PageHelper.startPage(pageNo, Const.PAGE_SIZE).doSelectPage(() -> {
				try {
					userService.getAppUserByConditionList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			Map<String,String> reviewStatusList = new LinkedHashMap<>();
			reviewStatusList.put("0","未验证");
			reviewStatusList.put("1","待审核");
			reviewStatusList.put("2","审核通过");
			reviewStatusList.put("3","审核不通过");
			model.addAttribute("user", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("reviewStatusList", reviewStatusList);
			model.addAttribute("key_username", pm.getString("userName"));
			model.addAttribute("key_phone", pm.getString("mobile"));
			model.addAttribute("key_mail", pm.getString("mail"));
			model.addAttribute("key_status",pm.get("status"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/user/list";
	}
	
	@GetMapping(path="/detail")
	@ResponseBody
	public Object getDetail() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(qxurl, "query",this.getSession())){return ResponseModel.getNotAuthModel();} 
		return userService.getDetail(this.getParameterMap());
	}
	
	
	@PostMapping("/edit")
	@ResponseBody
	public Object edit() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(qxurl, "edit",this.getSession())){return ResponseModel.getNotAuthModel();} 
		return userService.edit(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping("/add")
	@ResponseBody
	public Object add() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(qxurl, "add",this.getSession())){return ResponseModel.getNotAuthModel();} 
		return userService.add(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping(path="/del")
	@ResponseBody
	public Object del() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(qxurl, "del",this.getSession())){return ResponseModel.getNotAuthModel();} 
		return userService.del(this.getParameterMap());
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
		ParameterMap userExtendParam = new ParameterMap();
		userExtendParam.put("userId",pm.get("id"));
		List<ParameterMap> list = userExtendInfoService.getByConditionList(userExtendParam);
		model.addAttribute("userInfo",userService.getByCondition(pm));
		model.addAttribute("nationlityList",userExtendLangService.getByConditionList(param));
		if(CollectionUtils.isEmpty(list)){
			model.addAttribute("usrExtendInfo", null);
		}else{
			ParameterMap map = list.get(0);
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
			model.addAttribute("usrExtendInfo",map);
		}
		model.addAttribute("reviewStatusList", reviewStatusList);
		model.addAttribute("id", pm.getString("id"));
		model.addAttribute("homeUrl", homeUrl);
		model.addAttribute("page", pm.get("page"));
		model.addAttribute("actionurl", "/appUser/restEdit");
		return "page/user/view";
	}

	@PostMapping("/restEdit")
	@ResponseBody
	public Object restEdit() throws Exception {
		ParameterMap pm = this.getParameterMap();
		if(pm.get("restType").equals("1")){
			String pwd = (String) pm.get("password");
			if(StringUtils.isEmpty(pwd)){
				return ResponseModel.getModel(ResultEnum.ERROR, "登陆密码不能为空");
			}
			pm.put("password", EncryptUtil.sha256Encrypt(pwd));
		}
		if(pm.get("restType").equals("2")){
			String pwd = (String) pm.get("dealPassword");
			if(StringUtils.isEmpty(pwd)){
				return ResponseModel.getModel(ResultEnum.ERROR, "安全密码不能为空");
			}
			pm.put("dealPassword", EncryptUtil.sha256Encrypt(pwd));
		}

		userService.updateByPrimaryKeySelective(pm);
		if(pm.get("restType").equals("4")){
			String userToken = (String) redisService.stringGetByKey(USER_TOKEN_RECIRD+pm.get("id"));
			redisService.deleteFromRedis(String.format(TOKEN_FORMAT,userToken));
		}

		return ResponseModel.getModel(ResultEnum.SUCCESS, pm.get("page"));
	}

	@PostMapping("/update")
	@ResponseBody
	public Object update() throws Exception {
		String regex = "\\w+@\\w+(\\.[a-zA-Z]+)+";

		ParameterMap pm = this.getParameterMap();
		ParameterMap param = new ParameterMap();
		if(!StringUtils.isEmpty(pm.get("mobile").toString())){
			param.put("mobile",pm.get("mobile"));
			param.put("isDeleted","N");

			pm.put("authMobile",Byte.valueOf("1"));
			ParameterMap result = userService.getByCondition(param);
			if(result != null ){
				String pmId = (String) pm.get("id");
				Long resultId = (Long) result.get("id");
				if(!pmId.equals(resultId.toString())){
					return ResponseModel.getModel(ResultEnum.ERROR, "手机号已经存在");
				}
			}
		}
		if(!StringUtils.isEmpty(pm.get("mail").toString())){
			String maile = (String) pm.get("mail");
			pm.put("authMail",Byte.valueOf("1"));
			if(!maile.matches(regex)){
				return ResponseModel.getModel(ResultEnum.ERROR, "邮箱格式不正确");
			}
		}
		userService.updateByPrimaryKeySelective(pm);
		return ResponseModel.getModel(ResultEnum.SUCCESS, pm.get("page"));
	}
}