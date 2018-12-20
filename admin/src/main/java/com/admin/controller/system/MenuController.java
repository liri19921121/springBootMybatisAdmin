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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.controller.base.BaseController;
import com.admin.entity.ResponseModel;
import com.admin.service.system.IMenuService;
import com.admin.util.Jurisdiction;

@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController{
	
	private final static String qxurl = "menu/list";
	
	@Autowired
	private IMenuService menuService;
	
	@GetMapping("/list")
	public String list(Model model){
		model.addAttribute("menus",menuService.getAllParentMenuList());
		return "page/system/menu/list";
	}
	
	@PostMapping(path="/add")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object add(){
		return menuService.addMenu(this.getParameterMap(),this.getSession());
	}
	
	@GetMapping("/del/{menu_id}")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.DEL)
	public Object del(@PathVariable("menu_id") String menuId){
		return menuService.delMenu(menuId);
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit(){
		return menuService.editMenu(this.getParameterMap());
	}
	@GetMapping("/query/{menu_id}")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object find(@PathVariable("menu_id") String menuId){
		if(!Jurisdiction.buttonJurisdiction(qxurl, "query",this.getSession())){return ResponseModel.getNotAuthModel();} //校验权限
		return menuService.findMenu(menuId);
	}
	
	@GetMapping("/getSubMenu")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object getSubMenu(){
		if(!Jurisdiction.buttonJurisdiction(qxurl, "query",this.getSession())){return ResponseModel.getNotAuthModel();} //校验权限
		return menuService.getSubMenuList(this.getParameterMap());
	}
}
