
package com.admin.controller.usrArea;


import java.util.LinkedHashMap;
import java.util.Map;

import com.admin.annotation.Permission;
import com.admin.controller.base.BaseController;
import com.admin.entity.PermissionType;
import com.admin.entity.system.Const;
import com.admin.entity.system.User;
import com.admin.service.appuser.AppUserService;
import com.admin.service.usrArea.UsrAreaService;
import com.admin.util.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 * dev
 * @author rstyro
 * @version v1.0
 */

@Controller
@RequestMapping("/usrArea")
public class UsrAreaController extends BaseController {
	
	private final static String qxurl="usrArea/list";
	
	@Autowired
	private UsrAreaService usrAreaService;

	@Autowired
	private AppUserService userService;
	

	@GetMapping(path={"/list/{page}","/list"})
	public Object list(@PathVariable(value="page",required=false)Integer pageNo,Model model){
		try {
			if(pageNo == null){
				pageNo=1;
			}
			ParameterMap pm = this.getParameterMap();
			pm.put("isDeleted","N");
			Page<ParameterMap> page = PageHelper.startPage(pageNo, Const.PAGE_SIZE).doSelectPage(() -> {
				try {
					usrAreaService.getPageList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			model.addAttribute("usrArea", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("areaName", pm.getString("areaName"));
			model.addAttribute("userName", pm.getString("userName"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/usrArea/list";
	}

	@GetMapping(path={"/gotoUserList/{page}","/gotoUserList"})
	public Object gotoUserList(@PathVariable(value="page",required=false)Integer pageNo,Model model){
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
			model.addAttribute("keyword", pm.getString("keyword"));
			User user = (User) this.getSession().getAttribute(Const.SESSION_USER);
			model.addAttribute("username",user.getUsername());

			model.addAttribute("areaName",pm.getString("areaName"));
			model.addAttribute("userId",pm.getString("userId"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/usrArea/userList";
	}
	
	@GetMapping(path="/detail")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object getDetail() throws Exception{
		return usrAreaService.getDetail(this.getParameterMap());
	}
	
	
	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit() throws Exception{
		return usrAreaService.edit(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping("/add")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object add() throws Exception{
		return usrAreaService.add(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping(path="/del")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object del() throws Exception{
		return usrAreaService.del(this.getParameterMap(),this.getSession());
	}

	@PostMapping("/createArea")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object createArea() throws Exception{
		return usrAreaService.createArea(this.getParameterMap(),this.getSession());
	}
}