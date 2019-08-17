package com.jifenke.lepluslive.yibao.domain.entities;

import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 易宝子商户
 * Created by zhangwen on 2017/7/11.
 */
@Entity
@Table(name = "YB_MERCHANT_USER_LEDGER")
public class MerchantUserLedger {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date dateCreated = new Date();

  private Date dateUpdated = new Date();

  @Column(nullable = false, unique = true, length = 50)
  private String ledgerNo;  //易宝的子商户号

  private Integer state = 0;  //状态   0=冻结(待审核)|1=审核成功|2=审核失败

  private Integer checkState = -1;   //-1=初始化|0=修改审核中|1=修改审核成功|2=修改审核失败

  @ManyToOne
  @NotNull
  private MerchantUser merchantUser;

  @Column(nullable = false, length = 30)
  private String signedName; //签约名

  private Integer customerType = 1;  //注册类型  1=PERSON(个人)|| 2=ENTERPRISE(企业)

  @Column(nullable = false, length = 50)
  private String linkman; //联系人姓名

  @Column(nullable = false, length = 11)
  private String bindMobile;  //绑定手机号

  @Column(length = 50)
  private String legalPerson; //法人姓名 当customerType=2时必填

  @Column(nullable = false, length = 18)
  private String idCard;  //身份证号 customerType=1时，为个人身份证号 ，=2时，为法人身份证号

  @Column(length = 30)
  private String businessLicence;  //营业执照号   当customerType=2时必填

  private Integer bankAccountType = 1;  //结算账户类型 1=PrivateCash(对私)||2=PublicCash(对公)

  @Column(nullable = false, length = 30)
  private String bankAccountNumber;  //出款用的银行卡号【必须为储蓄卡】

  @Column(nullable = false, length = 50)
  private String bankName;  //开户行 根据数据字典「中国所有银行支行省市库表.xls」，填写中文

  @Column(nullable = false, length = 30)
  private String accountName; //开户名

  @Column(nullable = false, length = 10)
  private String bankProvince; //开户省 银行卡开户行所在省，请根据数据字典「易宝省市编号表.xls」，填写中文

  @Column(nullable = false, length = 20)
  private String bankCity; //开户市 银行卡开户行所在市「易宝省市编号表.xls」，请根据数据字典，填写中文

  private Integer minSettleAmount = 1;  //起结金额 单位/分,最小为1

  private Integer riskReserveDay = 1;  //结算周期 目前固定为1

  private Integer manualSettle = 0;  //结算方式  0=自动结算||1=手动结算   本期写死是自动结算

  private Integer costSide = 0;   //结算费用承担方  0=积分客（主商户）|1=子商户

  public Integer getCostSide() {
    return costSide;
  }

  public void setCostSide(Integer costSide) {
    this.costSide = costSide;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getManualSettle() {
    return manualSettle;
  }

  public void setManualSettle(Integer manualSettle) {
    this.manualSettle = manualSettle;
  }

  public Integer getRiskReserveDay() {
    return riskReserveDay;
  }

  public void setRiskReserveDay(Integer riskReserveDay) {
    this.riskReserveDay = riskReserveDay;
  }

  public Date getDateUpdated() {
    return dateUpdated;
  }

  public void setDateUpdated(Date dateUpdated) {
    this.dateUpdated = dateUpdated;
  }

  public Integer getMinSettleAmount() {
    return minSettleAmount;
  }

  public void setMinSettleAmount(Integer minSettleAmount) {
    this.minSettleAmount = minSettleAmount;
  }

  public Integer getCheckState() {
    return checkState;
  }

  public void setCheckState(Integer checkState) {
    this.checkState = checkState;
  }

  public String getBindMobile() {
    return bindMobile;
  }

  public void setBindMobile(String bindMobile) {
    this.bindMobile = bindMobile;
  }

  public String getBankCity() {
    return bankCity;
  }

  public void setBankCity(String bankCity) {
    this.bankCity = bankCity;
  }

  public String getBankProvince() {
    return bankProvince;
  }

  public void setBankProvince(String bankProvince) {
    this.bankProvince = bankProvince;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBankAccountNumber() {
    return bankAccountNumber;
  }

  public void setBankAccountNumber(String bankAccountNumber) {
    this.bankAccountNumber = bankAccountNumber;
  }

  public Integer getBankAccountType() {
    return bankAccountType;
  }

  public void setBankAccountType(Integer bankAccountType) {
    this.bankAccountType = bankAccountType;
  }

  public String getBusinessLicence() {
    return businessLicence;
  }

  public void setBusinessLicence(String businessLicence) {
    this.businessLicence = businessLicence;
  }

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public String getLedgerNo() {
    return ledgerNo;
  }

  public void setLedgerNo(String ledgerNo) {
    this.ledgerNo = ledgerNo;
  }

  public MerchantUser getMerchantUser() {
    return merchantUser;
  }

  public void setMerchantUser(MerchantUser merchantUser) {
    this.merchantUser = merchantUser;
  }

  public String getSignedName() {
    return signedName;
  }

  public void setSignedName(String signedName) {
    this.signedName = signedName;
  }

  public Integer getCustomerType() {
    return customerType;
  }

  public void setCustomerType(Integer customerType) {
    this.customerType = customerType;
  }

  public String getLinkman() {
    return linkman;
  }

  public void setLinkman(String linkman) {
    this.linkman = linkman;
  }

  public String getLegalPerson() {
    return legalPerson;
  }

  public void setLegalPerson(String legalPerson) {
    this.legalPerson = legalPerson;
  }


}
