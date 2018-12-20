
package com.admin.dao.picture;

import com.admin.entity.picture.Picture;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * devMaper
 * @author rstyro
 * @version v1.0
 */

@Repository
public interface PictureMapper {
	
    int deleteByPrimaryKey(Long pictureId);

    int insert(Picture record);

    int insertSelective(Picture record);

    Picture selectByPrimaryKey(Long pictureId);

    int updateByPrimaryKeySelective(Picture record);

    int updateByPrimaryKey(Picture record);
    
    int deleteByIds(String[] ids);
    
    Picture getByCondition(HashMap<String, Object> argmap);

    List<Picture> getByCondition4List(HashMap<String, Object> argmap);

    List<Picture> getByConditionList(HashMap<String, Object> argmap);

    long getByCondition4Count(HashMap<String, Object> argmap);
}