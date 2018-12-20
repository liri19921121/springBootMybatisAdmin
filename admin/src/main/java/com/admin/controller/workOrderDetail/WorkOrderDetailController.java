
package com.admin.controller.workOrderDetail;


import com.admin.util.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.service.workOrderDetail.WorkOrderDetailService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.admin.controller.base.BaseController;
import com.admin.entity.ResponseModel;
import com.admin.entity.system.Const;
import com.admin.util.Jurisdiction;


/**
 * dev
 * @author rstyro
 * @version v1.0
 */

@Controller
@RequestMapping("/workOrderDetail")
public class WorkOrderDetailController extends BaseController{
	
	private String qxurl="workOrderDetail/list";
	
	@Autowired
	private WorkOrderDetailService workOrderDetailService;
	

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
					workOrderDetailService.getByConditionList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			model.addAttribute("workOrderDetail", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("keyword", pm.getString("keyword"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/workOrderDetail/list";
	}
	
	@GetMapping(path="/detail")
	@ResponseBody
	public Object getDetail() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(qxurl, "query",this.getSession())){return ResponseModel.getNotAuthModel();} 
		return workOrderDetailService.getDetail(this.getParameterMap());
	}
	
	
	@PostMapping("/edit")
	@ResponseBody
	public Object edit() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(qxurl, "edit",this.getSession())){return ResponseModel.getNotAuthModel();} 
		return workOrderDetailService.edit(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping("/add")
	@ResponseBody
	public Object add() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(qxurl, "add",this.getSession())){return ResponseModel.getNotAuthModel();} 
		return workOrderDetailService.add(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping(path="/del")
	@ResponseBody
	public Object del() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(qxurl, "del",this.getSession())){return ResponseModel.getNotAuthModel();} 
		return workOrderDetailService.del(this.getParameterMap());
	}
}