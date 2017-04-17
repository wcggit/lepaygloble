package com.jifenke.lepluslive.web;


import com.jifenke.lepluslive.Application;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.order.repository.OffLineOrderRepository;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerManager;
import com.jifenke.lepluslive.partner.repository.PartnerManagerRepository;
import com.jifenke.lepluslive.partner.repository.PartnerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by xf on 2016/10/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles({Constants.SPRING_PROFILE_DEVELOPMENT})
public class SimpleTest {

/*  @Inject
    private PartnerScoreLogService scoreLogService;
    @Inject
    private WeiXinUserService weiXinUserService;

    @Test
    public void simpleTest() {
        WeiXinUserInfoCriteria infoCriteria = new WeiXinUserInfoCriteria();
        infoCriteria.setOffset(1);
        infoCriteria.setSubSource("4_0_875");
        Map map = weiXinUserService.findPageByInfoCriteria(infoCriteria, 10);
        Page page  = (Page) map.get("page");
        List<WeiXinUser> list = page.getContent();
        for (WeiXinUser user : list) {
            System.out.println(user.getLeJiaUser().getId()+"---"+user.getHeadImageUrl()+"---------"+user.getDateCreated());
        }
        List wxScoreas =(List) map.get("wxScoreas");
        List wxScorebs =(List) map.get("wxScorebs");
        System.out.println("wxScoreas:"+wxScoreas.toString()+"-------wxScorebs: "+wxScorebs.toString());
        System.out.println("共查询到了:  "+page.getTotalElements()+"条记录.");
    }*/

    @Inject
    private OffLineOrderRepository offLineOrderRepository;

    @Inject
    private PartnerManagerRepository partnerManagerRepository;

    @Inject
    private PartnerRepository partnerRepository;

    @Test
    public void dateTest() {
        // 当前时间
        Date date = new Date();
        // 一天的毫秒值
        Long dayMills = 86400000L;
        // 存放最近七天的日期
        List<String> dates= new ArrayList<>();
        for (int i=0;i<7;i++) {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
            date.setTime(date.getTime()-86400000L);
            dates.add(dateStr);
        }
        // 查看最近七天的日期
        System.out.println(dates);

        // 从数据库查询
        List<Object[]> objects = offLineOrderRepository.countWeekOfflineWx(10L, new Date());
        for (Object[] object : objects) {
            System.out.println(Arrays.toString(object));
        }
    }


    /**
     *  创建城市合伙人账号
     */
    @Test
    public void saverPartnerMerchantAccount() {
        List<PartnerManager> managers = partnerManagerRepository.findAll();
        for (PartnerManager manager : managers) {
            if(manager.getPartnerId()==null) {
                Partner partner = new Partner();
                partner.setName(manager.getName());
                partner.setPartnerName(manager.getName());
                partner.setPassword(MD5Util.MD5Encode("123456",null));
                partner.setPartnerManager(manager);
                partner.setBankName(manager.getBankName());
                partner.setPayee(manager.getBankName());
                partner.setPartnerSid(MvUtil.getMerchantSid());
                partnerRepository.save(partner);
                manager.setPartnerId(partner.getId());
                partnerManagerRepository.save(manager);
            }
        }
    }
}
