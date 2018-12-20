
package com.admin.dao.banner;

import java.util.List;

import com.admin.util.ParameterMap;
import org.springframework.stereotype.Repository;


/**
 * devMaper
 * @author rstyro
 * @version v1.0
 */

@Repository
public interface BannerDao {
	
    int deleteByPrimaryKey(Long bannerId);

    int insert(ParameterMap pm);

    int insertSelective(ParameterMap pm);

    int insertPictureSelective(ParameterMap pm);

    ParameterMap selectByPrimaryKey(Long bannerId);

    int updateByPrimaryKeySelective(ParameterMap pm);

    int updateByPrimaryKey(ParameterMap pm);

    ParameterMap selectByPrimary(ParameterMap pm);

    int deleteByIds(String[] ids);
    
    ParameterMap getByCondition(ParameterMap pm);
    
    List<ParameterMap> getByConditionList(ParameterMap pm);
    
    long getByConditionCount(ParameterMap pm);
}