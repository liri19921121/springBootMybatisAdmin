
package com.admin.dao.usrArea;

import java.util.List;

import com.admin.util.ParameterMap;
import org.springframework.stereotype.Repository;


/**
 * devMaper
 * @author rstyro
 * @version v1.0
 */

@Repository
public interface UsrAreaDao {
	
    int deleteByPrimaryKey(Long usrAreaId);

    int insert(ParameterMap pm);

    int insertSelective(ParameterMap pm);

    ParameterMap selectByPrimaryKey(Long usrAreaId);

    int updateByPrimaryKeySelective(ParameterMap pm);

    int updateByPrimaryKey(ParameterMap pm);
    
    int deleteByIds(String[] ids);
    
    ParameterMap getByCondition(ParameterMap pm);
    
    List<ParameterMap> getByConditionList(ParameterMap pm);

    List<ParameterMap> getPageList(ParameterMap pm);
    
    long getByConditionCount(ParameterMap pm);
}