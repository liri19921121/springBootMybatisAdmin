
package com.admin.controller.coinDetail;


import com.admin.util.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.admin.service.coinDetail.CoinDetailService;

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
@RequestMapping("/coinDetail")
public class CoinDetailController extends BaseController{
	
	private final static String qxurl="coinDetail/list";
	
	@Autowired
	private CoinDetailService coinDetailService;
	

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
					coinDetailService.getByConditionList(pm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			model.addAttribute("coinDetail", page.getResult());
			model.addAttribute("page", page);
			model.addAttribute("keyword", pm.getString("keyword"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return "page/coinDetail/list";
	}
	

}