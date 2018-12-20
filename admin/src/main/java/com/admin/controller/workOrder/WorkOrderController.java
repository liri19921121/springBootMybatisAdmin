
package com.admin.controller.workOrder;


import java.util.List;

import com.admin.util.ParameterMap;
import com.admin.entity.dto.WorkOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.service.workOrder.WorkOrderService;

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
@RequestMapping("/workOrder")
public class WorkOrderController extends BaseController{
	
	private String qxurl="workOrder/list";
	
	@Autowired
	private WorkOrderService workOrderService;
	

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
					workOrderService.getByConditionList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			model.addAttribute("workOrder", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("keyword", pm.getString("keyword"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/workOrder/list";
	}
	
	@GetMapping(path="/detail")
	@ResponseBody
	public List<WorkOrderDTO> getDetail() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(qxurl, "query",this.getSession())){
			/*return ResponseModel.getNotAuthModel();*/
		}
		List<WorkOrderDTO> list = workOrderService.selectWorkOrderList(Long.valueOf(this.getParameterMap().getString("id")));
		return list;
	}

	@GetMapping(path="/getDetailById")
	@ResponseBody
	public ParameterMap getDetail(String id) throws Exception{
		ParameterMap map = new ParameterMap();
		map.put("id",id);
		return workOrderService.getDetailById(map);
	}
	
	
	@PostMapping("/edit")
	@ResponseBody
	public Object edit() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(qxurl, "edit",this.getSession())){return ResponseModel.getNotAuthModel();} 
		return workOrderService.edit(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping("/add")
	@ResponseBody
	public Object add() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(qxurl, "add",this.getSession())){return ResponseModel.getNotAuthModel();} 
		return workOrderService.add(this.getParameterMap(),this.getSession());
	}

	@PostMapping("/replyContent")
	@ResponseBody
	public Object replyContent() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(qxurl, "add",this.getSession())){return ResponseModel.getNotAuthModel();}
		return workOrderService.addReplyContent(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping(path="/del")
	@ResponseBody
	public Object del() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(qxurl, "del",this.getSession())){return ResponseModel.getNotAuthModel();} 
		return workOrderService.del(this.getParameterMap());
	}
}