
package com.admin.service.banner.impl;

import com.admin.dao.banner.BannerDao;
import com.admin.service.picture.PictureService;
import com.admin.util.ParameterMap;
import com.alibaba.druid.util.Base64;
import com.alibaba.druid.util.StringUtils;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.picture.Picture;
import com.admin.entity.system.Const;
import com.admin.entity.system.User;
import com.admin.service.banner.BannerService;
import com.admin.util.ImgUtil;
import com.admin.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional(readOnly = true)
public class BannerServiceImpl implements BannerService {
	
	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private BannerDao bannerMapper;

	@Autowired
	private PictureService pictureService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static Set<String> picPrefix = new HashSet<String>();

	static {
		picPrefix.add(".jpg");
		picPrefix.add(".png");
	}

	@Value("${upload.root.folder}")
	public String root_fold;

	@Value("${img.folder}")
	public String img_fold;

	@Value("${home.url}")
	public String home_url;

	@Transactional(readOnly = false)
	 public  int deleteByPrimaryKey(Long bannerId){
		 return  bannerMapper.deleteByPrimaryKey(bannerId);
	 }
	
	@Transactional(readOnly = false)
	 public   int insert(ParameterMap pm){
		 return bannerMapper.insert(pm);
	 }
	
	@Transactional(readOnly = false)
	 public  int insertSelective(ParameterMap pm){
		 return bannerMapper.insertSelective(pm);
	 }

	@Transactional(readOnly = false)
	public  int insertPictureSelective(ParameterMap pm){
		return bannerMapper.insertPictureSelective(pm);
	}


	 public   ParameterMap selectByPrimaryKey(Long bannerId){
		 return bannerMapper.selectByPrimaryKey(bannerId);
	 }

	public ParameterMap selectByPrimary(ParameterMap pm)throws Exception{
		pm.put("homeUrl",home_url);
		return bannerMapper.selectByPrimary(pm);
	}

	@Transactional(readOnly = false)
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  bannerMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional(readOnly = false)
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  bannerMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional(rollbackFor=Exception.class)  
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.bannerMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.bannerMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional(rollbackFor=Exception.class)
	public boolean deleteBanner(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.bannerMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.bannerMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		argmap.put("homeUrl", home_url);
		return this.bannerMapper.getByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.bannerMapper.getByConditionCount(pm);
	}
	
	@Transactional(readOnly = false)
	public Object add(String pics,ParameterMap pm, HttpSession session) throws Exception {
		User user = (User) session.getAttribute(Const.SESSION_USER);
		int picId = Integer.valueOf(this.uploadImg(pics,"img",user).get("picId").toString());
		try {
			if(StringUtils.isEmpty(pm.getString("id"))){
				pm.remove("id");
			}
			pm.put("imgId",picId);
			pm.put("createTime", LocalDateTime.now());
			pm.put("createBy", ((User) session.getAttribute(Const.SESSION_USER)).getUserId());
			this.insertSelective(pm);
			return ResponseModel.getModel(ResultEnum.SUCCESS, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}


	@Transactional(readOnly = false)
	public HashMap<String, Object> uploadImg(String pics, String type,User user){
		Long pid = 0L;
		try {
			pics = this.replaceBase64Before(pics.toString());
			byte[] bytes = Base64.base64ToByteArray(pics.toString());
			InputStream in = new ByteArrayInputStream(bytes);
			//生成新的名称，防止名字重复
			String newName = Tools.random(8)+".png";
			String filePath = img_fold+newName;
			String userPath = ImgUtil.uploadImg(root_fold,filePath, in);

			Picture picture = new Picture();
			picture.setCreateTime(new Date());
			picture.setCreateBy(Long.valueOf(user.getUserId()));
			picture.setPicName(newName);
			picture.setPicType("1");
			picture.setPicPath(userPath);
			pid = 0L;

			//保存图片
			pictureService.insertSelective(picture);
			pid = picture.getPicId();
			System.out.println("upload  success");
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, "");
		}
			HashMap<String, Object> map = new HashMap();
			map.put("picId",pid);
			return map;
	}

	/**
	 * 替换base64的前缀
	 * @param pics
	 * @return
	 */
	public static String replaceBase64Before(String pics){
		pics = pics.replace("data:image/png;base64,", "");
		pics = pics.replace("data:image/jpeg;base64,", "");
		pics = pics.replace("data:image/bmp;base64,", "");
		pics = pics.replace("data:image/x-icon;base64,", "");
		pics = pics.replace("data:image/gif;base64,", "");
		return pics;
	}

	@Override
	public Object getDetail(ParameterMap pm) throws Exception {
		try {
			ParameterMap detail = null;
			detail = this.selectByPrimaryKey(Long.valueOf(pm.getString("id")));
			return ResponseModel.getModel(ResultEnum.SUCCESS, detail);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}

	@Transactional(readOnly = false)
	public Object edit(String file,ParameterMap pm, HttpSession session) throws Exception {
		User user = (User) session.getAttribute(Const.SESSION_USER);
		if (pm.get("isImgChange") != null && pm.get("isImgChange").toString().equals("isImgChange")){
			int picId = Integer.valueOf(this.uploadImg(file,"img",user).get("picId").toString());
			pm.put("imgId",picId);
		}
		try {

			pm.put("modifyTime", LocalDateTime.now());
			pm.put("modifyBy",user.getUserId());
			this.updateByPrimaryKeySelective(pm);
			return ResponseModel.getModel(ResultEnum.SUCCESS, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}

	@Transactional(readOnly = false)
	public Object del(ParameterMap pm) throws Exception {
		boolean isdel = false;
		try {
			String ids = pm.getString("id");
			isdel = this.deleteBanner(ids);
			return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}
	
}
