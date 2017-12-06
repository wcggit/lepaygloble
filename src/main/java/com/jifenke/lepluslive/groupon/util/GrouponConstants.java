package com.jifenke.lepluslive.groupon.util;

/**
 * 会员返利及佣金分配比率 2017/8/16
 * 单位均为1/10000
 */
public final class GrouponConstants {

  //成本手续费率-计算返金币时使用
  public static final int COST_RATE = 60;

  //返鼓励金、金币计算基数 base = 订单总价 * 佣金费率 - 订单总价 * 成本手续费率

  //返鼓励金占 base 比
  public static final int BACK_A_RATE = 4000;

  //返金币占 base 比
  public static final int BACK_C_RATE = 2000;

  //总分润金额share = base - 返鼓励金 - 返金币

  //交易商圈合伙人分润 = share * 25%
  public static final int TRADE_PARTNER_RATE = 2500;

  //交易城市合伙人分润 = share * 12%
  public static final int TRADE_PARTNER_MANAGER_RATE = 1200;

  //锁定商家分润 = share * 20%  注意：如果没有或没开启 分润 => 积分客
  public static final int LOCK_MERCHANT_RATE = 2000;

  //锁定商圈合伙人分润 = share * 25%  注意：如果没有 分润 => 积分客
  public static final int LOCK_PARTNER_RATE = 2500;

  //锁定城市合伙人分润 = share * 3%  注意：如果没有 分润 => 积分客
  public static final int LOCK_PARTNER_MANAGER_RATE = 300;

  private GrouponConstants() {
  }
}
