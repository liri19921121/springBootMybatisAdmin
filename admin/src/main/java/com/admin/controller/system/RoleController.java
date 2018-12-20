package com.admin.controller.system;

import com.admin.annotation.Permission;
import com.admin.entity.PermissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.controller.base.BaseController;
import com.admin.entity.ResponseModel;
import com.admin.service.system.IRoleService;
import com.admin.util.Jurisdiction;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{
	
	private final static String qxurl="role/list";
	
	@Autowired
	private IRoleService roleService;
	
	/**
	 * 角色列表
	 * @return
	 */
	@GetMapping("/list")
	public Object list(Model model){
		model.addAttribute("roles", roleService.list());
		return "page/system/role/list";
	}
	
	/**
	 * 获取权限
	 * @return
	 */
	@PostMapping("/qx")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object qx(){
		if(!Jurisdiction.buttonJurisdiction(qxurl, "add",this.getSession())){return ResponseModel.getNotAuthModel();} //校验权限
		return roleService.getMenu(this.getParameterMap());
	}
	
	/**
	 * 更改角色
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit(){
		return roleService.edit(this.getParameterMap());
	}
	/**
	 * 添加角色
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object add(){
		return roleService.add(this.getParameterMap(),this.getSession());
	}
	/**
	 * 删除角色
	 * @return
	 */
	@GetMapping("/del/{roleId}")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.DEL)
	public Object del(@PathVariable("roleId") String roleId){
		return roleService.del(roleId);
	}
	
	
}
