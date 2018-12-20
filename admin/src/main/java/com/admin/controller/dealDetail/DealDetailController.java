
package com.admin.controller.dealDetail;

import com.admin.service.dealDetail.DealDetailService;
import com.admin.util.ParameterMap;
import com.admin.annotation.Permission;
import com.admin.entity.PermissionType;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
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
import com.admin.controller.base.BaseController;
import com.admin.entity.system.Const;


/**
 * dev
 * @author rstyro
 * @version v1.0
 */

@Controller
@RequestMapping("/dealDetail")
public class DealDetailController extends BaseController{
	
	private final static String qxurl="dealDetail/list";
	
	@Autowired
	private DealDetailService dealDetailService;
	

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
					dealDetailService.getByConditionList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			model.addAttribute("dealDetail", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("keyword", pm.getString("keyword"));
			model.addAttribute("orderState", pm.getString("orderState"));
			model.addAttribute("appealState", pm.getString("appealState"));
			model.addAttribute("mold", pm.getString("mold"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/dealDetail/list";
	}
	
	@GetMapping(path="/detail")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object getDetail() throws Exception{
		return ResponseModel.getModel(ResultEnum.SUCCESS, dealDetailService.getByDetails(this.getParameterMap()));
	}
	
	
	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit() throws Exception{
		return dealDetailService.edit(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping("/add")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object add() throws Exception{
		return dealDetailService.add(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping(path="/del")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object del() throws Exception{
		return dealDetailService.del(this.getParameterMap());
	}

}