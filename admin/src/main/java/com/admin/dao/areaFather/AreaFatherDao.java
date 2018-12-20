
package com.admin.dao.areaFather;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.admin.util.ParameterMap;


/**
 * devMaper
 * @author rstyro
 * @version v1.0
 */

@Repository
public interface AreaFatherDao {
	
    int deleteByPrimaryKey(Long areaFatherId);

    int insert(ParameterMap pm);

    int insertSelective(ParameterMap pm);

    int insertFathers(List<ParameterMap> list);

    ParameterMap selectByPrimaryKey(Long areaFatherId);

    int updateByPrimaryKeySelective(ParameterMap pm);

    int updateByPrimaryKey(ParameterMap pm);
    
    int deleteByIds(String[] ids);

    int updateByAreaid(ParameterMap pm);
    
    ParameterMap getByCondition(ParameterMap pm);
    
    List<ParameterMap> getByConditionList(ParameterMap pm);

    List<ParameterMap> getList(ParameterMap pm);

    List<ParameterMap> getAllArea(ParameterMap pm);
    
    long getByConditionCount(ParameterMap pm);
}