/*

package com.otcbi.admin.controller.notice;


import java.time.LocalDateTime;

import Permission;
import PermissionType;
import NewsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.otcbi.admin.service.notice.NoticeService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import BaseController;
import ResponseModel;
import ResultEnum;
import Const;
import User;
import Jurisdiction;
import ParameterMap;


*/
/**
 * dev
 * @author rstyro
 * @version v1.0
 *//*


@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController{
	
	private final static String qxurl="notice/list";
	
	@Autowired
	private NoticeService noticeService;

	@Autowired
	private NewsCategoryService newCategoryService;

	@Value("${home.url}")
	public String homeUrl;
	

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
					*/
/*noticeService.getNoticeList(pm);*//*

				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			model.addAttribute("notice", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("keyword", pm.getString("keyword"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/notice/list";
	}
	
	@GetMapping(path="/detail")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object getDetail() throws Exception{
		return noticeService.getDetail(this.getParameterMap());
	}
	
	
	@PostMapping("/edit")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.EDIT)
	public Object edit() throws Exception{
		return noticeService.edit(this.getParameterMap(),this.getSession());
	}
	
	@PostMapping(path="/del")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.QUERY)
	public Object del() throws Exception{
		return noticeService.del(this.getParameterMap());
	}

	@GetMapping(path={"/toAdd"})
	public Object toAdd(Model model) throws Exception{
		ParameterMap pm = this.getParameterMap();
		model.addAttribute("categorys", newCategoryService.getByConditionList(pm));
		model.addAttribute("homeUrl", homeUrl);
		model.addAttribute("actionurl", "/notice/add");
		return "page/notice/edit";
	}

	@PostMapping("/add")
	@ResponseBody
	@Permission(url = qxurl,type = PermissionType.ADD)
	public Object add(){
		try {
			User user = (User) this.getSession().getAttribute(Const.SESSION_USER);
			ParameterMap pm = this.getParameterMap();
			System.out.println("pm="+pm);
			*/
/*pm.put("createTime", LocalDateTime.now());
			pm.put("createBy", user.getUserId());
			newsService.insertSelective(pm);
			ParameterMap contentPm = new ParameterMap();
			contentPm.put("newsId", pm.getString("id"));
			contentPm.put("content", pm.getString("content"));
			contentPm.put("createTime", LocalDateTime.now());
			contentPm.put("createBy",user.getUserId());
			newsContentService.insertSelective(contentPm);*//*

			return ResponseModel.getModel(ResultEnum.SUCCESS, null);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}

}*/
