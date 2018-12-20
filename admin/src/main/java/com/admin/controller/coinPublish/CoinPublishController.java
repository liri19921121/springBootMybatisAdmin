
package com.admin.controller.coinPublish;


import java.util.List;

import com.admin.util.ParameterMap;
import com.alibaba.fastjson.JSON;
import com.admin.annotation.Permission;
import com.admin.entity.PermissionType;
import com.admin.service.coinInfo.CoinInfoService;
import com.admin.service.coinLang.CoinLangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.service.coinPublish.CoinPublishService;

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
@RequestMapping("/coinPublish")
public class CoinPublishController extends BaseController{
	
	private final static String qxurl="coinPublish/list";
	
	@Autowired
	private CoinPublishService coinPublishService;

	@Autowired
	private CoinInfoService coinInfoService;

	@Autowired
	private CoinLangService coinLangService;
	

	@GetMapping("/list")
	public Object list(@PathVariable(value="page",required=false)Integer pageNo,Model model){
		try {
			if(pageNo == null){
				pageNo=1;
			}
			ParameterMap pm = this.getParameterMap();
			pm.put("isDeleted","N");
			Page<ParameterMap> page = PageHelper.startPage(pageNo, Const.PAGE_SIZE).doSelectPage(() -> {
				try {
					coinPublishService.getByConditionList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			System.out.println("============================"+JSON.toJSONString(page.getResult()));
			model.addAttribute("coinPublish", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("keyword", pm.getString("keyword"));
			model.addAttribute("publishState", pm.getString("publishState"));
			model.addAttribute("allCoinName",coinInfoService.getAllCoinName(null));
			model.addAttribute("allLang",coinLangService.getAllLang(null));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/coinPublish/list";
	}
	
	@GetMapping(path="/detail")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object getDetail() throws Exception{
		return coinPublishService.getDetail(this.getParameterMap());
	}

	@GetMapping(path="/getHaveLang")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object getHaveLang(String raiseId) throws Exception{
		return coinPublishService.getHaveLang(raiseId);
	}
	
	
	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit() throws Exception{
		return coinPublishService.edit(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping("/add")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object add() throws Exception{
		return coinPublishService.add(this.getParameterMap(),this.getSession());
	}

	@PostMapping("/addLangVersion")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object addLangVersion() throws Exception{
		return coinPublishService.addLangVersion(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping(path="/del")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object del() throws Exception{
		return coinPublishService.del(this.getParameterMap(),this.getSession());
	}

	@GetMapping(path={"getAllCoinName"})
	@ResponseBody
	public List<ParameterMap> getAllCoinName(@PathVariable(value="page",required=false)Integer pageNo, Model model) throws Exception{
		return coinInfoService.getAllCoinName(null);
	}
}