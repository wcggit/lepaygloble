package com.jifenke.lepluslive.merchant.controller;

import com.jifenke.lepluslive.global.util.ImageLoad;
import com.jifenke.lepluslive.merchant.domain.entities.InvitationCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wanjun on 2016/12/21.
 */
@RestController
@RequestMapping(value = "/invitationCode")
public class MerchantInvitationCodeController {

    private static ReentrantLock lock = new ReentrantLock();

    @RequestMapping(value = "/downLoadInvitationCode")
    public void downLoadInvitationCode(HttpServletResponse response,
                                       @RequestParam(required = true) String qrCodeUrl) {

        CountDownLatch end = new CountDownLatch(3);
        String backgroundInvitationCodeUrl = InvitationCode.INVITATION_CODE_BACKIMAGE_URL;
        response.setContentType("application/x-msdownload;");
        response.setHeader("Content-disposition", "attachment; filename=image.png");
        response.setCharacterEncoding("UTF-8");
        OutputStream outputSream = null;
        try {
            outputSream = response.getOutputStream();
            InputStream back = ImageLoad.returnStream(backgroundInvitationCodeUrl);
            BufferedImage backImage = ImageIO.read(back);
            Graphics2D g = backImage.createGraphics();
//            new Thread(() -> {
            InputStream qrCode = ImageLoad.returnStream(qrCodeUrl);
            try {
                BufferedImage qrCodeImage = ImageIO.read(qrCode);
                int qrWidth = 770, qrHeight = 770;
                int
                    x1 =
                    new Long(Math.round((backImage.getWidth() - qrWidth) * 0.49)).intValue();
                int
                    y1 =
                    new Long(Math.round((backImage.getHeight() - qrHeight) * 0.59)).intValue();
//                    lock.lock();
                g.drawImage(qrCodeImage, x1, y1, qrWidth, qrHeight, null);
                g.drawRoundRect(x1, y1, qrWidth, qrHeight, 20, 20);
                g.setStroke(new BasicStroke(1.0f));
                g.setColor(Color.white);
                g.drawRect(x1, y1, qrWidth, qrHeight);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

//                    lock.unlock();
//                    end.countDown();
            }
//            }).start();
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
