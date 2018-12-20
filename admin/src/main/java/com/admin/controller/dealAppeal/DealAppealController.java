
package com.admin.controller.dealAppeal;


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

import com.admin.service.dealAppeal.DealAppealService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.admin.controller.base.BaseController;
import com.admin.entity.system.Const;
import com.admin.util.ParameterMap;


/**
 * dev
 * @author rstyro
 * @version v1.0
 */

@Controller
@RequestMapping("/dealAppeal")
public class DealAppealController extends BaseController{
	
	private final static String qxurl="dealAppeal/list";
	
	@Autowired
	private DealAppealService dealAppealService;
	

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
					dealAppealService.getByConditionList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			model.addAttribute("dealAppeal", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("keyword", pm.getString("keyword"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/dealAppeal/list";
	}
	
	@GetMapping(path="/detail")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object getDetail() throws Exception{
		return dealAppealService.getDetail(this.getParameterMap());
	}
	
	
	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit() throws Exception{
		return dealAppealService.edit(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping("/add")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object add() throws Exception{
		return dealAppealService.add(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping(path="/del")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object del() throws Exception{
		return dealAppealService.del(this.getParameterMap());
	}

	@PostMapping("/dispose")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object disPose() throws Exception{
		return dealAppealService.disPose(this.getParameterMap(),this.getSession());
	}
}