package com.admin.service.system;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.admin.util.ParameterMap;
import org.springframework.stereotype.Component;

import com.admin.entity.system.User;

@Component
public interface IUserService {
	public HashMap<String, Object> login(ParameterMap pm, HttpSession session);
	public String logout(HttpSession session);
	public List<ParameterMap> getUserList();
	public User getUserInfo(ParameterMap pm);
	public HashMap<String, Object> getRole(ParameterMap pm);
	public HashMap<String, Object> add(ParameterMap pm,HttpSession session);
	public HashMap<String, Object> edit(ParameterMap pm);
	public HashMap<String, Object> editRole(ParameterMap pm);
	public HashMap<String, Object> del(ParameterMap pm);
}
