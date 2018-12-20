
package com.admin.dao.coinLang;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.admin.util.ParameterMap;


/**
 * devMaper
 * @author rstyro
 * @version v1.0
 */

@Repository
public interface CoinLangDao {
	
    int deleteByPrimaryKey(Long coinLangId);

    int insert(ParameterMap pm);

    int insertSelective(ParameterMap pm);

    ParameterMap selectByPrimaryKey(Long coinLangId);

    int updateByPrimaryKeySelective(ParameterMap pm);

    int updateByPrimaryKey(ParameterMap pm);
    
    int deleteByIds(String[] ids);
    
    ParameterMap getByCondition(ParameterMap pm);
    
    List<ParameterMap> getByConditionList(ParameterMap pm);
    
    long getByConditionCount(ParameterMap pm);

    List<ParameterMap> getAllLang(ParameterMap pm);
}