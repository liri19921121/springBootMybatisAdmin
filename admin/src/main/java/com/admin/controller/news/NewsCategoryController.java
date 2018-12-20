package com.admin.controller.news;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

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

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.admin.controller.base.BaseController;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.system.Const;
import com.admin.entity.system.User;
import com.admin.service.news.NewsCategoryService;
import com.admin.util.Jurisdiction;

@Controller
@RequestMapping("/newsCategory")
public class NewsCategoryController extends BaseController{
private final static String qxurl="newsCategory/list";
	
	@Autowired
	private NewsCategoryService newCategoryService;
	

	@GetMapping(path={"/list/{page}","/list"})
	public Object list(@PathVariable(value="page",required=false)Integer pageNo,Model model){
		try {
			if(pageNo == null){
				pageNo=1;
			}
			ParameterMap pm = this.getParameterMap();
			pm.put("isDeleted", "N");
			Page<ParameterMap> page = PageHelper.startPage(pageNo, Const.PAGE_SIZE).doSelectPage(() -> {
				try {
					newCategoryService.getByConditionList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			Map<String,String> languageList = new LinkedHashMap<>();
			languageList.put("zh_CN","中文");
			languageList.put("zh_TW","繁体");
			languageList.put("en_US","英文");
			languageList.put("ko_KR","韩文");
			languageList.put("ja_JP","日文");

			model.addAttribute("languageList",languageList);
			model.addAttribute("newsCategory", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("keyword", pm.getString("keyword"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/news/newsCategory";
	}
	
	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit(){
		if(!Jurisdiction.buttonJurisdiction(qxurl, "edit",this.getSession())){return ResponseModel.getNotAuthModel();} 
		try {
			User user = (User) this.getSession().getAttribute(Const.SESSION_USER);
			ParameterMap pm = this.getParameterMap();
			System.out.println("pm="+pm);
			pm.put("modifyTime", LocalDateTime.now());
			pm.put("modifyBy",user.getUserId());
			newCategoryService.updateByPrimaryKeySelective(pm);
			return ResponseModel.getModel(ResultEnum.SUCCESS, null);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}
	
	@PostMapping("/add")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object add(){
		if(!Jurisdiction.buttonJurisdiction(qxurl, "add",this.getSession())){return ResponseModel.getNotAuthModel();} 
		try {
			User user = (User) this.getSession().getAttribute(Const.SESSION_USER);
			ParameterMap pm = this.getParameterMap();
			System.out.println("pm="+pm);
			ParameterMap condition = new ParameterMap();
			condition.put("code", pm.getString("code"));
			condition.put("isDeleted", "N");
			ParameterMap repeatMap = newCategoryService.getByCondition(condition);
			if(repeatMap != null && !StringUtils.isEmpty(repeatMap.getString("id"))){
				return ResponseModel.getModel(ResultEnum.ALREADY_EXISTS, null);
			}
			pm.put("createTime", LocalDateTime.now());
			pm.put("createBy", user.getUserId());
			newCategoryService.insertSelective(pm);
			return ResponseModel.getModel(ResultEnum.SUCCESS, null);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}
	
	@PostMapping(path={"/del","/del/{id}"})
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.DEL)
	public Object del(@PathVariable(value="id",required=false) String id){
		if(!Jurisdiction.buttonJurisdiction(qxurl, "del",this.getSession())){return ResponseModel.getNotAuthModel();} 
		boolean isdel = false;
		try {
			String ids = id;
			if(id == null){
				ParameterMap pm = this.getParameterMap();
				ids = pm.getString("id");
			}
			isdel = newCategoryService.deleteNewsCategory(ids);
			return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}
}
