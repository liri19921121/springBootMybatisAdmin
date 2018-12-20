
package com.admin.controller.userAccount;


import java.util.List;

import com.admin.util.ParameterMap;
import com.admin.service.coinInfo.CoinInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.service.userAccount.UserAccountService;

import com.admin.annotation.Permission;
import com.admin.entity.PermissionType;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.admin.controller.base.BaseController;
import com.admin.entity.system.Const;


/**
 * dev
 * @author rstyro
 * @version v1.0
 */

@Controller
@RequestMapping("/userAccount")
public class UserAccountController extends BaseController{
	
	private final static String qxurl="userAccount/list";
	
	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private CoinInfoService coinInfoService;

	@GetMapping(path={"/list/{page}","/list"})
	public Object list(@PathVariable(value="page",required=false)Integer pageNo,Model model){
		try {
			if(pageNo == null){
				pageNo=1;
			}
			ParameterMap pm = this.getParameterMap();

			List<ParameterMap> list = coinInfoService.getAllCoinName(null);
			if (pm.get("coin") == null){
				pm.put("coin","all");
				model.addAttribute("coin","all");
			}else{
				model.addAttribute("coin",pm.get("coin"));
			}
			pm.put("isDeleted","N");
			Page<ParameterMap> page = PageHelper.startPage(pageNo, Const.PAGE_SIZE).doSelectPage(() -> {
				try {
					userAccountService.getListByCode(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			model.addAttribute("allCoinName",list);
			model.addAttribute("userAccount", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("keyword", pm.getString("keyword"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/userAccount/list";
	}
	
	@GetMapping(path="/detail")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object getDetail() throws Exception{
		return userAccountService.getDetail(this.getParameterMap());
	}
	
	
	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit() throws Exception{
		return userAccountService.edit(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping("/add")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object add() throws Exception{
		return userAccountService.add(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping(path="/del")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object del() throws Exception{
		return userAccountService.del(this.getParameterMap());
	}
}