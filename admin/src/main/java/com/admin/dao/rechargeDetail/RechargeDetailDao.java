
package com.admin.dao.rechargeDetail;

import java.util.List;

import com.admin.util.ParameterMap;
import org.springframework.stereotype.Repository;


/**
 * devMaper
 * @author rstyro
 * @version v1.0
 */

@Repository
public interface RechargeDetailDao {
	
    int deleteByPrimaryKey(Long rechargeDetailId);

    int insert(ParameterMap pm);

    int insertSelective(ParameterMap pm);

    ParameterMap selectByPrimaryKey(Long rechargeDetailId);

    int updateByPrimaryKeySelective(ParameterMap pm);

    int updateByPrimaryKey(ParameterMap pm);
    
    int deleteByIds(String[] ids);
    
    ParameterMap getByCondition(ParameterMap pm);
    
    List<ParameterMap> getByConditionList(ParameterMap pm);

    List<ParameterMap> getRechargeList(ParameterMap pm);

    long getByConditionCount(ParameterMap pm);
}