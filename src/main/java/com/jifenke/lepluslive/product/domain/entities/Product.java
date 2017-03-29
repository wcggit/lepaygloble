package com.jifenke.lepluslive.product.domain.entities;

import com.jifenke.lepluslive.weixin.domain.entities.Category;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wcg on 16/3/9.
 */
@Entity
@Table(name = "PRODUCT")
public class Product implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Date createDate = new Date();  //创建时间

  private Date lastUpdate;   //最后修改时间

  private Long sid;

  private Integer type = 1; //商品类型 1=常规|2=秒杀

  @NotNull
  private String name;

  @NotNull
  private String picture;

  @NotNull
  private Long price;    //市场价

  @Column(name = "min_price")
  @NotNull
  private Long minPrice;   //购买最低金额

  private Integer minScore = 0;  //兑换最低所需积分

  @Column(name = "sale_num")
  private Integer saleNumber = 0;

  private Integer customSale = 0;  //自定义起始销售量

  @Column(name = "points_count")
  private Long pointsCount = 0L;   // 该商品的所有订单使用的积分加和  不再使用

  @Column(name = "packet_count")
  private Long packetCount = 0L; //该商品的所有订单发放的红包加和    不再使用

  private String description;

  private Integer state; //1=上架|0=已下架

  private String thumb;//缩略图,显示在订单里|或爆款图片

  private String qrCodePicture;

  private Integer postage = 0;   //该商品所需邮费 0=包邮

  private Integer freePrice = 0;  //不包邮时，满此价格包邮，针对普通商品

  private Integer buyLimit = 1;  //每个用户限购数量 0=无限制

  private Integer hotStyle = 0;  //1=爆款

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "product_type_id")
  private ProductType productType;

  @ManyToOne
  private Category mark; //商品角标  为null时无角标

  public String getQrCodePicture() {
    return qrCodePicture;
  }

  public void setQrCodePicture(String qrCodePicture) {
    this.qrCodePicture = qrCodePicture;
  }

  public String getThumb() {
    return thumb;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setThumb(String thumb) {
    this.thumb = thumb;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProductType getProductType() {
    return productType;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSid() {
    return sid;
  }

  public void setSid(Long sid) {
    this.sid = sid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public Long getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(Long minPrice) {
    this.minPrice = minPrice;
  }

  public Integer getSaleNumber() {
    return saleNumber;
  }

  public void setSaleNumber(Integer saleNumber) {
    this.saleNumber = saleNumber;
  }

  public Long getPointsCount() {
    return pointsCount;
  }

  public Category getMark() {
    return mark;
  }

  public void setMark(Category mark) {
    this.mark = mark;
  }

  public void setPointsCount(Long pointsCount) {
    this.pointsCount = pointsCount;
  }

  public Long getPacketCount() {
    return packetCount;
  }

  public void setPacketCount(Long packetCount) {
    this.packetCount = packetCount;
  }

  public Integer getFreePrice() {
    return freePrice;
  }

  public void setFreePrice(Integer freePrice) {
    this.freePrice = freePrice;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getMinScore() {
    return minScore;
  }

  public void setMinScore(Integer minScore) {
    this.minScore = minScore;
  }

  public Integer getCustomSale() {
    return customSale;
  }

  public void setCustomSale(Integer customSale) {
    this.customSale = customSale;
  }

  public Integer getPostage() {
    return postage;
  }

  public void setPostage(Integer postage) {
    this.postage = postage;
  }

  public Integer getBuyLimit() {
    return buyLimit;
  }

  public void setBuyLimit(Integer buyLimit) {
    this.buyLimit = buyLimit;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getHotStyle() {
    return hotStyle;
  }

  public void setHotStyle(Integer hotStyle) {
    this.hotStyle = hotStyle;
  }
}
