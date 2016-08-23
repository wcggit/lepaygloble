package com.jifenke.lepluslive.merchant.service;

import com.jifenke.lepluslive.barcode.BarcodeConfig;
import com.jifenke.lepluslive.barcode.service.BarcodeService;
import com.jifenke.lepluslive.filemanage.service.FileImageService;
import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.lejiauser.domain.entities.RegisterOrigin;
import com.jifenke.lepluslive.lejiauser.repository.RegisterOriginRepository;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantProtocol;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantType;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;
import com.jifenke.lepluslive.merchant.domain.entities.OpenRequest;
import com.jifenke.lepluslive.merchant.repository.MerchantProtocolRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantTypeRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantUserRepository;
import com.jifenke.lepluslive.merchant.repository.MerchantWalletRepository;
import com.jifenke.lepluslive.merchant.repository.OpenRequestRepository;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.partner.domain.entities.Partner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
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


    /**
     * 获取商家详情
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Merchant findMerchantById(Long id) {
        return merchantRepository.findOne(id);
    }


    public MerchantWallet findMerchantWalletByMerchant(Merchant merchant) {
        return merchantWalletRepository.findByMerchant(merchant);
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
        return merchantRepository.countByPartner(partner);
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
        origin.setName(merchant.getName());
        origin.setLocation(merchant.getLocation());
        // origin.setState(merchant.getState());
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
}
