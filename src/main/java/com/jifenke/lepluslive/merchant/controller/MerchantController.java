package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.ImageLoad;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.lejiauser.domain.criteria.LeJiaUserCriteria;
import com.jifenke.lepluslive.lejiauser.service.LeJiaUserService;
import com.jifenke.lepluslive.merchant.controller.dto.MerchantDto;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantType;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWallet;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.service.FinanicalStatisticService;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.security.SecurityUtils;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.List;

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


    @RequestMapping(value = "/merchant/getCommission", method = RequestMethod.GET)
    public LejiaResult getAvaliableCommission() {
        Merchant
            merchant =
            merchantService.findMerchantUserByName(SecurityUtils.getCurrentUserLogin())
                .getMerchant();
        MerchantWallet
            merchantWallet =
            merchantService.findMerchantWalletByMerchant(merchant);
        Long transfering = finanicalStatisticService.countTransfering(merchant);

        return LejiaResult.ok(new MerchantDto(transfering, merchantWallet.getTotalTransferMoney(),
                                              merchantWallet.getAvailableBalance(),
                                              merchantWallet.getTotalMoney()));
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
        merchant.setPartner(partnerService.findPartnerByName(SecurityUtils.getCurrentUserLogin()));
        merchantService.createMerchant(merchant);

        return LejiaResult.ok("添加商户成功");
    }

    @RequestMapping(value = "/merchant/editMerchant", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public LejiaResult eidtMerchant(@RequestBody Merchant merchant) {
        merchant.setPartner(partnerService.findPartnerByName(SecurityUtils.getCurrentUserLogin()));
        merchantService.editMerchant(merchant);

        return LejiaResult.ok("修改商户成功");
    }

}
