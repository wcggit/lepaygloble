package com.jifenke.lepluslive.groupon.util;

/**
 * 计算相关 Created by zhangwen on 2016/11/22.
 */
public class MathUtil {

  private static final int d10000 = 10000;

  /**
   * 根据费率总价计算会员返利及佣金分配  2017/8/16
   *
   * @param base 佣金
   */
  public static GrouponParameter share(long base) {
    GrouponParameter parameter = new GrouponParameter();
    if (base > 0) {
      //返鼓励金
      long scoreA = base * GrouponConstants.BACK_A_RATE / d10000;
      parameter.setScoreA(scoreA);
      if (base - scoreA > 0) {
        //返金币
        long scoreC = base * GrouponConstants.BACK_C_RATE / d10000;
        parameter.setScoreC(scoreC);
        //分润金额
        long share = base - scoreA - scoreC;
        if (share > 0) {
          parameter.setShare(share);
          //交易商圈
          long tradePartner = share * GrouponConstants.TRADE_PARTNER_RATE / d10000;
          parameter.setTradePartner(tradePartner);
          if (share - tradePartner > 0) {
            //交易城市
            long tradePartnerManager = share * GrouponConstants.TRADE_PARTNER_MANAGER_RATE / d10000;
            parameter.setTradePartnerManager(tradePartnerManager);
            if (share - tradePartner - tradePartnerManager > 0) {
              //锁定商家
              long lockMerchant = share * GrouponConstants.LOCK_MERCHANT_RATE / d10000;
              parameter.setLockMerchant(lockMerchant);
              if (share - tradePartner - tradePartnerManager - lockMerchant > 0) {
                //锁定商圈
                long lockPartner = share * GrouponConstants.LOCK_PARTNER_RATE / d10000;
                parameter.setLockPartner(lockPartner);
                if (share - tradePartner - tradePartnerManager - lockMerchant - lockPartner > 0) {
                  //锁定城市
                  long
                      lockPartnerManager =
                      share * GrouponConstants.LOCK_PARTNER_MANAGER_RATE / d10000;
                  parameter.setLockPartnerManager(lockPartnerManager);
                  //积分客总部
                  long
                      lePlus =
                      share - tradePartner - tradePartnerManager - lockMerchant - lockPartner
                      - lockPartnerManager;
                  if (lePlus > 0) {
                    parameter.setLePlus(lePlus);
                  }
                }
              }
            }
          }
        }
      }
    }
    return parameter;
  }

}
