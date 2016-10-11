package com.jifenke.lepluslive.web;
//
import com.jifenke.lepluslive.Application;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.weixin.repository.WeiXinUserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


import java.math.BigInteger;
import java.util.Date;
import java.util.List;

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
  private MerchantRepository merchantRepository;

  @Inject
  private MerchantService merchantService;


  @Test
  public void tttt(){
      EntityManager em = entityManagerFactory.createEntityManager();
      Date date = new Date();
      for(int i =0;i<=100;i++){
          Query query = null;
          if(i%2==0){
              query = em.createNativeQuery("select le_jia_user.id,le_jia_user.bind_merchant_date,le_jia_user.phone_number,le_jia_user.count,wei_xin_user.nickname,wei_xin_user.head_image_url from wei_xin_user,(select le_jia_user.id,le_jia_user.bind_merchant_date,le_jia_user.phone_number,ifnull(counts.total,0) as count from (select * from le_jia_user where le_jia_user.bind_merchant_id = 1 and le_jia_user.bind_merchant_date between '2016/07/01 00:00:00' and '2016/07/08 23:59:59') as le_jia_user left join (select sum(off_line_order_share.to_lock_merchant) as total,off_line_order.le_jia_user_id as id from off_line_order,off_line_order_share where off_line_order.id = off_line_order_share.off_line_order_id and off_line_order_share.lock_merchant_id = 1 group by off_line_order.le_jia_user_id) as counts  on   le_jia_user.id = counts.id) as le_jia_user where le_jia_user.id = wei_xin_user.le_jia_user_id  limit 0,20");
          }else{
              query = em.createNativeQuery("select le_jia_user.id,le_jia_user.bind_merchant_date,le_jia_user.phone_number,le_jia_user.count,wei_xin_user.nickname,wei_xin_user.head_image_url from wei_xin_user,(select le_jia_user.id,le_jia_user.bind_merchant_date,le_jia_user.phone_number,ifnull(counts.total,0) as count from (select * from le_jia_user where le_jia_user.bind_merchant_id = 1 and le_jia_user.bind_merchant_date between '2016/07/01 00:00:00' and '2016/07/08 23:59:59') as le_jia_user left join (select sum(off_line_order_share.to_lock_merchant) as total,off_line_order.le_jia_user_id as id from off_line_order,off_line_order_share where off_line_order.id = off_line_order_share.off_line_order_id and off_line_order_share.lock_merchant_id = 1 group by off_line_order.le_jia_user_id) as counts  on   le_jia_user.id = counts.id) as le_jia_user where le_jia_user.id = wei_xin_user.le_jia_user_id  limit 0,10");
          }

          List<BigInteger> details = query.getResultList();
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
