
package com.admin.service.dealAppeal.impl;

import com.admin.config.CoinType;
import com.admin.service.dealAppeal.DealAppealService;
import com.alibaba.fastjson.JSONObject;
import com.admin.config.MoneyType;
import com.admin.service.accountPlan.AccountPlanService;
import com.admin.service.dealDetail.DealDetailService;
import com.admin.service.dealPublish.DealPublishService;
import com.admin.service.userAccount.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
import java.util.List;

import com.admin.util.ParameterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

import com.admin.dao.dealAppeal.DealAppealDao;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestTemplate;

/**
 * devservice
 *
 * @author rstyro
 * @version v1.0
 */

@Service
@Transactional
public class DealAppealServiceImpl implements DealAppealService {
    @Value("${home.url}")
    public String home_url;

    /**
     * this loger  can be used by service in anywhere . wzz
     */
    @Autowired
    private DealAppealDao dealAppealMapper;

    @Autowired
    private DealDetailService dealDetailService;

    @Autowired
    private AccountPlanService accountPlanService;

    @Autowired
    private DealPublishService dealPublishService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mq.addPlanUrl}")
    private String url;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Transactional
    public int deleteByPrimaryKey(Long dealAppealId) {
        return dealAppealMapper.deleteByPrimaryKey(dealAppealId);
    }

    @Transactional
    public int insert(ParameterMap pm) {
        return dealAppealMapper.insert(pm);
    }

    @Transactional
    public int insertSelective(ParameterMap pm) {
        return dealAppealMapper.insertSelective(pm);
    }

    public ParameterMap selectByPrimaryKey(ParameterMap pm) {
        return dealAppealMapper.selectByPrimaryKey(pm);
    }

    @Transactional
    public int updateByPrimaryKeySelective(ParameterMap pm) {
        return dealAppealMapper.updateByPrimaryKeySelective(pm);
    }

    @Transactional
    public int updateByPrimaryKey(ParameterMap pm) {
        return dealAppealMapper.updateByPrimaryKey(pm);
    }

    @Transactional
    public boolean saveOrUpdate(ParameterMap pm, String operateType) throws Exception {
        boolean flag = false;

        if ("add".equals(operateType)) {
            flag = this.dealAppealMapper.insert(pm) > 0 ? true : false;
        }

        if ("edit".equals(operateType)) {
            flag = this.dealAppealMapper.updateByPrimaryKey(pm) > 0 ? true : false;
        }

        return flag;
    }

    @Transactional
    public boolean deleteDealAppeal(String ids) throws Exception {
        boolean flag = false;
        if (ids != null && !"".equals(ids.trim())) {
            String[] id = ids.split(",");
            flag = this.dealAppealMapper.deleteByIds(id) > 0 ? true : false;
        }
        return flag;
    }

    public ParameterMap getByCondition(ParameterMap pm) throws Exception {
        return this.dealAppealMapper.getByCondition(pm);
    }


    public List<ParameterMap> getByConditionList(ParameterMap argmap) throws Exception {
        return this.dealAppealMapper.getByConditionList(argmap);
    }

    public long getByConditionCount(ParameterMap pm) throws Exception {
        return this.dealAppealMapper.getByConditionCount(pm);
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
            pm.put("homeUrl", home_url);
            detail = this.selectByPrimaryKey(pm);
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
    public Object disPose(ParameterMap pm, HttpSession session) throws Exception {
        try {
            ParameterMap dtoPm = new ParameterMap();
            dtoPm.put("id", pm.get("id"));
            ParameterMap appeal = this.getByCondition(dtoPm);
            if (appeal.get("dispose_time") != null && appeal.get("dispose_time") != "") {
                return ResponseModel.getModel("订单已处理，请勿重复处理", "888", null);
            }
            ParameterMap detalPm = new ParameterMap();
            String dealId = appeal.get("deal_id").toString();
            detalPm.put("id", dealId);
            ParameterMap dealDetail = dealDetailService.getByCondition(detalPm);
            if (dealDetail == null) {
                return ResponseModel.getModel("订单异常", "888", null);
            }

            if (!dealDetail.get("state").toString().equals("1")) {
                return ResponseModel.getModel("订单状态异常，不能处理", "888", null);
            }
            String iswin = pm.get("userIsWin").toString();
            if ("1".equals(iswin)) {
                dtoPm.put("isWin", 1);
            } else if ("0".equals(iswin)) {
                dtoPm.put("isWin", 0);
            } else {
                return ResponseModel.getModel(ResultEnum.ERROR, null);
            }
            User user = (User) session.getAttribute(Const.SESSION_USER);
            dtoPm.put("disposeTime", LocalDateTime.now());
            dtoPm.put("disposeId", user.getUserId());
            dtoPm.put("modifyTime", LocalDateTime.now());
            dtoPm.put("modifyBy", user.getUserId());
            this.updateByPrimaryKeySelective(dtoPm);

            //卖家申诉，申诉成功订单取消，子链解冻回归
            if ("1".equals(iswin)) {
                //更改订单状态
                dtoPm.clear();
                dealDetail.put("state", 3);
                dealDetail.put("isAppeal", 2);
                dealDetail.put("collectionTime", new Date());
                dealDetail.put("modifyTime", new Date());
                dealDetail.put("modifyBy", user.getUserId());
                dealDetail.put("remarks", "卖家申诉成功");
                dealDetailService.updateByPrimaryKeySelective(dealDetail);

                //解冻广告相应冻结子链
                dtoPm.clear();
                dtoPm.put("id", dealDetail.get("publish_id"));
                ParameterMap dealPublish = dealPublishService.getByCondition(dtoPm);
                dealPublish.put("freezeNumber", new BigDecimal(dealPublish.get("freeze_number").toString()).subtract(new BigDecimal(dealDetail.get("amount").toString())));
                dealPublish.put("modifyBy", user.getUserId());
                dealPublish.put("modifyTime", new Date());
                dealPublishService.updateByPrimaryKeySelective(dealPublish);

                if (dealDetail.get("type").toString().equals("0")) {
                    return ResponseModel.getModel(ResultEnum.SUCCESS, null);
                } else {
                    //卖家不是发布者,解冻相应账号子链
                    ParameterMap planFree = new ParameterMap();
                    planFree.put("objectId", dealDetail.getString("id"));
                    planFree.put("userId", dealDetail.get("seller_user_id"));
                    planFree.put("tranUserId", -1);
                    planFree.put("operateState", 1);
                    planFree.put("operateAmount", dealDetail.get("amount"));
                    planFree.put("operateCode", CoinType.FREE.getKey());
                    planFree.put("operateSource", MoneyType.PLAN_SELL_DETAIL.getValue());
                    planFree.put("description", "广告申诉卖家申诉成功");
                    planFree.put("intoAccount", "0");
                    planFree.put("isAdmin", "0");
                    User adminUser = (User) session.getAttribute(Const.SESSION_USER);
                    planFree.put("createBy", adminUser.getUserId());
                    planFree.put("createTime", new Date());
                    accountPlanService.insertSelective(planFree);

                    ParameterMap planLock = new ParameterMap();
                    planLock.put("objectId", dealDetail.getString("id"));
                    planLock.put("userId", dealDetail.get("seller_user_id"));
                    planLock.put("tranUserId", -1);
                    planLock.put("operateState", 0);
                    planLock.put("operateAmount", dealDetail.get("amount"));
                    planLock.put("operateCode", CoinType.LOCK.getKey());
                    planLock.put("operateSource", MoneyType.PLAN_SELL_DETAIL.getValue());
                    planLock.put("description", "广告申诉卖家申诉成功");
                    planLock.put("intoAccount", "0");
                    planLock.put("isAdmin", "0");
                    planLock.put("createBy", adminUser.getUserId());
                    planLock.put("createTime", new Date());
                    accountPlanService.insertSelective(planLock);

                    ResponseEntity<String> responseEntityFree = restTemplate.getForEntity(url + "?id=" + planFree.getString("id"), String.class);
                    System.out.println("responseEntityFree=" + responseEntityFree);
                    JSONObject objFree = JSONObject.parseObject(responseEntityFree.getBody());

                    ResponseEntity<String> responseEntityLock = restTemplate.getForEntity(url + "?id=" + planLock.getString("id"), String.class);
                    System.out.println("responseEntityLock=" + responseEntityLock);
                    JSONObject objLock = JSONObject.parseObject(responseEntityLock.getBody());

                    if (objFree.getString("status").equals("200") && objLock.getString("status").equals("200")) {
                        return ResponseModel.getModel(ResultEnum.SUCCESS, null);
                    } else {
                        //强制回滚
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ResponseModel.getModel(ResultEnum.ERROR, null);
                    }
                }

            } else {
                //卖家申诉失败，直接完成订单,加入plan计划

                //更改订单状态
                dtoPm.clear();
                dealDetail.put("state", 2);
                dealDetail.put("isAppeal", 2);
                dealDetail.put("collectionTime", new Date());
                dealDetail.put("modifyTime", new Date());
                dealDetail.put("modifyBy", user.getUserId());
                dealDetail.put("remarks", "卖家申诉失败");
                dealDetailService.updateByPrimaryKeySelective(dealDetail);

                //更新广告
                dtoPm.clear();
                dtoPm.put("id", dealDetail.get("publish_id"));
                ParameterMap dealPublish = dealPublishService.getByCondition(dtoPm);
                dealPublish.put("freezeNumber", new BigDecimal(dealPublish.get("freeze_number").toString()).subtract(new BigDecimal(dealDetail.get("amount").toString())));
                dealPublish.put("sellNumber", new BigDecimal(dealPublish.get("sell_number").toString()).add(new BigDecimal(dealDetail.get("amount").toString())));
                dealPublish.put("accomplishNum", new BigDecimal(dealPublish.get("accomplish_num").toString()).add(new BigDecimal("1")));
                dealPublish.put("modifyTime", new Date());
                dealPublish.put("modifyBy", user.getUserId());
                dealPublishService.updateByPrimaryKeySelective(dealPublish);

                //买家获得子链,加入计划
                ParameterMap planFree = new ParameterMap();
                planFree.put("objectId", dealId);
                planFree.put("userId", dealDetail.get("buyer_user_id"));
                planFree.put("tranUserId", dealDetail.get("seller_user_id"));
                planFree.put("operateState", 1);
                planFree.put("operateAmount", dealDetail.get("amount"));
                planFree.put("operateCode", CoinType.FREE.getKey());
                planFree.put("operateSource", MoneyType.PLAN_SELL_DETAIL.getValue());
                planFree.put("description", "广告申诉卖家申诉失败订单完成");
                planFree.put("intoAccount", "0");
                planFree.put("isAdmin", "0");
                planFree.put("createBy", user.getUserId());
                planFree.put("createTime", new Date());
                accountPlanService.insertSelective(planFree);

                //减少卖家账户锁定子链
                ParameterMap planLock = new ParameterMap();
                planLock.put("objectId", dealId);
                planLock.put("userId", dealDetail.get("seller_user_id"));
                planLock.put("tranUserId", dealDetail.get("buyer_user_id"));
                planLock.put("operateState", 0);
                planLock.put("operateAmount", dealDetail.get("amount"));
                planLock.put("operateCode", CoinType.LOCK.getKey());
                planLock.put("operateSource", MoneyType.PLAN_SELL_DETAIL.getValue());
                planLock.put("description", "广告申诉卖家申诉失败订单完成");
                planLock.put("intoAccount", "0");
                planLock.put("isAdmin", "0");
                planLock.put("createBy", user.getUserId());
                planLock.put("createTime", new Date());
                accountPlanService.insertSelective(planLock);

                ResponseEntity<String> responseEntityFree = restTemplate.getForEntity(url + "?id=" + planFree.getString("id"), String.class);
                System.out.println("responseEntityFree=" + responseEntityFree);
                JSONObject objFree = JSONObject.parseObject(responseEntityFree.getBody());

                ResponseEntity<String> responseEntityLock = restTemplate.getForEntity(url + "?id=" + planLock.getString("id"), String.class);
                System.out.println("responseEntityLock=" + responseEntityLock);
                JSONObject objLock = JSONObject.parseObject(responseEntityLock.getBody());

                if (objFree.getString("status").equals("200") && objLock.getString("status").equals("200")) {
                    return ResponseModel.getModel(ResultEnum.SUCCESS, null);
                } else {
                    //强制回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResponseModel.getModel(ResultEnum.ERROR, null);
                }

            }

        } catch (Exception e) {
            //强制回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResponseModel.getModel(ResultEnum.ERROR, null);
        }
    }

    @Transactional
    public Object del(ParameterMap pm) throws Exception {
        boolean isdel = false;
        try {
            String ids = pm.getString("id");
            isdel = this.deleteDealAppeal(ids);
            return ResponseModel.getModel(ResultEnum.SUCCESS, isdel);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return ResponseModel.getModel(ResultEnum.ERROR, isdel);
        }
    }

}
