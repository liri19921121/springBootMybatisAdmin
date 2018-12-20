package com.admin.entity;

public enum ResultEnum {
	SUCCESS("200","成功"),
	ALREADY_EXISTS("201","已存在相同的数据"),
	ALREADY_EXISTS_CODE("203","已存在相同的币种"),
	ALREADY_EXISTS_CONFIG("204","已存在相同的配置"),
	NON_EXISTENT_CODE("205","商户不存在"),
	
	FAILED("400", "失败"),
	PARAMETER_INCOMPLETE ("401", "参数不全"),
	NOT_AUTH("403","没有权限"),
	LOCK_ACCOUNT("406","账户已锁定"),
	TIMEOUT("407", "请求超时，请稍后重试"),
	UNABLE_DELETE("408", "该币种已被购买无法删除"),
	NOT_PAY_LOG("409", "没有找到付款记录"),
	REQUEST_WITHDRAW_FAILED("410", "请求提币失败"),
	USER_EXISTENT("411", "找不到用户会员信息"),

	
	ERROR("500", "服务器异常"),
	ERROR_INCORRECT_NAMEORPASSWORD("501","用户名或密码错误"),
	;
	private String status;
	private String msg;
	private ResultEnum(String status, String msg) {
		this.status = status;
		this.msg = msg;
	}
	public String getStatus() {
		return status;
	}
	public String getMsg() {
		return msg;
	}
	
}
