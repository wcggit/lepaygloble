package com.jifenke.lepluslive.merchant.controller.dto;

/**
 * Created by xf on 17-5-8.
 */
public class PwdDto {
    private String oldPwd;      // 原密码
    private String newPwd;      // 新密码
    private String userSid;

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getUserSid() {
        return userSid;
    }

    public void setUserSid(String userSid) {
        this.userSid = userSid;
    }
}
