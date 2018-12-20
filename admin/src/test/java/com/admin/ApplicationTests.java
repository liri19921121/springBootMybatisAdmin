package com.admin;

import java.util.List;

import com.admin.service.system.impl.MenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.admin.entity.system.Menu;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
	MenuService menuService;

    @Test
    public void testPage() throws Exception{
//    	System.out.println("menus="+menus);
//    	PageHelper.startPage(1, 5);
//    	List<Menu> menus = menuService.getAllMenuList();
//    	System.out.println("menus:"+menus.get(0).toString());
//    	System.out.println("menus:"+menus);
//    	System.out.println("menus.size:"+menus.size());

    	Page<Menu> page = PageHelper.startPage(1, 10).doSelectPage(() -> menuService.getAllParentMenuList());
    	List<Menu> menus = page.getResult();
    	System.out.println("page:"+page);
    	System.out.println("page.getResult:"+menus.get(0));
    }
    
    
    @Autowired
    private RestTemplate restTemplate;


    @Test
    public void testDel(){
    	try {
//    		cacheService.delKey("CONFIG_abc_min_amount_ETH");
    		System.out.println("=========success=============");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    

    

    
}
