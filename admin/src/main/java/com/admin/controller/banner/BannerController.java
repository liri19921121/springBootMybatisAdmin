
package com.admin.controller.banner;


import com.admin.controller.base.BaseController;
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

import com.admin.service.banner.BannerService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.system.Const;


/**
 * dev
 * @author rstyro
 * @version v1.0
 */

@Controller
@RequestMapping("/banner")
public class BannerController extends BaseController {
	
	private final static String qxurl="banner/list";
	
	@Autowired
	private BannerService bannerService;
	

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
					bannerService.getByConditionList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			model.addAttribute("banner", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("keyword", pm.getString("keyword"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/banner/list";
	}
	
	@GetMapping(path="/detail")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object getDetail() throws Exception{
		return bannerService.selectByPrimary(this.getParameterMap());
	}


	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit(String pics,String isImgChange, Integer number ,String pictureName,String isOpen,String id,String type) throws Exception{
		ParameterMap pm = new ParameterMap();
		pm.put("id",id);
		pm.put("number",number);
		pm.put("pictureName",pictureName);
		pm.put("isOpen",isOpen);
		pm.put("isImgChange",isImgChange);
		pm.put("type",type);
		bannerService.edit(pics,pm,this.getSession());
		return ResponseModel.getModel("编辑成功","200",null);
	}

	@PostMapping("/add")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object add(String pics,Integer number ,String pictureName,String isOpen,String type) throws Exception{
		if (pics == null){
			return "请上传图片";
		}
		ParameterMap pm = new ParameterMap();
		pm.put("number",number);
		pm.put("pictureName",pictureName);
		pm.put("isOpen",isOpen);
		if (type == null){
			type = "zh_CN";
		}
		pm.put("type",type);
		bannerService.add(pics,pm,this.getSession());
		return ResponseModel.getModel(ResultEnum.SUCCESS, null);
	}

	@PostMapping(path="/del")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.DEL)
	public Object del() throws Exception{
//		if(!Jurisdiction.buttonJurisdiction(qxurl, "del",this.getSession())){return ResponseModel.getNotAuthModel();}
		return bannerService.del(this.getParameterMap());
	}
}