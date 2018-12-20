
package com.admin.dao.goodsDirectory;

import java.util.List;

import com.admin.util.ParameterMap;
import org.springframework.stereotype.Repository;


/**
 * devMaper
 * @author rstyro
 * @version v1.0
 */

@Repository
public interface GoodsDirectoryDao {
	
    int deleteByPrimaryKey(Long goodsDirectoryId);

    int insert(ParameterMap pm);

    int insertSelective(ParameterMap pm);

    ParameterMap selectByPrimaryKey(Long goodsDirectoryId);

    int updateByPrimaryKeySelective(ParameterMap pm);

    int updateByPrimaryKey(ParameterMap pm);
    
    int deleteByIds(String[] ids);
    
    ParameterMap getByCondition(ParameterMap pm);
    
    List<ParameterMap> getByConditionList(ParameterMap pm);
    
    long getByConditionCount(ParameterMap pm);
}