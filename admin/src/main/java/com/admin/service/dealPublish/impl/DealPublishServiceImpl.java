
package com.admin.service.dealPublish.impl;

import com.admin.config.CoinType;
import com.admin.config.MoneyType;
import com.admin.dao.dealPublish.DealPublishDao;
import com.admin.service.accountPlan.AccountPlanService;
import com.admin.service.dealDetail.DealDetailService;
import com.admin.service.userAccount.UserAccountService;
import com.admin.util.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.druid.util.StringUtils;
import com.admin.entity.ResponseModel;
import com.admin.entity.ResultEnum;
import com.admin.entity.system.Const;
import com.admin.entity.system.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

import com.admin.service.dealPublish.DealPublishService;

/**
 * devservice
 *
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional
public class DealPublishServiceImpl implements DealPublishService {

    /**
     * this loger  can be used by service in anywhere . wzz
     */
    @Autowired
    private DealPublishDao dealPublishMapper;

    @Autowired
    private DealDetailService dealDetailService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private AccountPlanService accountPlanService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${home.url}")
    public String home_url;

    @Transactional
    public int deleteByPrimaryKey(Long dealPublishId) {
        return dealPublishMapper.deleteByPrimaryKey(dealPublishId);
    }

    @Transactional
    public int insert(ParameterMap pm) {
        return dealPublishMapper.insert(pm);
    }

    @Transactional
    public int insertSelective(ParameterMap pm) {
        return dealPublishMapper.insertSelective(pm);
    }

    public ParameterMap selectByPrimaryKey(Long dealPublishId) {
        return dealPublishMapper.selectByPrimaryKey(dealPublishId);
    }

    @Transactional
    public int updateByPrimaryKeySelective(ParameterMap pm) {
        return dealPublishMapper.updateByPrimaryKeySelective(pm);
    }

    @Transactional
    public int updateByPrimaryKey(ParameterMap pm) {
        return dealPublishMapper.updateByPrimaryKey(pm);
    }

    @Transactional
    public boolean saveOrUpdate(ParameterMap pm, String operateType) throws Exception {
        boolean flag = false;

        if ("add".equals(operateType)) {
            flag = this.dealPublishMapper.insert(pm) > 0 ? true : false;
        }

        if ("edit".equals(operateType)) {
            flag = this.dealPublishMapper.updateByPrimaryKey(pm) > 0 ? true : false;
        }

        return flag;
    }

    @Transactional
    public boolean deleteDealPublish(String ids) throws Exception {
        boolean flag = false;
        if (ids != null && !"".equals(ids.trim())) {
            String[] id = ids.split(",");
            flag = this.dealPublishMapper.deleteByIds(id) > 0 ? true : false;
        }
        return flag;
    }

    public ParameterMap getByCondition(ParameterMap pm) throws Exception {
        return this.dealPublishMapper.getByCondition(pm);
    }


    public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
        argmap.put("homeUrl", home_url);
        return this.dealPublishMapper.getByConditionList(argmap);
    }

    public long getByConditionCount(ParameterMap pm) throws Exception {
        return this.dealPublishMapper.getByConditionCount(pm);
    }

    @Transactional
    public Object add(ParameterMap pm, HttpSession session) throws Exception {
        try {
            if (StringUtils.isEmpty(pm.getString("id"))) {
                pm.remove("id");
            }
            pm.put("createTime", LocalDateTime.now());
            pm.put("createBy", ((User) session.getAttribute(Const.SESSION_USER)).getUserId());
            this.insertSelective(pm);
            return ResponseModel.getModel(ResultEnum.SUCCESS, null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResponseModel.getModel(ResultEnum.ERROR, null);
        }
    }

    @Override
    public Object getDetail(ParameterMap pm) throws Exception {
        try {
            ParameterMap detail = null;
            detail = this.selectByPrimaryKey(Long.valueOf(pm.getString("id")));
            return ResponseModel.getModel(ResultEnum.SUCCESS, detail);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResponseModel.getModel(ResultEnum.ERROR, null);
        }
    }

    @Transactional
    public Object edit(ParameterMap pm, HttpSession session) throws Exception {
        try {
            User user = (User) session.getAttribute(Const.SESSION_USER);
            pm.put("modifyTime", LocalDateTime.now());
            pm.put("modifyBy", user.getUserId());
            this.updateByPrimaryKeySelective(pm);
            return ResponseModel.getModel(ResultEnum.SUCCESS, null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResponseModel.getModel(ResultEnum.ERROR, null);
        }
    }

    @Transactional
    public Object cancelPublish(ParameterMap pm, HttpSession session) throws Exception {
        try {
            //下架相当于取消广告
            ParameterMap temp = new ParameterMap();
            temp.put("id", pm.get("id").toString());
            ParameterMap publishPm = this.getByCondition(temp);

            if (!publishPm.get("state").toString().equals("0")) {
                return ResponseModel.getModel("广告已下架请勿重复操作！", "666", null);
            }

            temp.clear();
            temp.put("publishId", pm.get("id"));
            temp.put("state", 0);
            List<ParameterMap> dealDetail = dealDetailService.getByConditionList(temp);
            temp.clear();
            temp.put("publishId", pm.get("id"));
            temp.put("state", 1);
            List<ParameterMap> dealDetail2 = dealDetailService.getByConditionList(temp);
            if (!dealDetail.isEmpty() || !dealDetail2.isEmpty()) {
                return ResponseModel.getModel("有进行中的订单，不能进行下架操作", "888", null);
            }

            User user = (User) session.getAttribute(Const.SESSION_USER);

            pm.put("backoutUserId", user.getUserId());
            pm.put("cancelTime", LocalDateTime.now());
            pm.put("isPublish", 2);
            pm.put("state", 1);
            pm.put("modifyTime", LocalDateTime.now());
            pm.put("modifyBy", user.getUserId());
            pm.put("intro", "后台强制下架");

            if (publishPm.get("type").toString().equals("0")) {
                //出售广告,操作加入计划表,解冻卖家资金
                ParameterMap dtoPm = new ParameterMap();
                //减少卖家账户锁定子链
                dtoPm.put("objectId", pm.get("id").toString());
                dtoPm.put("userId", publishPm.get("user_id"));
                dtoPm.put("operateState", Const.OPERATE_STATE_OUT);
                BigDecimal money = new BigDecimal(publishPm.get("number").toString()).subtract(new BigDecimal(publishPm.get("sell_number").toString()));
                dtoPm.put("operateAmount", money);
                dtoPm.put("operateCode", CoinType.LOCK.getKey());
                dtoPm.put("operateSource", MoneyType.PLAN_SELL_PUBLISH.getValue());
                dtoPm.put("intoAccount", Const.OPERATE_STATE__IN);
                dtoPm.put("isAdmin", 0);
                dtoPm.put("create_by", user.getUserId());
                dtoPm.put("description", "后台强制取消广告");
                dtoPm.put("create_time", new Date());
                accountPlanService.insertSelective(dtoPm);
                //卖家解冻锁定子链,加入计划
                dtoPm.clear();
                dtoPm.put("objectId", pm.get("id").toString());
                dtoPm.put("userId", publishPm.get("user_id"));
                dtoPm.put("operateState", Const.OPERATE_STATE__IN);
                dtoPm.put("operateAmount", money);
                dtoPm.put("operateCode", CoinType.FREE.getKey());
                dtoPm.put("operateSource", MoneyType.PLAN_SELL_PUBLISH.getValue());
                dtoPm.put("intoAccount", Const.INTO_ACCOUNT_N);
                dtoPm.put("isAdmin", 0);
                dtoPm.put("description", "后台强制取消广告");
                dtoPm.put("create_by", user.getUserId());
                dtoPm.put("create_time", new Date());
                accountPlanService.insertSelective(dtoPm);
            }
            //更新广告
            this.updateByPrimaryKeySelective(pm);
            return ResponseModel.getModel(ResultEnum.SUCCESS, null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResponseModel.getModel(ResultEnum.ERROR, null);
        }
    }

    @Transactional
    public Object cancelPublishBySearch(ParameterMap pm, HttpSession session) throws Exception {
        pm.put("isDeleted", "N");
        List<ParameterMap> list = this.getByConditionList(pm);
        ParameterMap map = new ParameterMap();
        int i = 0;
        int j = 0;
        int f = 0;
        for (ParameterMap p : list) {
            map.clear();
            map.put("id", p.get("id"));
            HashMap<String, Object> result = (HashMap<String, Object>) this.cancelPublish(map, session);
            if (result.get("status").toString().equals("200")) {
                i = i + 1;
            } else if (result.get("status").toString().equals("666")) {
                j = j + 1;
            } else if (result.get("status").toString().equals("888")) {
                f = f + 1;
            }
        }
        int k = i + j + f;
        String message = "共处理" + k + "条广告，其中处理成功" + i + "条，" + f + "条数据有订单未完成，暂时不能下架，" + j + "条数据已下架无需取消！";
        return ResponseModel.getModel(message, "200", null);
    }

    @Transactional
    public Object del(ParameterMap pm) throws Exception {
        boolean isdel = false;
        try {
            String ids = pm.getString("id");
            isdel = this.deleteDealPublish(ids);
            return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResponseModel.getModel(ResultEnum.ERROR, isdel);
        }
    }

}
