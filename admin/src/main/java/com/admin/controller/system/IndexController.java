package com.admin.controller.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.controller.base.BaseController;
import com.admin.entity.system.Const;
import com.admin.entity.system.Menu;
import com.admin.entity.system.User;
import com.admin.service.system.IUserService;

@Controller
public class IndexController extends BaseController{
	
	@Value("${admin.name}")
	private String adminName;
	
	@Autowired
	private IUserService userService;

	@Value("${home.url}")
	public String homeUrl;
	
	/**
	 * 入口
	 * @return
	 */
	@GetMapping(path={"/","/toLogin"})
	public String toLogin(){
		return "login";
	}

	/**
	 * 首页
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/index")
	public String index(Model model){
		try {
			List<Menu> allMenu = (List<Menu>) this.getSession().getAttribute(Const.SESSION_ALL_MENU);
			if(allMenu != null){
				model.addAttribute("menus", allMenu);
			}
			String userPath = homeUrl+((User)this.getSession().getAttribute(Const.SESSION_USER)).getPicPath();
			log.info("userPath="+userPath);
			model.addAttribute("adminName", adminName);
			model.addAttribute("homeUrl", homeUrl);
			model.addAttribute("userName", ((User)this.getSession().getAttribute(Const.SESSION_USER)).getNickName());
			model.addAttribute("userPath", userPath);
			model.addAttribute("userStatus", "在线");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
	
	
	/**
	 * 用户登录
	 * @return
	 */
	@PostMapping(path={"/login"})
	@ResponseBody
	public Object login(HttpServletRequest request){
		return userService.login(this.getParameterMap(), request.getSession());
	}
	
	/**
	 * 用户注销
	 * @return
	 */
	@GetMapping("/logout")
	public String logout(){
		return userService.logout(this.getSession());
	}
	
	
}
