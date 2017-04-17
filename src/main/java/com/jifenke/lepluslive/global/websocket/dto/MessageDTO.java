package com.jifenke.lepluslive.global.websocket.dto;

import java.util.Date;

/**
 * Created by xf on 17-1-11.
 * 语音消息相关数据
 */
public class MessageDTO {

    private String content;

    private Double price;

    private Date createTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
