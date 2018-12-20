
package com.admin.dao.userAccount;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.admin.util.ParameterMap;


/**
 * devMaper
 * @author rstyro
 * @version v1.0
 */

@Repository
public interface UserAccountDao {
	
    int deleteByPrimaryKey(Long userAccountId);

    int insert(ParameterMap pm);

    int insertSelective(ParameterMap pm);

    ParameterMap selectByPrimaryKey(Long userAccountId);

    int updateByPrimaryKeySelective(ParameterMap pm);

    int updateByPrimaryKey(ParameterMap pm);
    
    int deleteByIds(String[] ids);
    
    ParameterMap getByCondition(ParameterMap pm);
    
    List<ParameterMap> getByConditionList(ParameterMap pm);
    
    long getByConditionCount(ParameterMap pm);

    List<ParameterMap> getListByCode(ParameterMap pm);
}