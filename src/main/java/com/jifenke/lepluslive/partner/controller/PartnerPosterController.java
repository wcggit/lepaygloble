package com.jifenke.lepluslive.partner.controller;

import com.jifenke.lepluslive.global.util.ImageLoad;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.partner.domain.entities.Partner;
import com.jifenke.lepluslive.partner.domain.entities.PartnerInfo;
import com.jifenke.lepluslive.partner.domain.entities.Poster;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by xf on 2016/10/14. 海报生成 Controller
 */
@Controller
@RequestMapping("/poster")
public class PartnerPosterController {

    @Inject
    private PartnerService partnerService;

    @RequestMapping(value="/partner/downloadPage/{sid}",method= RequestMethod.GET)
    public String showPage(@PathVariable String sid,Model model) {
        model.addAttribute("sid",sid);
        return "hbDownload/hbDownload";
    }

    @RequestMapping(value = "/partner/downloadPoster")
    public void downLoadPoster(HttpServletResponse response,
                               @RequestParam(required = true) String sid) {
        Partner partner = partnerService.findByPartnerSid(sid);
        PartnerInfo partnerInfo = partnerService.findPartnerInfoByPartnerSid(sid);
        String headImageUrl = null;                                        // 头像url
        WeiXinUser weiXinUser = partner.getWeiXinUser();
        if (weiXinUser != null && weiXinUser.getHeadImageUrl() != null) {
            headImageUrl = weiXinUser.getHeadImageUrl();
        } else {
            headImageUrl = Poster.DEFAULT_HEADIMAGE_URL;
        }
        String partnerName = partner.getPartnerName();                       // 用户名
        String backgroundPictureUrl = Poster.POSTER_BACKIMAGE_URL;           // 背景url
        String
            qrCodeUrl = partnerInfo.getHbQrCodeUrl();                        // 海报二维码
        response.setContentType("application/x-msdownload;");
        response.setHeader("Content-disposition", "attachment; filename=image.png");
        response.setCharacterEncoding("UTF-8");
        OutputStream outputSream = null;
        try {
            outputSream = response.getOutputStream();
            InputStream back = ImageLoad.returnStream(backgroundPictureUrl); //  背景图片
            BufferedImage backImage = ImageIO.read(back);
            Graphics2D g = backImage.createGraphics();
            InputStream qrCode = ImageLoad.returnStream(qrCodeUrl);          //  二维码
            BufferedImage qrCodeImage = ImageIO.read(qrCode);
            InputStream headImageIns = ImageLoad.returnStream(headImageUrl); //  头像
            BufferedImage headImage = ImageIO.read(headImageIns);
            // 切图:　将头像且为圆形
            BufferedImage
                cutHeadImage =
                new BufferedImage(headImage.getWidth(), headImage.getHeight(),
                                  BufferedImage.TYPE_INT_ARGB);
            int minLength = Math.min(cutHeadImage.getWidth(), cutHeadImage.getHeight());
            //设置头像(椭圆形)的坐标和长宽
            Ellipse2D.Double
                shape =
                new Ellipse2D.Double((cutHeadImage.getWidth() - minLength) / 2,
                                     (cutHeadImage.getHeight() - minLength) / 2,
                                     minLength, minLength);
            Graphics2D g2 = cutHeadImage.createGraphics();
            cutHeadImage = g2.getDeviceConfiguration().createCompatibleImage(headImage.getWidth(),
                                                                             headImage.getHeight(),
                                                                             Transparency.TRANSLUCENT);
            g2 = cutHeadImage.createGraphics();
            g2.setComposite(AlphaComposite.Clear);
            g2.fill(new Rectangle(cutHeadImage.getWidth(), cutHeadImage.getHeight()));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
            g2.setClip(shape);
            // 使用 setRenderingHint 设置抗锯齿
            g2.drawImage(headImage, 0, 0, null);
            g2.dispose();
            // 开始绘制二维码图片
            int qrWidth = 220, qrHeight = 220;                  // 二维码宽高
            int x1 = new Long(Math.round((backImage.getWidth() - qrWidth) * 0.5)).intValue();
            int y1 = new Long(Math.round((backImage.getHeight() - qrHeight) * 0.79)).intValue();
            g.drawImage(qrCodeImage, x1, y1, qrWidth, qrHeight, null);
            g.drawRoundRect(x1, y1, qrWidth, qrHeight, 20, 20);
            g.setStroke(new BasicStroke(1.0f));
            g.setColor(Color.white);
            g.drawRect(x1, y1, qrWidth, qrHeight);
            // 开始绘制头像图片
            int hdWidth = 115, hdHeight = 115;
            int x2 = new Long(Math.round((backImage.getWidth() - hdWidth) * 0.55)).intValue();
            int y2 = new Long(Math.round(hdHeight * 0.75)).intValue();
            g.drawImage(cutHeadImage, x2, y2, hdWidth, hdHeight, null);
            // 开始绘制文字
            int x3 = new Long(Math.round((backImage.getWidth() - hdWidth) * 0.85)).intValue();
            int y3 = new Long(Math.round(hdHeight * 1.3)).intValue();
            g.setColor(Color.BLACK);
            g.setBackground(Color.black);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(new Font("微软雅黑", Font.BOLD, 27)); //字体、字型、字号
            g.drawString(partnerName, x3, y3); //画文字
            g.dispose();
            int len = 0;
            InputStream is = null;
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            ImageIO.write(backImage, "png", ImageIO.createImageOutputStream(bs));
            is = new ByteArrayInputStream(bs.toByteArray());
            while ((len = is.read(buf, 0, 1024)) != -1) {
                outputSream.write(buf, 0, len);
            }
            outputSream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
