
package com.admin.controller.news;


import java.time.LocalDateTime;
import java.util.*;

import com.admin.controller.base.BaseController;
import com.admin.util.ParameterMap;
import com.admin.annotation.Permission;
import com.admin.entity.PermissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.service.news.NewsCategoryService;
import com.admin.service.news.NewsContentService;
import com.admin.service.news.NewsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.system.Const;
import com.admin.entity.system.User;


/**
 * 
 * @author rstyro
 * @version v1.0
 */

@Controller
@RequestMapping("/news")
public class NewsController extends BaseController {
	
	private final static String qxurl="news/list";
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private NewsCategoryService newCategoryService;
	
	@Autowired
	private NewsContentService newsContentService;

	@Value("${home.url}")
	public String homeUrl;
	

	@GetMapping(path={"/list/{page}","/list"})
	public Object list(@PathVariable(value="page",required=false)Integer pageNo,Model model){
		try {
			if(pageNo == null){
				pageNo=1;
			}
			ParameterMap pm = this.getParameterMap();
			Map<String,String> languageList = new LinkedHashMap<>();
			languageList.put("zh_CN","中文");
			languageList.put("zh_TW","繁体");
			languageList.put("en_US","英文");
			languageList.put("ko_KR","韩文");
			languageList.put("ja_JP","日文");
			pm.put("isDeleted", "N");
			Page<ParameterMap> page = PageHelper.startPage(pageNo, Const.PAGE_SIZE).doSelectPage(() -> {
				try {
					newsService.getByConditionList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			model.addAttribute("news", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("categorys", newCategoryService.getByConditionList(pm));
			model.addAttribute("categoryCode", pm.getString("categoryCode"));
			model.addAttribute("languageList",languageList);
			model.addAttribute("keyword", pm.getString("keyword"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/news/list";
	}
	
	@GetMapping(path={"/toAdd"})
	public Object toAdd(Model model) throws Exception{
		ParameterMap pm = this.getParameterMap();
		Map<String,String> languageList = new LinkedHashMap<>();
		languageList.put("zh_CN","中文");
		languageList.put("zh_TW","繁体");
		languageList.put("en_US","英文");
		languageList.put("ko_KR","韩文");
		languageList.put("ja_JP","日文版");

		model.addAttribute("categorys", newCategoryService.getByConditionList(pm));
		model.addAttribute("btnName", "发布");
		model.addAttribute("id", "");
		model.addAttribute("homeUrl", homeUrl);
		model.addAttribute("languageList",languageList);
		model.addAttribute("actionurl", "/news/add");
		return "page/news/edit";
	}
	
	@GetMapping(path={"/toEdit"})
	public Object toEdit(Model model) throws Exception{
		ParameterMap pm = this.getParameterMap();
		Map<String,String> languageList = new LinkedHashMap<>();
		languageList.put("zh_CN","中文");
		languageList.put("zh_TW","繁体");
		languageList.put("en_US","英文");
		languageList.put("ko_KR","韩文");
		languageList.put("ja_JP","日文");

		ParameterMap detail = newsService.getByCondition(pm);
		model.addAttribute("news", detail);
		model.addAttribute("categorys", newCategoryService.getByConditionList(new ParameterMap()));
		model.addAttribute("btnName", "更改");
		model.addAttribute("id", pm.getString("id"));
		model.addAttribute("languageList",languageList);
		model.addAttribute("homeUrl", homeUrl);
		model.addAttribute("actionurl", "/news/edit");
		return "page/news/edit";
	}
	
	@GetMapping(path={"/view","/view/{id}"})
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object getDetail(@PathVariable(value="id",required=false) String id,Model model){
		ParameterMap detail = null;
		try {
			ParameterMap pm = this.getParameterMap();
			if(id != null){
				pm.put("id", id);
			}
			detail = newsService.getByCondition(pm);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		model.addAttribute("news", detail);
		return "page/news/view";
	}
	
	
	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit(){
		try {
			User user = (User) this.getSession().getAttribute(Const.SESSION_USER);
			ParameterMap pm = this.getParameterMap();
			System.out.println("pm="+pm);
			pm.put("modifyTime", LocalDateTime.now());
			pm.put("modifyBy",user.getUserId());
			newsService.updateByPrimaryKeySelective(pm);
			ParameterMap contentPm = new ParameterMap();
			contentPm.put("newsId", pm.getString("id"));
			contentPm.put("content", pm.getString("content"));
			contentPm.put("modifyTime", LocalDateTime.now());
			contentPm.put("modifyBy",user.getUserId());
			newsContentService.updateByPrimaryKeySelective(contentPm);
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
		try {
			User user = (User) this.getSession().getAttribute(Const.SESSION_USER);
			ParameterMap pm = this.getParameterMap();
			System.out.println("pm="+pm);
			pm.put("createTime", LocalDateTime.now());
			pm.put("createBy", user.getUserId());
			newsService.insertSelective(pm);
			ParameterMap contentPm = new ParameterMap();
			contentPm.put("newsId", pm.getString("id"));
			contentPm.put("content", pm.getString("content"));
			contentPm.put("createTime", LocalDateTime.now());
			contentPm.put("createBy",user.getUserId());
			newsContentService.insertSelective(contentPm);
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
	public Object del(Long id){
		boolean isdel = false;
		try {
			isdel = newsService.deleteNew(id);
			return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}
}