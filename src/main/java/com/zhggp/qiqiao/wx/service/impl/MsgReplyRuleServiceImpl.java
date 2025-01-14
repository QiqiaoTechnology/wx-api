package com.zhggp.qiqiao.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhggp.qiqiao.wx.common.utils.PageUtils;
import com.zhggp.qiqiao.wx.common.utils.Query;
import com.zhggp.qiqiao.wx.db.dao.MsgReplyRuleMapper;
import com.zhggp.qiqiao.wx.db.entity.MsgReplyRule;
import com.zhggp.qiqiao.wx.service.MsgReplyRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MsgReplyRuleServiceImpl extends ServiceImpl<MsgReplyRuleMapper, MsgReplyRule> implements MsgReplyRuleService {
    @Autowired
    MsgReplyRuleMapper msgReplyRuleMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String matchValue = (String) params.get("matchValue");
        String appid = (String) params.get("appid");
        int pageSize = 10;
        if (null != params.get("pageSize")) {
            pageSize = Integer.parseInt((String) params.get("pageSize"));

        }

        int currPage = 1;
        if (null != params.get("currPage")) {
            currPage = Integer.parseInt((String) params.get("currPage"));
        }
        IPage<MsgReplyRule> page = this.page(
                new Query<MsgReplyRule>().getPage(params).setCurrent(currPage).setSize(pageSize),
                new QueryWrapper<MsgReplyRule>()
                        .eq(StringUtils.hasText(appid), "appid", appid)
                        .or()
                        .apply("appid is null or appid = ''")
                        .like(StringUtils.hasText(matchValue), "match_value", matchValue)
                        .orderByDesc("update_time")
        );

        return new PageUtils(page);
    }

    /**
     * 保存自动回复规则
     *
     * @param msgReplyRule
     */

    @Override
    public boolean save(MsgReplyRule msgReplyRule) {
        System.out.println(JSON.toJSONString(msgReplyRule));
        if (null == msgReplyRule.getRuleId()) {
            msgReplyRuleMapper.insert(msgReplyRule);
        } else {
            msgReplyRuleMapper.updateById(msgReplyRule);
        }
        return false;
    }

    /**
     * 获取所有的回复规则
     *
     * @return
     */
    @Override
    public List<MsgReplyRule> getRules() {
        return msgReplyRuleMapper.selectList(new QueryWrapper<MsgReplyRule>().orderByDesc("rule_id"));
    }

    /**
     * 获取默认的回复规则
     *
     * @return
     */
    @Override
    public List<MsgReplyRule> getDefaultRules() {
        return msgReplyRuleMapper.selectList(
                new QueryWrapper<MsgReplyRule>()
                        .eq("status", 1)
                        .eq("match_value", "default")
//                        .isNotNull("match_value")
//                        .ne("match_value", "")
//                        .orderByDesc("priority")
        );
    }

    /**
     * 获取当前时段内所有有效的回复规则
     *
     * @return
     */
    @Override
    public List<MsgReplyRule> getValidRules() {
        return msgReplyRuleMapper.selectList(
                new QueryWrapper<MsgReplyRule>()
                        .eq("status", 1)
                        .isNotNull("match_value")
                        .ne("match_value", "")
                        .orderByDesc("priority"));
    }

    /**
     * 筛选符合条件的回复规则
     *
     * @param appid      公众号appid
     * @param exactMatch 是否精确匹配
     * @param keywords   关键词
     * @return 规则列表
     */
    @Override
    public List<MsgReplyRule> getMatchedRules(String appid, boolean exactMatch, String keywords) {
        log.info("MsgReplyRuleServiceImpl.getMatchedRules appid={},exactMatch={},keywords={}",
                appid, exactMatch, keywords);

        LocalTime now = LocalTime.now();
        List<MsgReplyRule> list = this.getValidRules().stream()
                .filter(rule -> !StringUtils.hasText(rule.getAppid()) || appid.equals(rule.getAppid())) // 检测是否是对应公众号的规则，如果appid为空则为通用规则
//                .filter(rule -> null == rule.getEffectTimeStart() || rule.getEffectTimeStart().toLocalTime().isBefore(now))// 检测是否在有效时段，effectTimeStart为null则一直有效
//                .filter(rule -> null == rule.getEffectTimeEnd() || rule.getEffectTimeEnd().toLocalTime().isAfter(now)) // 检测是否在有效时段，effectTimeEnd为null则一直有效
                .filter(rule -> isMatch(exactMatch || rule.isExactMatch(), rule.getMatchValue().split(","), keywords)) //检测是否符合匹配规则
                .collect(Collectors.toList());
        log.info("MsgReplyRuleServiceImpl.getMatchedRules list={}", JSON.toJSONString(list));

        if (list.isEmpty()) {
            log.info("MsgReplyRuleServiceImpl.getMatchedRules list是空的,加默认回复");

            list = this.getDefaultRules().stream()
//                    .filter(rule -> !StringUtils.hasText(rule.getAppid()) || appid.equals(rule.getAppid())) // 检测是否是对应公众号的规则，如果appid为空则为通用规则
//                    .filter(rule -> null == rule.getEffectTimeStart() || rule.getEffectTimeStart().toLocalTime().isBefore(now))// 检测是否在有效时段，effectTimeStart为null则一直有效
//                    .filter(rule -> null == rule.getEffectTimeEnd() || rule.getEffectTimeEnd().toLocalTime().isAfter(now)) // 检测是否在有效时段，effectTimeEnd为null则一直有效
                    .collect(Collectors.toList());
        }
        log.info("MsgReplyRuleServiceImpl.getMatchedRules list={}", JSON.toJSONString(list));


        return list;
    }

    /**
     * 检测文字是否匹配规则
     * 精确匹配时，需关键词与规则词语一致
     * 非精确匹配时，检测文字需包含任意一个规则词语
     *
     * @param exactMatch 是否精确匹配
     * @param ruleWords  规则列表
     * @param checkWords 需检测的文字
     * @return
     */
    public static boolean isMatch(boolean exactMatch, String[] ruleWords, String checkWords) {
        if (!StringUtils.hasText(checkWords)) {
            return false;
        }
        for (String words : ruleWords) {
            if (exactMatch && words.equals(checkWords)) {
                return true;//精确匹配，需关键词与规则词语一致
            }
            if (!exactMatch && checkWords.contains(words)) {
                return true;//模糊匹配
            }
        }
        return false;
    }
}
