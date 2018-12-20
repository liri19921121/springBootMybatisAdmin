
package com.admin.dao.relationUmbrella;

import java.util.List;

import com.admin.util.ParameterMap;
import org.springframework.stereotype.Repository;


/**
 * devMaper
 *
 * @author rstyro
 * @version v1.0
 */

@Repository
public interface RelationUmbrellaDao {

    int deleteByPrimaryKey(Long relationUmbrellaId);

    int insert(ParameterMap pm);

    int insertSelective(ParameterMap pm);

    ParameterMap selectByPrimaryKey(Long relationUmbrellaId);

    int updateByPrimaryKeySelective(ParameterMap pm);

    int updateByIds(ParameterMap pm);

    int updateByPrimaryKey(ParameterMap pm);

    int deleteByIds(String[] ids);

    ParameterMap getByCondition(ParameterMap pm);

    List<ParameterMap> getByConditionList(ParameterMap pm);

    List<String> getUserIdByRecommendId(String userId);

    List<Long> getIds(ParameterMap pm);

    List<Long> getIdsByAreaId(ParameterMap pm);

    long getByConditionCount(ParameterMap pm);
}