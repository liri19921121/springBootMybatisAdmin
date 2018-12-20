
package com.admin.controller.coinInfo;


import com.admin.util.ParameterMap;
import com.admin.annotation.Permission;
import com.admin.entity.PermissionType;
import com.admin.service.goodsType.GoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.service.coinInfo.CoinInfoService;

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
@RequestMapping("/coinInfo")
public class CoinInfoController extends BaseController{
	
	private final static String qxurl="coinInfo/list";
	
	@Autowired
	private CoinInfoService coinInfoService;

	@Autowired
	private GoodsTypeService goodsTypeService;
	

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
					coinInfoService.selectList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			model.addAttribute("allLang",goodsTypeService.getAllLang(null));
			model.addAttribute("coinInfo", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("keyword", pm.getString("keyword"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/coinInfo/list";
	}
	
	@GetMapping(path="/detail")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object getDetail() throws Exception{
		return coinInfoService.getDetail(this.getParameterMap());
	}
	
	
	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit() throws Exception{
		return coinInfoService.edit(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping("/add")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object add() throws Exception{
		return coinInfoService.add(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping(path="/del")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object del() throws Exception{
		return coinInfoService.del(this.getParameterMap());
	}



}