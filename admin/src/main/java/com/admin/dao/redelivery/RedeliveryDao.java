
package com.admin.dao.redelivery;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.admin.util.ParameterMap;


/**
 * devMaper
 * @author rstyro
 * @version v1.0
 */

@Repository
public interface RedeliveryDao {
	
    int deleteByPrimaryKey(Long redeliveryId);

    int insert(ParameterMap pm);

    int insertSelective(ParameterMap pm);

    ParameterMap selectByPrimaryKey(Long redeliveryId);

    int updateByPrimaryKeySelective(ParameterMap pm);

    int updateByPrimaryKey(ParameterMap pm);
    
    int deleteByIds(String[] ids);
    
    ParameterMap getByCondition(ParameterMap pm);
    
    List<ParameterMap> getByConditionList(ParameterMap pm);
    
    long getByConditionCount(ParameterMap pm);
}