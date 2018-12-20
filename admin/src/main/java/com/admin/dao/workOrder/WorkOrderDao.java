
package com.admin.dao.workOrder;

import java.util.List;

import com.admin.entity.dto.WorkOrderDTO;
import org.springframework.stereotype.Repository;
import com.admin.util.ParameterMap;


/**
 * devMaper
 * @author rstyro
 * @version v1.0
 */

@Repository
public interface WorkOrderDao {
	
    int deleteByPrimaryKey(Long workOrderId);

    int insert(ParameterMap pm);

    int insertSelective(ParameterMap pm);

    ParameterMap selectByPrimaryKey(Long workOrderId);

    int updateByPrimaryKeySelective(ParameterMap pm);

    int updateByPrimaryKey(ParameterMap pm);
    
    int deleteByIds(String[] ids);
    
    ParameterMap getByCondition(ParameterMap pm);
    
    List<ParameterMap> getByConditionList(ParameterMap pm);

    List<WorkOrderDTO> selectWorkOrderList(ParameterMap pm);
    
    long getByConditionCount(ParameterMap pm);

    ParameterMap getDetailById(ParameterMap pm);
}