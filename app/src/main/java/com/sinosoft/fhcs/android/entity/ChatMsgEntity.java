package com.sinosoft.fhcs.android.entity;

import android.net.Uri;

/**
 * 一个消息的JavaBean
 *
 * @author way
 *
 */
public class ChatMsgEntity {
    private String name;//消息来自
    private String date;//消息日期
    private String message;//消息内容
    private Uri  picUri;//消息内容
    private int msgType;//消息类型 1 文字 2.图片 3.语音
    private boolean isComMeg = true;// 是否为收到的消息



    public Uri getPicUri() {
        return picUri;
    }

    public void setPicUri(Uri picUri) {
        this.picUri = picUri;
    }

    public int getMsgType() {
        return msgType;
    }

    public boolean isComMeg() {
        return isComMeg;
    }

    public void setComMeg(boolean isComMeg) {
        this.isComMeg = isComMeg;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public ChatMsgEntity() {
    }

    public ChatMsgEntity(String name, String date, String text, boolean isComMsg) {
        super();
        this.name = name;
        this.date = date;
        this.message = text;
        this.isComMeg = isComMsg;
    }

}  