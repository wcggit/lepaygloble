package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.barcode.BarcodeConfig;
import com.jifenke.lepluslive.barcode.service.BarcodeService;
import com.jifenke.lepluslive.filemanage.service.FileImageService;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.RegisterOrigin;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.lejiauser.repository.RegisterOriginRepository;
import com.jifenke.lepluslive.merchant.domain.entities.*;
import com.jifenke.lepluslive.merchant.repository.*;
import com.jifenke.lepluslive.order.domain.entities.MerchantScanPayWay;
import com.jifenke.lepluslive.order.repository.MerchantScanPayWayRepository;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.weixin.repository.WeiXinUserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Created by wcg on 16/3/17.
 */
@Service
@Transactional(readOnly = true)
public class MerchantService {

    @Inject
    private MerchantRepository merchantRepository;


    @Inject
    private MerchantWalletRepository merchantWalletRepository;

    @Inject
    private MerchantUserRepository merchantUserRepository;

    @Inject
    private OpenRequestRepository openRequestRepository;

    @Inject
    private MerchantTypeRepository merchantTypeRepository;

    @Inject
    private BarcodeService barcodeService;

    @Inject
    private FileImageService fileImageService;

    @Inject
    private MerchantProtocolRepository merchantProtocolRepository;

    @Inject
    private RegisterOriginRepository registerOriginRepository;

    @Value("${bucket.ossBarCodeReadRoot}")
    private String barCodeRootUrl;

    @Inject
    private MerchantWeiXinUserService merchantWeiXinUserService;

    @Inject
    private LeJiaUserRepository leJiaUserRepository;

    @Inject
    private WeiXinUserRepository weiXinUserRepository;

    @Inject
    private PartnerService partnerService;

    @Inject
    private MerchantInfoRepository merchantInfoRepository;

    @Inject
    private MerchantScanPayWayRepository merchantScanPayWayRepository;

    @Inject
    private MerchantBankRepository merchantBankRepository;

    /**
     * 获取商家详情
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Merchant findMerchantById(Long id) {
        return merchantRepository.findOne(id);
    }

    // 旧版本
    public MerchantWallet findMerchantWalletByMerchant(Merchant merchant) {
        return merchantWalletRepository.findByMerchant(merchant);
    }

    // 新版本 商户平台 2.0
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public Map findCommissionByMerchants(List<Merchant> merchants) {
        Map map = new HashMap();
        Long available = 0L;
        Long totalCommission = 0L;
        for (Merchant merchant : merchants) {
            MerchantWallet merchantWallet = merchantWalletRepository.findByMerchant(merchant);
            available += merchantWallet.getAvailableBalance()==null ? 0L:merchantWallet.getAvailableBalance();
            totalCommission += merchantWallet.getTotalMoney()==null ? 0L:merchantWallet.getTotalMoney();
        }
        map.put("available",available);
        map.put("totalCommission",totalCommission);
        return map;
    }

    public MerchantUser findMerchantUserByName(String name) {
        return merchantUserRepository.findByName(name).get();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void createOpenRequest(Merchant merchant) {
        OpenRequest openRequest = openRequestRepository.findByMerchant(merchant);
        if (openRequest == null) {
            openRequest = new OpenRequest();
            openRequest.setState(0);
            openRequest.setMerchant(merchant);
        }
        openRequestRepository.save(openRequest);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void resetPasswword(MerchantUser merchantUser, String reset, String password) {
        String origin = MD5Util.MD5Encode(password, "utf-8");

        if (merchantUser.getPassword().equals(origin)) {
            merchantUser.setPassword(MD5Util.MD5Encode(reset, "utf-8"));
            merchantUserRepository.save(merchantUser);
        } else {
            throw new RuntimeException();
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Long countPartnerBindMerchant(Partner partner) {
        return merchantRepository.countByPartnerAndPartnershipNot(partner, 2);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Merchant findmerchantBySid(String sid) {
        return merchantRepository.findByMerchantSid(sid);
    }

    public List<MerchantUser> findMerchantUserByMerchant(Merchant merchant) {
        return merchantUserRepository.findAllByMerchant(merchant);
    }

    public List<MerchantType> findAllMerchantTypes() {
        return merchantTypeRepository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void createMerchant(Merchant merchant) {
        if (merchant.getId() != null) {
            throw new RuntimeException("新建商户ID不能存在");
        }
        merchant.setSid((int) merchantRepository.count());
        String merchantSid = MvUtil.getMerchantSid();
        while (merchantRepository.findByMerchantSid(merchantSid) != null) {
            merchantSid = MvUtil.getMerchantSid();
        }
        merchant.setMerchantSid(merchantSid);
        byte[]
            bytes =
            new byte[0];
        try {
            bytes = barcodeService.qrCode(Constants.MERCHANT_URL + merchant.getMerchantSid(),
                                          BarcodeConfig.QRCode.defaultConfig());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filePath = MvUtil.getFilePath(Constants.BAR_CODE_EXT);

        merchant.setQrCodePicture(barCodeRootUrl + "/" + filePath);
        final byte[] finalBytes = bytes;
        new Thread(() -> {
            fileImageService.SaveBarCode(finalBytes, filePath);
        }).start();
        //  MerchantInfo   02/12/16
        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfoRepository.save(merchantInfo);
        merchant.setMerchantInfo(merchantInfo);
        merchantRepository.save(merchant);
        RegisterOrigin registerOrigin = new RegisterOrigin();
        registerOrigin.setOriginType(3);
        registerOrigin.setMerchant(merchant);
        MerchantWallet merchantWallet = new MerchantWallet();
        merchantWallet.setMerchant(merchant);
        merchant.getMerchantProtocols().stream().map(merchantProtocol -> {
            merchantProtocol.setMerchant(merchant);
            merchantProtocolRepository.save(merchantProtocol);
            return merchantProtocol;
        }).collect(Collectors.toList());
        merchantWalletRepository.save(merchantWallet);
        registerOriginRepository.save(registerOrigin);
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void editMerchant(Merchant merchant) {
        Merchant origin = merchantRepository.findOne(merchant.getId());
        origin.getMerchantProtocols().stream().map(merchantProtocol -> {
            merchantProtocolRepository.delete(merchantProtocol);
            return null;
        }).collect(Collectors.toList());
        String sid = origin.getMerchantSid();
        if (origin == null) {
            throw new RuntimeException("不存在的商户");
        }
        origin.setLjBrokerage(merchant.getLjBrokerage());
        origin.setLjCommission(merchant.getLjCommission());
        origin.setPicture(merchant.getPicture());
        origin.setName(merchant.getName());
        origin.setLocation(merchant.getLocation());
        origin.setLng(merchant.getLng());
        origin.setLat(merchant.getLat());
        origin.setPartner(merchant.getPartner());
        origin.setArea(merchant.getArea());
        origin.setUserLimit(merchant.getUserLimit());
        origin.setCity(merchant.getCity());
        origin.setContact(merchant.getContact());
        origin.setPayee(merchant.getPayee());
        origin.setCycle(merchant.getCycle());
        origin.setMerchantBank(merchant.getMerchantBank());
        origin.setMerchantPhone(merchant.getMerchantPhone());
        origin.setMerchantProtocols(merchant.getMerchantProtocols());
        origin.setScoreARebate(merchant.getScoreARebate());
        origin.setScoreBRebate(merchant.getScoreBRebate());
        origin.setMerchantType(merchant.getMerchantType());
        origin.setReceiptAuth(merchant.getReceiptAuth());
        origin.setPartnership(merchant.getPartnership());
        origin.setMemberCommission(merchant.getMemberCommission());
        long l = merchant.getId();
        origin.setSid((int) l);
        origin.setMerchantSid(sid);
        origin.getMerchantProtocols().stream().map(merchantProtocol -> {
            merchantProtocol.setMerchant(origin);
            merchantProtocolRepository.save(merchantProtocol);
            return null;
        }).collect(Collectors.toList());
        merchantRepository.save(origin);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void createMerchantUser(Merchant merchant, String username, String password) {
        MerchantUser merchantUser = new MerchantUser();
        merchantUser.setName(username);
        merchantUser.setPassword(MD5Util.MD5Encode(password, "UTF-8"));
        merchantUser.setMerchant(merchant);
        merchantUser.setType(0);
        merchantUserRepository.save(merchantUser);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteMerchantUser(Long id, Partner partner) {
        MerchantUser merchantUser = merchantUserRepository.findOne(id);
        long partnerId = merchantUser.getMerchant().getPartner().getId();
        if (partnerId == partner.getId()) {
            merchantWeiXinUserService.unBindMerchantUser(merchantUser);
            merchantUserRepository.delete(merchantUser);
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 获取合伙人虚拟商户
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Merchant findPartnerVirtualMerchant(Partner partner) {
        return merchantRepository.findVtMerchantByPartner(partner.getId());
    }

    /**
     * 获取合伙人抢福利页面所需数据 16/10/13
     *
     * @return 数据
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map findMerchantCodeData(Merchant merchant) {
        Integer count = null;
        Long scoreAs = null;
        Long scoreBs = null;
        Map<String, Object> map = new HashMap<>();
        String subSource = "4_0_" + merchant.getId();  //关注来源
        //获取注册来源为该商家的用户总数
        count = leJiaUserRepository.countBySubSource(subSource);
        if (count == 0) {
            map.put("inviteM", 0);//邀请会员数
            map.put("totalA", 0); //邀请会员的会员累计获得红包额
            map.put("totalB", 0); //邀请会员的会员累计获得红包额
        } else {
            //邀请会员数
            count = leJiaUserRepository.countBySubSourceAndState(subSource);
            map.put("inviteM", count);
            if (partnerService.findPartnerInfoByPartnerSid(merchant.getPartner().getPartnerSid())
                    .getInviteLimit() == 1) {
                scoreAs = leJiaUserRepository.countScoreAByMerchant(merchant.getPartner().getId());
                scoreBs = leJiaUserRepository.countScoreBByMerchant(merchant.getPartner().getId());
            } else {
                scoreAs = leJiaUserRepository.countScoreABySubSource(
                    subSource);
                scoreBs =
                    leJiaUserRepository.countScoreBBySubSource(subSource);
            }
            map.put("totalA", scoreAs);
            map.put("totalB", scoreBs);
        }
        return map;
    }


    /**
     *  分页获取门店的扫码订单和POS订单数据
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public List<Object[]> findOrderList(Merchant merchant,Long limit) {
        Long offset = limit*10;
        MerchantScanPayWay payway = merchantScanPayWayRepository.findByMerchantId(merchant.getId());
        if(payway==null) {        // 返回 Pos 订单和乐加扫码订单
            return merchantRepository.findOrderListByMerchant(merchant.getId(),offset);
        }else {                   // 返回 Pos 订单和掌富扫码订单
            return merchantRepository.findScanOrderListByMerchant(merchant.getId(),offset);
        }

    }

    /**
     *  商户（管理员） 创建子账号
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public void saveUserAccount(MerchantUser merchantUser) {
        MerchantBank merchantBank = merchantUser.getMerchantBank();
        merchantBankRepository.save(merchantBank);
        merchantUser.setPassword(MD5Util.MD5Encode(merchantUser.getPassword(),null));
        merchantUser.setMerchantBank(merchantBank);
        merchantUserRepository.save(merchantUser);
    }

}
