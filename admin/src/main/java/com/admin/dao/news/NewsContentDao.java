
package com.admin.dao.news;

import java.util.List;

import com.admin.util.ParameterMap;
import org.springframework.stereotype.Repository;


/**
 * Maper
 * @author rstyro
 * @version v1.0
 */

@Repository
public interface NewsContentDao {
	
    int deleteByPrimaryKey(Long newsContentId);

    int insert(ParameterMap pm);

    int insertSelective(ParameterMap pm);

    ParameterMap selectByPrimaryKey(Long newsContentId);

    int updateByPrimaryKeySelective(ParameterMap pm);

    int updateByPrimaryKey(ParameterMap pm);
    
    int deleteByIds(String[] ids);
    
    ParameterMap getByCondition(ParameterMap pm);
    
    List<ParameterMap> getByConditionList(ParameterMap pm);
    
    long getByConditionCount(ParameterMap pm);
}