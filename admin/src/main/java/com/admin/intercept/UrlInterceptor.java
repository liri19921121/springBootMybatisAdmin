package com.admin.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.admin.annotation.Permission;
import com.admin.entity.ResponseModel;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.admin.entity.system.Const;
import com.admin.entity.system.User;
import com.admin.util.Jurisdiction;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class UrlInterceptor implements HandlerInterceptor{

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mv)
			throws Exception {
		//添加增删改查权限
		if(mv != null){
			mv.addObject(Const.SESSION_QX, request.getSession().getAttribute(Const.SESSION_QX));
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			HttpSession session = request.getSession();
			String path = request.getServletPath();
			User user =  (User) session.getAttribute(Const.SESSION_USER);
			if(null == user || "".equals(user)){
				response.sendRedirect("/");
				return false;
			}else{
				path = path.substring(1, path.length());
				boolean b = Jurisdiction.hasJurisdiction(path,session);
				if(!b){
					response.sendRedirect("/error/404");
					return b;
				}
			}

			//是否有权限
			if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				//获取controller注解， controller检查是否有权限控制
				Permission permission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Permission.class);
				if(!checkPermission(permission,request)){
					outputJson(response, ResponseModel.getNotAuthModel());
					return false;
				}
				//获取方法注解，方法检查是否有权限控制
				permission = handlerMethod.getMethod().getAnnotation(Permission.class);
				if(!checkPermission(permission,request)){
					outputJson(response, ResponseModel.getNotAuthModel());
					return false;
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	//校验是否有权限
	private boolean checkPermission(Permission permission,HttpServletRequest request) throws IOException {
		if (permission != null && !StringUtils.isEmpty(permission.url()) && !StringUtils.isEmpty(permission.type().getType())) {
			if(!Jurisdiction.buttonJurisdiction(permission.url(), permission.type().getType(),request.getSession())){
				return false;
			}
		}
		return true;
	}

	//json 字符串返回
	private void outputJson(HttpServletResponse response, HashMap<String,Object> model) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null ;
		String json = JSONObject.toJSON(model).toString();
		out = response.getWriter();
		out.append(json);
	}

}
