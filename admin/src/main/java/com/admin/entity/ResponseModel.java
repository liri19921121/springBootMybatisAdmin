package com.admin.entity;

import java.util.HashMap;


public class ResponseModel{

	private static HashMap<String,Object> model=null;
	
	private ResponseModel() {}
	
	public static HashMap<String, Object> getModel(ResultEnum result,Object data){
		model = new HashMap<>();
		model.put("status", result.getStatus());
		model.put("message", result.getMsg());
		model.put("data", data);
		return model;
	}
	
	public static HashMap<String, Object> getModel(String msg,String status,Object data){
		model = new HashMap<>();
		model.put("message", msg);
		model.put("status", status);
		model.put("data", data);
		return model;
	}
	
	public static HashMap<String, Object> getErrorModel(){
		model = new HashMap<>();
		model.put("status", "failed");
		model.put("message", "你请求的是一个冒牌接口");
		return model;
	}
	public static HashMap<String, Object> getNotAuthModel(){
		model = new HashMap<>();
		model.put("status", "notauth");
		model.put("message", "你权限不足");
		return model;
	}
	
}
