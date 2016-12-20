package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.ImageLoad;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.lejiauser.domain.criteria.LeJiaUserCriteria;
import com.jifenke.lepluslive.lejiauser.repository.LeJiaUserRepository;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.controller.dto.MerchantDto;
import com.jifenke.lepluslive.merchant.domain.criteria.LockMemberCriteria;
import com.jifenke.lepluslive.merchant.domain.entities.*;
import com.jifenke.lepluslive.merchant.service.MerchantRebatePolicyService;
import com.jifenke.lepluslive.merchant.service.MerchantUserResourceService;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.service.FinanicalStatisticService;
import com.jifenke.lepluslive.order.service.OffLineOrderService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wcg on 16/6/29.
 */
@RestController
@RequestMapping("/api")
public class MerchantController {

    private String
        backgroundPicture =
        "http://lepluslive-image.oss-cn-beijing.aliyuncs.com/20160701164034O6wvP4QZ5X.png";

    @Inject
    private MerchantService merchantService;

    @Inject
    private FinanicalStatisticService finanicalStatisticService;

    @Inject
    private LeJiaUserService leJiaUserService;

    @Inject
    private PartnerService partnerService;

    @Inject
    private MerchantRebatePolicyService merchantRebatePolicyService;

    @Inject
    private MerchantUserResourceService merchantUserResourceService;

    @Inject
    private OffLineOrderService offLineOrderService;

    @Inject
    private LeJiaUserRepository leJiaUserRepository;

    //  旧版本 - 我的乐付
    @RequestMapping(value = "/merchant/getCommission", method = RequestMethod.GET)
    public LejiaResult getAvaliableCommission() {
        Merchant
            merchant =
            merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin())
                .getMerchant();
        MerchantWallet
            merchantWallet =
            merchantService.findMerchantWalletByMerchant(merchant);
        Long transfering = finanicalStatisticService.countDailyTransfering(merchant);

        return LejiaResult.ok(new MerchantDto(transfering, merchantWallet.getTotalTransferMoney(),
            merchantWallet.getAvailableBalance(),
            merchantWallet.getTotalMoney()));
    }

    /**
     * 首页 - 商户数据
     * a.今日入账
     * b.累计入账
     * c.消费金额/次数
     * d.接受红包总额
     *
     * @return
     */
    @RequestMapping(value = "/merchant/homePage/merchantData", method = RequestMethod.GET)
    public LejiaResult getHomePageMerchantData() {
        MerchantUser merchantUser = merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        Long transfering = offLineOrderService.countDailyTransfering(merchants);                            //  今日转账金额
        Long totalTransfering = finanicalStatisticService.countTotalTransfering(merchants);                 //  转账总金额
        Map<String, Long> detail = offLineOrderService.countMemberOrdersDetail(merchants);
        detail.put("transfering", transfering);
        detail.put("totalTransfering", totalTransfering);
        return LejiaResult.ok(detail);
    }


    @RequestMapping(value = "/merchant", method = RequestMethod.GET)
    public LejiaResult getMerchant() {
        Merchant
            merchant =
            merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin())
                .getMerchant();

        return LejiaResult.ok(merchant);
    }

    @RequestMapping(value = "/merchant/wallet", method = RequestMethod.GET)
    public LejiaResult getMerchantWallet() {
        Merchant
            merchant =
            merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin())
                .getMerchant();

        return LejiaResult.ok(merchantService.findMerchantWalletByMerchant(merchant));
    }


    @RequestMapping(value = "/merchant/downLoadQrCode", method = RequestMethod.GET)
    public void downLoadQrCode(HttpServletResponse response,
                               @RequestParam(required = false) String sid) {
        Merchant
            merchant = null;
        if (sid != null && sid != "") {
            merchant = merchantService.findmerchantBySid(sid);
        } else {
            merchant = merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin())
                .getMerchant();
        }

        response.setContentType("application/x-msdownload;");
        response.setHeader("Content-disposition", "attachment; filename=image.png");
        response.setCharacterEncoding("UTF-8");
        OutputStream outputSream = null;
        // byte[] image =ImageLoad.saveToFile(url) ;
        try {
            outputSream = response.getOutputStream();
            InputStream qrCode = ImageLoad.returnStream(merchant.getQrCodePicture());
            InputStream back = ImageLoad.returnStream(backgroundPicture);
            BufferedImage image = ImageIO.read(back);
            Graphics2D g = image.createGraphics();

            BufferedImage logo = ImageIO.read(qrCode);

            int widthLogo = logo.getWidth(), heightLogo = logo.getHeight();

            // 计算图片放置位置
            int width = image.getWidth();
            int height = image.getHeight();
            System.out.println(width + height);
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - logo.getHeight()) / 2;
            //开始绘制图片
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
            g.drawRoundRect(x, y, widthLogo, heightLogo, 20, 20);
            g.setStroke(new BasicStroke(1.0f));
            g.setColor(Color.white);
            g.drawRect(x, y, widthLogo, heightLogo);
            //写文子
            g.setColor(Color.WHITE);
            g.setBackground(Color.red);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(new Font("微软雅黑", Font.PLAIN, 88)); //字体、字型、字号
            g.drawString(merchant.getName(),
                (image.getWidth() - g.getFontMetrics().stringWidth(merchant.getName()))
                    / 2, 1700); //画文字

            g.dispose();

            int len = 0;
            InputStream is = null;
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            ImageIO.write(image, "png", ImageIO.createImageOutputStream(bs));
            is = new ByteArrayInputStream(bs.toByteArray());
            while ((len = is.read(buf, 0, 1024)) != -1) {
                outputSream.write(buf, 0, len);
            }
            outputSream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/merchant/bindUsers", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult getMerchantBindUserList(@RequestBody LeJiaUserCriteria leJiaUserCriteria) {
        if (leJiaUserCriteria.getOffset() == null) {
            leJiaUserCriteria.setOffset(1);
        }
        MerchantUser
            merchantUserByName =
            merchantService
                .findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        leJiaUserCriteria.setMerchant(merchantUserByName.getMerchant());
        return LejiaResult.ok(leJiaUserService.getMerchantBindUserList(leJiaUserCriteria));
    }

    @RequestMapping(value = "/merchant/totalPages", method = RequestMethod.POST)
    public
    @ResponseBody
    LejiaResult getMerchantBindUserTotalPage(@RequestBody LeJiaUserCriteria leJiaUserCriteria) {
        if (leJiaUserCriteria.getOffset() == null) {
            leJiaUserCriteria.setOffset(1);
        }
        MerchantUser
            merchantUserByName =
            merchantService
                .findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        leJiaUserCriteria.setMerchant(merchantUserByName.getMerchant());
        return LejiaResult.ok(leJiaUserService.getTotalPages(leJiaUserCriteria));
    }

    @RequestMapping(value = "/merchant/open", method = RequestMethod.GET)
    public void openRequest() {
        Merchant
            merchant =
            merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin())
                .getMerchant();
        merchantService.createOpenRequest(merchant);

        merchantService.findAllMerchantTypes();
    }

    @RequestMapping(value = "/merchant/merchantType", method = RequestMethod.GET)
    public
    @ResponseBody
    List<MerchantType> findAllMerchantType() {
        return merchantService.findAllMerchantTypes();
    }

    @RequestMapping(value = "/merchant/resetPassword", method = RequestMethod.POST)
    public LejiaResult resetPassword(HttpServletRequest request) {
        String reset = request.getParameter("reset");
        String password = request.getParameter("password");

        MerchantUser
            merchantUser =
            merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin());

        try {
            merchantService.resetPasswword(merchantUser, reset, password);
            return LejiaResult.ok();
        } catch (Exception e) {
            return LejiaResult.build(400, "密码不正确");
        }
    }

    @RequestMapping(value = "/merchant/createMerchant", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public LejiaResult createMerchant(
        @RequestBody Merchant merchant) {
        merchant.setPartner(partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin()));
        merchantService.createMerchant(merchant);
        MerchantRebatePolicy merchantRebatePolicy = new MerchantRebatePolicy();
        if (merchant.getPartnership() == 0) {
            merchantRebatePolicy.setImportScoreBScale(new BigDecimal(0));
            merchantRebatePolicy.setUserScoreBScaleB(new BigDecimal(0));
            merchantRebatePolicy.setUserScoreBScale(new BigDecimal(0));
            merchantRebatePolicy.setUserScoreAScale(new BigDecimal(0));
            merchantRebatePolicy.setRebateFlag(2);     // 不发放
        }
        if (merchant.getPartnership() == 1) {
            int result = merchant.getLjCommission().intValue() * 5;
            merchantRebatePolicy.setImportScoreBScale(new BigDecimal(result));
            merchantRebatePolicy.setUserScoreBScaleB(new BigDecimal(result));
            merchantRebatePolicy.setUserScoreBScale(new BigDecimal(0));
            merchantRebatePolicy.setUserScoreAScale(new BigDecimal(0));
            merchantRebatePolicy.setRebateFlag(1);      // 全额发放
        }
        // 发放策略
        merchantRebatePolicy.setMerchantId(merchant.getId());
        merchantRebatePolicy.setStageOne(0);
        merchantRebatePolicy.setStageTwo(0);
        merchantRebatePolicy.setStageThree(0);
        merchantRebatePolicy.setStageFour(0);
        merchantRebatePolicyService.saveMerchantRebatePolicy(merchantRebatePolicy);
        return LejiaResult.ok("添加商户成功");
    }

    @RequestMapping(value = "/merchant/editMerchant", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public LejiaResult eidtMerchant(@RequestBody Merchant merchant) {
        merchant.setPartner(partnerService.findByPartnerSid(SecurityUtils.getCurrentUserLogin()));
        merchantService.editMerchant(merchant);

        return LejiaResult.ok("修改商户成功");
    }

    /**
     * 新版本: 查询指定门店所有绑定用户,如果没有指定门店,则查询当前商户下所有门店绑定用户
     */
    @RequestMapping(value = "/merchantUser/bindUsers", method = RequestMethod.POST)
    public LejiaResult findBindUsersByCriteria(@RequestBody LockMemberCriteria lockMemberCriteria) {
        MerchantUser merchantUser = merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        //  设置查询条件:  如果没有指定 id , 则查询所有门店
        setCriteriaMerchantIds(merchantUser,lockMemberCriteria);
        //  数据封装
        List<Object[]> lockMembers = leJiaUserService.getMerchantLockMemberByPage(lockMemberCriteria);
        List<Long> scoreas = new ArrayList<>();             //   首次关注红包
        List<Long> scorebs = new ArrayList<>();             //   首次关注积分
        for (Object[] lockMember : lockMembers) {
            Long ljUserId = new Long(lockMember[0].toString());
            Long scorea = leJiaUserRepository.findTotalScorea(ljUserId);
            Long scoreb = leJiaUserRepository.findTotalScoreb(ljUserId);
            scoreas.add(scorea);
            scorebs.add(scoreb);
        }
        Map map = new HashMap();
        map.put("lockMembers", lockMembers);
        map.put("scoreas", scoreas);
        map.put("scorebs", scorebs);
        return LejiaResult.ok(map);
    }

    @RequestMapping(value = "/merchantUser/bindUsers/count", method = RequestMethod.POST)
    public LejiaResult countBindUsersByCriteria(@RequestBody LockMemberCriteria lockMemberCriteria) {
        MerchantUser merchantUser = merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        setCriteriaMerchantIds(merchantUser,lockMemberCriteria);
        Long totalCount = leJiaUserService.countMerchantLockMember(lockMemberCriteria);
        Long totalPages = Math.round(new Double(totalCount * 1.0 / 10.0));
        return LejiaResult.ok(totalPages);
    }

    //  设置查询条件:  如果没有指定 id , 则查询所有门店
    public void setCriteriaMerchantIds(MerchantUser merchantUser,LockMemberCriteria lockMemberCriteria) {
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        if (lockMemberCriteria.getStoreIds() == null && merchants != null) {
            Object[] storeIds = new Object[merchants.size()];
            for (int i = 0; i < storeIds.length; i++) {
                storeIds[i] = merchants.get(i).getId();
            }
            lockMemberCriteria.setStoreIds(storeIds);
        }
    }

    /**
     *  2.0 版本 - 获取商户门店名称和邀请码
     */
    @RequestMapping(value="/merchantUser/inviteCodes")
    public LejiaResult getMerchantsInviteCodes() {
        String codeUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
        MerchantUser merchantUser = merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin());
        List<Merchant> merchants = merchantUserResourceService.findMerchantsByMerchantUser(merchantUser);
        //  封装数据
        Map map = new HashMap();
        List<String> merchantNames = new ArrayList<>();
        List<String> merchantCodes = new ArrayList<>();
        for (Merchant merchant : merchants) {
            MerchantInfo info = merchant.getMerchantInfo();
            if(info!=null) {
                // 判断商户是否有 二维码
                if(info.getTicket()!=null) {
                    merchantNames.add(merchant.getName());
                    merchantCodes.add(codeUrl+info.getTicket());
                }
            }
        }
        map.put("merchantNames",merchantNames);
        map.put("merchantCodes",merchantCodes);
        return LejiaResult.ok(map);
    }
}
