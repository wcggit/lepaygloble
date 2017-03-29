package com.jifenke.lepluslive.weixin.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantWeiXinUserService;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWelfareLog;
import com.jifenke.lepluslive.weixin.domain.entities.DictionaryService;
import com.jifenke.lepluslive.weixin.domain.entities.WxTemMsg;
import com.jifenke.lepluslive.weixin.repository.WxTemMsgRepository;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


/**
 * 发送模板消息 Created by zhangwen on 2016/5/10.
 */
@Service
@Transactional(readOnly = true)
public class WxTemMsgService {

    @Inject
    private WxTemMsgRepository wxTemMsgRepository;

    @Inject
    private MerchantWeiXinUserService merchantWeiXinUserService;

    @Inject
    private MerchantService merchantService;

    @Inject
    private DictionaryService dictionaryService;

    private static final Logger log = LoggerFactory.getLogger(WxTemMsgService.class);


    public void sendToClient(PartnerWelfareLog partnerWelfareLog, String openId) {
        new Thread(() -> {
            //为用户推送
            StringBuffer sb = new StringBuffer();
            String[] keys = new String[3];
            if (partnerWelfareLog.getScoreA() != 0) {
                if (partnerWelfareLog.getScoreB() != 0) {
                    sb.append("¥");
                    sb.append(partnerWelfareLog.getScoreA() / 100.0);
                    sb.append("红包+");
                    sb.append(partnerWelfareLog.getScoreB());
                    sb.append("积分");
                    keys[0] = sb.toString();
                } else {
                    sb.append("¥");
                    sb.append(partnerWelfareLog.getScoreA() / 100.0);
                    sb.append("红包");
                    keys[0] = sb.toString();
                }
            } else {
                sb.append(partnerWelfareLog.getScoreB());
                sb.append("积分");
                keys[0] = sb.toString();
            }
            sb.setLength(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            keys[1] = dateFormat.format(new Date());
            if (StringUtils.isBlank(partnerWelfareLog.getDescription())) {
                sb.append("这里应该有留言,但");
                sb.append(partnerWelfareLog.getPartner().getPartnerName());
                sb.append("比较懒");
                keys[2] = sb.toString();
            } else {
                keys[2] = partnerWelfareLog.getDescription();
            }

            HashMap<String, Object> mapRemark = new HashMap<>();
            mapRemark.put("value", "点击查看详情");
            mapRemark.put("color", "#173177");
            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("remark", mapRemark);

            //为商户发送模版消息
            WxTemMsg wxTemMsg = wxTemMsgRepository.findOne(5L);

            HashMap<String, Object> mapfirst = new HashMap<>();
            mapfirst.put("value", partnerWelfareLog.getPartner().getPartnerName()+wxTemMsg.getFirst());
            mapfirst.put("color", wxTemMsg.getColor());
            map2.put("first", mapfirst);

            int i = 1;

            for (String key : keys) {
                HashMap<String, Object> mapKey = new HashMap<>();
                mapKey.put("value", key);
                mapKey.put("color", "");
                map2.put("keyword" + i, mapKey);
                i++;
            }

            // 先封装一个 JSON 对象
            JSONObject param = new JSONObject();

            param.put("touser", openId);
            param.put("template_id", wxTemMsg.getTemplateId());
            if (partnerWelfareLog.getRedirectUrl() == 0) {
                param.put("url", "http://www.lepluslife.com/weixin/user");
            } else if (partnerWelfareLog.getRedirectUrl() == 1) {
                param.put("url", "http://www.lepluslife.com/front/product/weixin/productIndex");
            } else {
                param.put("url", "http://www.lepluslife.com/merchant/index");
            }
            param.put("data", map2);

            sendTemplateMessage(param, 7L);
        }).start();

    }

    /**
     * 发送模板消息
     */
    private void sendTemplateMessage(JSONObject param, Long wxId) {
        try {
            // 绑定到请求 Entry

            StringEntity
                se =
                new StringEntity(new String(param.toString().getBytes("utf8"), "iso8859-1"));

            //获取token
            String token = dictionaryService.findDictionaryById(wxId).getValue();

            String
                getUrl =
                "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + token;
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(getUrl);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(se);
            CloseableHttpResponse response = null;

            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, String>
//                map =
//                mapper.readValue(
//                    new BufferedReader(new InputStreamReader(entity.getContent(), "utf-8")),
//                    Map.class);
//
//            EntityUtils.consume(entity);
//            //如果catch到异常,则跳出递归,并且纪录bug
//            if (!map.get("errmsg").equals("ok") && !String.valueOf(map.get("errcode"))
//                .equals("43004")) {
//                log.error("出现异常" + map.toString());
//            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

