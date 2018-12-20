
package com.admin.controller.dealPublish;


import com.admin.util.ParameterMap;
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

import com.admin.service.dealPublish.DealPublishService;

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
@RequestMapping("/dealPublish")
public class DealPublishController extends BaseController{
	
	private final static String qxurl="dealPublish/list";
	
	@Autowired
	private DealPublishService dealPublishService;
	

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
					dealPublishService.getByConditionList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			model.addAttribute("dealPublish", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("totalNumber", page.size());
			model.addAttribute("keyword", pm.getString("keyword"));
			model.addAttribute("mold", pm.getString("mold"));
			model.addAttribute("putawayState", pm.getString("putawayState"));
			model.addAttribute("advertisingState", pm.getString("advertisingState"));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/dealPublish/list";
	}
	
	@GetMapping(path="/detail")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object getDetail() throws Exception{
		return dealPublishService.getDetail(this.getParameterMap());
	}
	
	
	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit() throws Exception{
		return dealPublishService.edit(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping("/add")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object add() throws Exception{
		return dealPublishService.add(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping(path="/del")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object del() throws Exception{
		return dealPublishService.del(this.getParameterMap());
	}


	/**
	 * 撤单
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/cancelPublish")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object cancelPublish() throws Exception {
		return dealPublishService.cancelPublish(this.getParameterMap(),this.getSession());
	}

	/**
	 * 根据搜索条件批量撤单
	 * @return
	 */
	@PostMapping("/cancelPublishBySearch")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object cancelPublishBySearch() throws Exception{
		return dealPublishService.cancelPublishBySearch(this.getParameterMap(),this.getSession());
	}
}