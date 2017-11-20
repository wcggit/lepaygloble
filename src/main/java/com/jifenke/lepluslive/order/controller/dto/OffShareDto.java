package com.jifenke.lepluslive.order.controller.dto;

import com.jifenke.lepluslive.global.util.DataUtils;
import com.jifenke.lepluslive.lejiauser.domain.entities.LeJiaUser;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrderShare;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 线下分润数据展示
 * Created by zhangwen on 2017/9/16.
 */
public class OffShareDto {

    private String orderSid;

    private String channel;//业务渠道

    private String createdAt;//消费完成时间  yyyy-MM-dd HH:mm:ss

    private String user;//消费者信息

    private long totalPrice;//订单金额

    private long commission;//乐加佣金

    private long scoreA;//发鼓励金

    private long scoreC;//发金币

    private long share;//总分润金额

    private String merchant;//交易门店信息

    private String tradePartner;//交易天使&分润

    private String tradeManager;//交易城市&分润

    private String lockMerchant;//锁定门店&分润

    private String lockPartner;//锁定天使&分润

    private String lockManager;//锁定城市&分润

    private long self;//积分客分润

    /**
     * 锁定门店分润
     */
    private Long toLockMerchant;

    /**
     * 微信昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String imgUrl;

    public Long getToLockMerchant() {
        return toLockMerchant;
    }

    public void setToLockMerchant(Long toLockMerchant) {
        this.toLockMerchant = toLockMerchant;
    }

    public void parseShareInfo(OffLineOrderShare share) {
        this.setOrderSid(share.getOrderSid());
        this.setCreatedAt(DateFormatUtils.format(share.getCreateDate(), DataUtils.sdf4));
        this.setShare(share.getShareMoney());
        this.setSelf(share.getToLePlusLife());
        if (share.getTradeMerchant() != null) {
            this.setMerchant(share.getTradeMerchant().getName());
        }
        if (share.getTradePartner() != null) {
            this.setTradePartner(share.getTradePartner().getName() + "(" + share.getToTradePartner() / 100.0 + ")");
        }
        if (share.getTradePartnerManager() != null) {
            this.setTradeManager(share.getTradePartnerManager().getName() + "(" + share.getToTradePartnerManager() / 100.0 + ")");
        }
        if (share.getLockMerchant() != null) {
            this.setLockMerchant(share.getLockMerchant().getName());
            this.setToLockMerchant(share.getToLockMerchant());
        }
        if (share.getLockPartner() != null) {
            this.setLockPartner(share.getLockPartner().getName() + "(" + share.getToLockPartner() / 100.0 + ")");
        }
        if (share.getLockPartnerManager() != null) {
            this.setLockManager(share.getLockPartnerManager().getName() + "(" + share.getToLockPartnerManager() / 100.0 + ")");
        }
        this.setSelf(share.getToLePlusLife());
    }

    public void parseOrderNumber(OffLineOrderShare share) {
        switch (share.getType()) {
            case 1:
                this.setChannel("乐加扫码");
                if (share.getOffLineOrder() != null) {
                    this.setTotalPrice(share.getOffLineOrder().getTotalPrice());
                    this.setCommission(share.getOffLineOrder().getLjCommission());
                    this.setScoreA(share.getOffLineOrder().getRebate());
                    this.setScoreC(share.getOffLineOrder().getScoreC());
                }
                break;
            case 2:
                this.setChannel("富友POS");
                if (share.getPosOrder() != null) {
                    this.setTotalPrice(share.getPosOrder().getTotalPrice());
                    this.setCommission(share.getPosOrder().getLjCommission());
                    this.setScoreA(share.getPosOrder().getRebate());
                    this.setScoreC(share.getPosOrder().getScoreB());
                }
                break;
            case 3:
                this.setChannel("银商POS");
                if (share.getUnionPosOrder() != null) {
                    this.setTotalPrice(share.getUnionPosOrder().getTotalPrice());
                    this.setCommission(share.getUnionPosOrder().getCommission());
                    this.setScoreA(share.getUnionPosOrder().getRebate());
                    this.setScoreC(share.getUnionPosOrder().getScoreC());
                }
                break;
            case 4:
                this.setChannel("通道扫码");
                if (share.getScanCodeOrder() != null) {
                    this.setTotalPrice(share.getScanCodeOrder().getTotalPrice());
                    this.setCommission(share.getScanCodeOrder().getCommission());
                    this.setScoreA(share.getScanCodeOrder().getRebate());
                    this.setScoreC(share.getScanCodeOrder().getScoreC());
                }
                break;
        }
    }

    public static String userParse(LeJiaUser user) {
        if (user != null) {
            if (StringUtils.isNotEmpty(user.getPhoneNumber())) {
                return user.getPhoneNumber();
            } else {
                return user.getUserSid();
            }
        }
        return "未知";
    }

    public String getOrderSid() {
        return orderSid;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getCommission() {
        return commission;
    }

    public void setCommission(long commission) {
        this.commission = commission;
    }

    public long getScoreA() {
        return scoreA;
    }

    public void setScoreA(long scoreA) {
        this.scoreA = scoreA;
    }

    public long getScoreC() {
        return scoreC;
    }

    public void setScoreC(long scoreC) {
        this.scoreC = scoreC;
    }

    public long getShare() {
        return share;
    }

    public void setShare(long share) {
        this.share = share;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getTradePartner() {
        return tradePartner;
    }

    public void setTradePartner(String tradePartner) {
        this.tradePartner = tradePartner;
    }

    public String getTradeManager() {
        return tradeManager;
    }

    public void setTradeManager(String tradeManager) {
        this.tradeManager = tradeManager;
    }

    public String getLockMerchant() {
        return lockMerchant;
    }

    public void setLockMerchant(String lockMerchant) {
        this.lockMerchant = lockMerchant;
    }

    public String getLockPartner() {
        return lockPartner;
    }

    public void setLockPartner(String lockPartner) {
        this.lockPartner = lockPartner;
    }

    public String getLockManager() {
        return lockManager;
    }

    public void setLockManager(String lockManager) {
        this.lockManager = lockManager;
    }

    public long getSelf() {
        return self;
    }

    public void setSelf(long self) {
        this.self = self;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
