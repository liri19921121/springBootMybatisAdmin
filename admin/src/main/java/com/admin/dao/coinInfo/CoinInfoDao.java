
package com.admin.dao.coinInfo;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.admin.util.ParameterMap;


/**
 * devMaper
 * @author rstyro
 * @version v1.0
 */

@Repository
public interface CoinInfoDao {
	
    int deleteByPrimaryKey(Long coinInfoId);

    int insert(ParameterMap pm);

    int insertSelective(ParameterMap pm);

    ParameterMap selectByPrimaryKey(Long coinInfoId);

    int updateByPrimaryKeySelective(ParameterMap pm);

    int updateByPrimaryKey(ParameterMap pm);
    
    int deleteByIds(String[] ids);
    
    ParameterMap getByCondition(ParameterMap pm);
    
    List<ParameterMap> getByConditionList(ParameterMap pm);

    List<ParameterMap> selectList(ParameterMap pm);
    
    long getByConditionCount(ParameterMap pm);

    List<ParameterMap> getAllCoinName(ParameterMap pm);

    List<ParameterMap> getNameList(ParameterMap pm);

}