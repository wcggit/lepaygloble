package com.jifenke.lepluslive.cmbc.domain.entities;


import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 民生结算单
 *
 * @author ZM.Wang
 */
@Entity
public class CmbcSettlement {

    @Id
    private long id;

    private String settleDate;              //清算日期

    private long merchantUserId;            //商户id
    private String merchantName;            //商户名称
    private String cmbcMerchantNo;          //民生子商户号

    private long wxTotal;                   //微信订单总额
    private long wxCommision;               //微信订单总手续费
    private long wxActual;                  //微信订单入账
    private long wxCount;                   //微信入账笔数
    private long wxRefundCount;             //微信退款笔数
    private long wxRefundTotal;             //微信退款总额
    private long wxRefundCommision;         //微信退款总手续费

    private long aliTotal;                  //支付宝订单总额
    private long aliCommision;              //支付宝订单总手续费
    private long aliActual;                 //支付宝订单入账
    private long aliCount;                  //支付宝入账笔数
    private long aliRefundCount;            //支付宝退款笔数
    private long aliRefundTotal;            //支付宝退款总额
    private long aliRefundCommision;        //支付宝退款总手续费

    private long leTotal;                   //乐加通道交易总额
    private long leCommision;               //乐加通道交易总手续费
    private long leActual;                  //乐加通道交易总入账
    private long leCount;                   //乐加通道入账笔数
    private long leRefundCount;             //乐加通道退款笔数
    private long leRefundTotal;             //乐加通道退款总额

    private long scoreTotal;                //使用红包总额
    private long scoreCommision;            //使用红包包总手续费
    private long scoreActual;               //使用红包入账

    private long totalActual;               //总入账

    private String accNo;                   //结算账户号
    private String accName;                 //结算帐户名
    private int accType;                    //1：对私账户，2：对公账户
    private int state;                      //转账状态  0：未转账，1：已转账，2:已挂起

    public long getId() {
        return id;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public long getMerchantUserId() {
        return merchantUserId;
    }

    public String getCmbcMerchantNo() {
        return cmbcMerchantNo;
    }

    public long getWxTotal() {
        return wxTotal;
    }

    public long getWxCommision() {
        return wxCommision;
    }

    public long getWxActual() {
        return wxActual;
    }

    public long getAliTotal() {
        return aliTotal;
    }

    public long getAliCommision() {
        return aliCommision;
    }

    public long getAliActual() {
        return aliActual;
    }

    public long getLeTotal() {
        return leTotal;
    }

    public long getLeCommision() {
        return leCommision;
    }

    public long getLeActual() {
        return leActual;
    }

    public long getScoreTotal() {
        return scoreTotal;
    }

    public long getScoreCommision() {
        return scoreCommision;
    }

    public long getScoreActual() {
        return scoreActual;
    }

    public long getTotalActual() {
        return totalActual;
    }

    public String getAccNo() {
        return accNo;
    }

    public String getAccName() {
        return accName;
    }

    public int getAccType() {
        return accType;
    }

    public int getState() {
        return state;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getWxCount() {
        return wxCount;
    }

    public long getWxRefundCount() {
        return wxRefundCount;
    }

    public long getWxRefundTotal() {
        return wxRefundTotal;
    }

    public long getWxRefundCommision() {
        return wxRefundCommision;
    }

    public long getAliCount() {
        return aliCount;
    }

    public long getAliRefundCount() {
        return aliRefundCount;
    }

    public long getAliRefundTotal() {
        return aliRefundTotal;
    }

    public long getAliRefundCommision() {
        return aliRefundCommision;
    }

    public long getLeCount() {
        return leCount;
    }

    public long getLeRefundCount() {
        return leRefundCount;
    }

    public long getLeRefundTotal() {
        return leRefundTotal;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public void setMerchantUserId(long merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public void setCmbcMerchantNo(String cmbcMerchantNo) {
        this.cmbcMerchantNo = cmbcMerchantNo;
    }

    public void setWxTotal(long wxTotal) {
        this.wxTotal = wxTotal;
    }

    public void setWxCommision(long wxCommision) {
        this.wxCommision = wxCommision;
    }

    public void setWxActual(long wxActual) {
        this.wxActual = wxActual;
    }

    public void setWxCount(long wxCount) {
        this.wxCount = wxCount;
    }

    public void setWxRefundCount(long wxRefundCount) {
        this.wxRefundCount = wxRefundCount;
    }

    public void setWxRefundTotal(long wxRefundTotal) {
        this.wxRefundTotal = wxRefundTotal;
    }

    public void setWxRefundCommision(long wxRefundCommision) {
        this.wxRefundCommision = wxRefundCommision;
    }

    public void setAliTotal(long aliTotal) {
        this.aliTotal = aliTotal;
    }

    public void setAliCommision(long aliCommision) {
        this.aliCommision = aliCommision;
    }

    public void setAliActual(long aliActual) {
        this.aliActual = aliActual;
    }

    public void setAliCount(long aliCount) {
        this.aliCount = aliCount;
    }

    public void setAliRefundCount(long aliRefundCount) {
        this.aliRefundCount = aliRefundCount;
    }

    public void setAliRefundTotal(long aliRefundTotal) {
        this.aliRefundTotal = aliRefundTotal;
    }

    public void setAliRefundCommision(long aliRefundCommision) {
        this.aliRefundCommision = aliRefundCommision;
    }

    public void setLeTotal(long leTotal) {
        this.leTotal = leTotal;
    }

    public void setLeCommision(long leCommision) {
        this.leCommision = leCommision;
    }

    public void setLeActual(long leActual) {
        this.leActual = leActual;
    }

    public void setLeCount(long leCount) {
        this.leCount = leCount;
    }

    public void setLeRefundCount(long leRefundCount) {
        this.leRefundCount = leRefundCount;
    }

    public void setLeRefundTotal(long leRefundTotal) {
        this.leRefundTotal = leRefundTotal;
    }

    public void setScoreTotal(long scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public void setScoreCommision(long scoreCommision) {
        this.scoreCommision = scoreCommision;
    }

    public void setScoreActual(long scoreActual) {
        this.scoreActual = scoreActual;
    }

    public void setTotalActual(long totalActual) {
        this.totalActual = totalActual;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public void setAccType(int accType) {
        this.accType = accType;
    }
}
