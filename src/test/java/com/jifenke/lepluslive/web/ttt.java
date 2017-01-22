package com.jifenke.lepluslive.web;
//

import com.jifenke.lepluslive.Application;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.partner.domain.criteria.PartnerScoreLogCriteria;
import com.jifenke.lepluslive.partner.domain.criteria.WeiXinUserInfoCriteria;
import com.jifenke.lepluslive.partner.domain.entities.PartnerScoreLog;
import com.jifenke.lepluslive.partner.domain.entities.PartnerWelfareLog;
import com.jifenke.lepluslive.partner.repository.PartnerRepository;
import com.jifenke.lepluslive.partner.service.PartnerScoreLogService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.weixin.repository.WeiXinUserRepository;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 * Created by wcg on 16/4/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles({Constants.SPRING_PROFILE_DEVELOPMENT})
public class ttt {


    @Inject
    private WeiXinUserRepository weiXinUserRepository;

    @Inject
    private LeJiaUserRepository leJiaUserRepository;


    @Inject
    private EntityManagerFactory entityManagerFactory;


    @Inject
    private PartnerService partnerService;

    @Inject
    private MerchantUserRepository merchantUserRepository;

    @Test
    public void tttt() {
//        PartnerWelfareLog partnerWelfareLog = new PartnerWelfareLog();
//        partnerWelfareLog.setScoreA(100L);
//        partnerWelfareLog.setScoreB(1L);
//        new Thread(() -> {
//            for(int i=0;i<100;i++){
//                partnerService.welfareToUser(50,partnerWelfareLog);
//            }
//        }).start();
//        new Thread(() -> {
//            for(int i=0;i<100;i++){
//                partnerService.welfareToUser(50,partnerWelfareLog);
//            }
//        }).start();
//        while (true) {
//
//        }
        List<MerchantUser> users = merchantUserRepository.findAll();
        for (MerchantUser user : users) {
//            user.setMerchantSid(MvUtil.getMerchantUserSid());
//            merchantUserRepository.save(user);
            System.out.println(user.getMerchantSid());
        }

    }

////  public static void main(String[] args) {
////    int x[][] = new int[9][9];
////    for(int i=0;i<9;i++){
////      for(int y=0;y<9;y++){
////        x[i][y]=new Random().nextInt(2);
////      }
////    }
////    Scanner input = new Scanner(System.in);
////    int a = input.nextInt();
////    int b = input.nextInt();
////    int n = input.nextInt();
////
////    for(int z=1;z<n;z++){
////      int m = x[a][b];
////      int a1 = x[a-1][b];
////      int a2 = x[a+1][b];
////      int a3 = x[a][b+1];
////      int a4 = x[a][b-1];
////
////
////
////    }
//
//
//
//  }


}
