
package com.admin.service.coinPublish.impl;

import com.admin.dao.coinPublish.CoinPublishDao;
import com.admin.service.coinPublish.CoinPublishService;
import com.admin.util.ParameterMap;
import com.alibaba.druid.util.Base64;
import com.admin.service.coinLang.CoinLangService;
import com.admin.util.ImgUtil;
import com.admin.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.druid.util.StringUtils;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.system.Const;
import com.admin.entity.system.User;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;

/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional
public class CoinPublishServiceImpl implements CoinPublishService {

	@Value("${home.url}")
	public String pre;

	@Value("${raise.folder}")
	public String raise_coin_folder;

	@Value("${upload.root.folder}")
	public String root_fold;

	@Value("${img.folder}")
	public String img_fold;

	/**
	 * this loger  can be used by service in anywhere . wzz
	 */
	@Autowired
	private CoinPublishDao coinPublishMapper;

	@Autowired
	private CoinLangService coinLangService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Transactional
	 public  int deleteByPrimaryKey(Long coinPublishId){
		 return  coinPublishMapper.deleteByPrimaryKey(coinPublishId);
	 }
	
	@Transactional
	 public   int insert(ParameterMap pm){
		 return coinPublishMapper.insert(pm);
	 }
	
	@Transactional
	 public  int insertSelective(ParameterMap pm){
		 return coinPublishMapper.insertSelective(pm);
	 }

	 public   ParameterMap selectByPrimaryKey(Long coinPublishId){
		 return coinPublishMapper.selectByPrimaryKey(coinPublishId);
	 }
	 
	@Transactional
	 public  int updateByPrimaryKeySelective(ParameterMap pm){
		 return  coinPublishMapper.updateByPrimaryKeySelective(pm);
	 }

	@Transactional
	 public  int updateByPrimaryKey(ParameterMap pm){
		 return  coinPublishMapper.updateByPrimaryKey(pm);
	 }
	 
	@Transactional
	public boolean saveOrUpdate(ParameterMap pm,String operateType) throws Exception{
		boolean flag = false;
		
		if("add".equals(operateType)){
			flag = this.coinPublishMapper.insert(pm) > 0 ? true : false;
		}
		
		if("edit".equals(operateType)){
			flag = this.coinPublishMapper.updateByPrimaryKey(pm) > 0 ? true : false;
		}
		
		return flag;
	}

	@Transactional
	public boolean deleteCoinPublish(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.coinPublishMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public ParameterMap getByCondition(ParameterMap pm) throws Exception {
		return this.coinPublishMapper.getByCondition(pm);
	}

	
	public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
		argmap.put("homeUrl",pre);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date());
		argmap.put("nowTime",date);
		return this.coinPublishMapper.getByConditionList(argmap);
	}
	
	public long getByConditionCount(ParameterMap pm) throws Exception {
		return this.coinPublishMapper.getByConditionCount(pm);
	}
	
	@Transactional
	public Object add(ParameterMap pm,HttpSession session) throws Exception {
		try {
			pm = savePicture(pm);
			if(StringUtils.isEmpty(pm.getString("id"))){
				pm.remove("id");
			}
			//判断时间
			if (!judge(pm).equals("success")){
				return ResponseModel.getModel(judge(pm),"666",null);
			}
			pm.put("createTime", LocalDateTime.now());
			pm.put("createBy", ((User) session.getAttribute(Const.SESSION_USER)).getUserId());
			pm.put("isDeleted","N");
			this.insertSelective(pm);

			Object raiseId = pm.get("id");
			Object title = pm.get("title");
			Object depict = pm.get("depict");
			pm.clear();
			pm.put("raiseId",raiseId);
			pm.put("title",title);
			pm.put("depict",depict);
			pm.put("isDeleted","N");
			pm.put("createTime",new Date());
			pm.put("createBy", ((User) session.getAttribute(Const.SESSION_USER)).getUserId());
			pm.put("lang","zh_CN");
			coinLangService.insertSelective(pm);
			return ResponseModel.getModel(ResultEnum.SUCCESS, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}

	private String judge(ParameterMap pm) throws Exception{
		if (Long.valueOf(pm.get("showNumber").toString()) > Long.valueOf(pm.get("number").toString())){
			return "发布数量不能大于最大数量";
		}
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date preheatDate = fmt.parse(pm.get("preheatTime").toString());
		Date beginDate = fmt.parse(pm.get("publishBeginTime").toString());
		Date endDate = fmt.parse(pm.get("publishEndTime").toString());
		if (beginDate.getTime() > endDate.getTime()){
			return "开始时间不能大于结束时间";
		}
		if (preheatDate.getTime() > beginDate.getTime()){
			return "预热时间不能大于开始时间";
		}
		return "success";
	}

	public ParameterMap savePicture(ParameterMap pm) throws Exception{
		Object pics = pm.get("pics");
		if (pics != null){
			pics = this.replaceBase64Before(pics.toString());
			byte[] bytes = Base64.base64ToByteArray(pics.toString());
			InputStream in = new ByteArrayInputStream(bytes);
			//生成新的名称，防止名字重复
			String newName = Tools.random(8)+".png";
			String filePath = raise_coin_folder+newName;
			String userPath = ImgUtil.uploadImg(root_fold,filePath, in);
			pm.put("headPath",userPath);
		}
		return pm;
	}


	@Override
	public Object getDetail(ParameterMap pm) throws Exception {
		try {
			ParameterMap detail = null;
			detail = this.selectByPrimaryKey(Long.valueOf(pm.getString("id")));
			detail.put("head_path",pre+detail.get("head_path"));
			return ResponseModel.getModel(ResultEnum.SUCCESS, detail);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}

	@Transactional
	public Object edit(ParameterMap pm, HttpSession session) throws Exception {
		try {

			//判断时间
			if (!judge(pm).equals("success")){
				return ResponseModel.getModel(judge(pm),"666",null);
			}
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date endDate = fmt.parse(pm.get("publishEndTime").toString());
			if (new Date().getTime() > endDate.getTime()){
				return ResponseModel.getModel("项目已结束不能编辑","666",null);
			}

			User user = (User) session.getAttribute(Const.SESSION_USER);
			if (pm.get("isImgChange") != null && pm.get("isImgChange").toString().equals("isImgChange")){
				pm = savePicture(pm);
			}
			pm.put("modifyTime", LocalDateTime.now());
			pm.put("modifyBy",user.getUserId());
			this.updateByPrimaryKeySelective(pm);

			ParameterMap langPm = new ParameterMap();
			int index = 0;
			if (pm.get("title") != null && !pm.get("title").equals("")){
				index = index + 1;
				langPm.put("title",pm.get("title"));
			}
			if (pm.get("depict") != null && !pm.get("depict").equals("")){
				index = index + 1;
				langPm.put("depict",pm.get("depict"));
			}
			if (index > 0){
				langPm.put("raiseId",pm.get("id"));
				langPm.put("lang","zh_CN");
				langPm.put("modifyTime",new Date());
				langPm.put("modifyBy", ((User) session.getAttribute(Const.SESSION_USER)).getUserId());
				coinLangService.updateByPrimaryKeySelective(langPm);
			}
			return ResponseModel.getModel(ResultEnum.SUCCESS, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, null);
		}
	}

	@Transactional
	public Object del(ParameterMap pm,HttpSession session) throws Exception {
		boolean isdel = false;
		try {
			String ids = pm.getString("id");
			ParameterMap map = new ParameterMap();
			map.put("id",ids);
			map.put("isDeleted","Y");
			map.put("modifyTime",new Date());
			map.put("modifyBy",((User) session.getAttribute(Const.SESSION_USER)).getUserId());
			this.updateByPrimaryKeySelective(map);
			return ResponseModel.getModel(ResultEnum.SUCCESS, Boolean.TRUE);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return ResponseModel.getModel(ResultEnum.ERROR, isdel);
		}
	}


	public Object  getHaveLang(Object raiseId) throws Exception{
		ParameterMap pm = new ParameterMap();
		pm.put("raiseId",raiseId);
		List<ParameterMap> list = this.coinPublishMapper.getHaveLang(pm);
		String haveLang = "";
		for (ParameterMap map : list){
			haveLang = haveLang + map.get("chinese_name").toString()+",";
		}
		HashMap<String,Object> map = new HashMap<>();
		map.put("list",this.getAllVersionLang(pm));
		map.put("haveLang",haveLang.substring(0,haveLang.length()-1));
		return map;
	}

	@Transactional
	public Object addLangVersion(ParameterMap pm, HttpSession session) throws  Exception{
		if (pm.get("raiseId") == null || pm.get("raiseId").toString().equals("")
				|| pm.get("title") == null || pm.get("title").toString().equals("")
				|| pm.get("depict") == null || pm.get("depict").toString().equals("")
				|| pm.get("lang") == null || pm.get("lang").toString().equals("")){
			return ResponseModel.getModel("参数不齐,请检查","666",null);
		};
		if (pm.get("lang").toString().equals("zh_CN")){
			ParameterMap map = new ParameterMap();
			map.put("id",pm.get("raiseId"));
			map.put("title",pm.get("title"));
			map.put("depict",pm.get("depict"));
			this.updateByPrimaryKeySelective(map);
		}
		pm.put("isDeleted","N");
		ParameterMap chaMap = new ParameterMap();
		chaMap.put("raiseId",pm.get("raiseId"));
		System.out.println(pm.get("raiseId"));
		chaMap.put("lang",pm.get("lang"));
		chaMap.put("isDeleted","N");
		System.out.println(pm.get("lang"));
		if (coinLangService.getByCondition(chaMap) == null){
			pm.put("createTime",new Date());
			pm.put("createBy",((User) session.getAttribute(Const.SESSION_USER)).getUserId());
			coinLangService.insertSelective(pm);
		}else {
			pm.put("modifyTime",new Date());
			pm.put("modifyBy",((User) session.getAttribute(Const.SESSION_USER)).getUserId());
			coinLangService.updateByPrimaryKeySelective(pm);
		};
		return ResponseModel.getModel(ResultEnum.SUCCESS, Boolean.TRUE);
	}

	public List<ParameterMap> getAllVersionLang(ParameterMap pm) throws Exception{
		return this.coinPublishMapper.getAllVersionLang(pm);
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
}
