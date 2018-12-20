
package com.admin.service.picture.impl;

import com.admin.dao.picture.PictureMapper;
import com.admin.service.picture.PictureService;
import com.admin.entity.picture.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * devservice
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional(readOnly = true)
public class PictureServiceImpl implements PictureService {
	
	@Autowired
	private PictureMapper pictureMapper;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	

	@Transactional(readOnly = false)
	 public  int deleteByPrimaryKey(Long pictureId){
		 return  pictureMapper.deleteByPrimaryKey(pictureId);
	 }

	@Transactional(readOnly = false)
	 public   int insert(Picture record){
		 return pictureMapper.insert(record);
	 }

	@Transactional(readOnly = false)
	 public  int insertSelective(Picture record){
		 return pictureMapper.insertSelective(record);
	 }

	 public   Picture selectByPrimaryKey(Long pictureId){
		 return pictureMapper.selectByPrimaryKey(pictureId);
	 }

	@Transactional(readOnly = false)
	 public  int updateByPrimaryKeySelective(Picture record){
		 return  pictureMapper.updateByPrimaryKeySelective(record);
	 }

	@Transactional(readOnly = false)
	 public  int updateByPrimaryKey(Picture record){
		 return  pictureMapper.updateByPrimaryKey(record);
	 }

	@Transactional(rollbackFor=Exception.class)
	public boolean saveOrUpdate(Picture record,String operateType) throws Exception{
		boolean flag = false;

		if("add".equals(operateType)){
			flag = this.pictureMapper.insert(record) > 0 ? true : false;
		}

		if("edit".equals(operateType)){
			flag = this.pictureMapper.updateByPrimaryKey(record) > 0 ? true : false;
		}

		return flag;
	}


	public Picture getById(Long id){
		Picture record = null;
		record = this.pictureMapper.selectByPrimaryKey(id);
		return record;
	}

	@Transactional(rollbackFor=Exception.class)
	public boolean deletePicture(String ids) throws Exception {
		boolean flag = false;
		if(ids != null && !"".equals(ids.trim())){
			String[] id = ids.split(",");
			flag = this.pictureMapper.deleteByIds(id)> 0 ? true : false;
		}
		return flag;
	}
	
	public Picture getByCondition(HashMap<String, Object> argmap) throws Exception {
		return this.pictureMapper.getByCondition(argmap);
	}

	
	public List<Picture> getByCondition4List(HashMap<String, Object> argmap) throws Exception {
		return this.pictureMapper.getByCondition4List(argmap);
	}
	
	@Override
	public List<Picture> getByConditionList(HashMap<String, Object> argmap)
			throws Exception {
		return pictureMapper.getByConditionList(argmap);
	}
	
	public long getByCondition4Count(HashMap<String, Object> argmap) throws Exception {
		return this.pictureMapper.getByCondition4Count(argmap);
	}





}
