package com.admin.controller.system;

import com.admin.annotation.Permission;
import com.admin.entity.PermissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.controller.base.BaseController;
import com.admin.entity.system.Const;
import com.admin.entity.system.User;
import com.admin.service.system.IUserService;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
	
	@Autowired
	private IUserService userService;
	
	private final static String qxurl = "user/list";

	@Value("${home.url}")
	public String homeUrl;
	
	/**
	 * 用户列表
	 * @return
	 */
	@GetMapping("/list")
	public Object login(Model model){

		model.addAttribute("users", userService.getUserList());
		model.addAttribute("homeUrl", homeUrl);
		model.addAttribute("meid", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getUserId());
		return "page/system/user/list";
	}
	
	/**
	 * 获取用户角色
	 * @return
	 */
	@GetMapping("/getRole")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object userRole(){
		return userService.getRole(this.getParameterMap());
	}
	
	/**
	 * 添加用户
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object add(){
		return userService.add(this.getParameterMap(),this.getSession());
	}
	
	
	/**
	 * 编辑用户
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit(){
		return userService.edit(this.getParameterMap());
	}
	
	/**
	 * 编辑用户
	 * @return
	 */
	@PostMapping("/editRole")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object editRole(){
		return userService.editRole(this.getParameterMap());
	}
	
	/**
	 * 删除用户
	 * @return
	 */
	@PostMapping("/del")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.DEL)
	public Object del(){
		return userService.del(this.getParameterMap());
	}
	
	
}
