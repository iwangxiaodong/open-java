package com.openle.p.core;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author xiaodong
 */
public class TransferObject implements Serializable {

    private static final long serialVersionUID = 1L;

    //  行为
    private String action;

    //  子行为
    private String subAction;

    //  负载
    private byte[] payload;

    //  字符负载
    private String textPayload;

    // 实例即初始化，避免AIDL空指针
    private Integer version = 0;

    // 为空则广播所有，格式 192.168.1.23,1@1@1@1...
    private List<String> toAddresses;
    private List<String> fromAddresses;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSubAction() {
        return subAction;
    }

    public void setSubAction(String subAction) {
        this.subAction = subAction;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public String getTextPayload() {
        return textPayload;
    }

    public void setTextPayload(String textPayload) {
        this.textPayload = textPayload;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<String> getToAddresses() {
        return toAddresses;
    }

    public void setToAddresses(List<String> toAddresses) {
        this.toAddresses = toAddresses;
    }

    public List<String> getFromAddresses() {
        return fromAddresses;
    }

    public void setFromAddresses(List<String> fromAddresses) {
        this.fromAddresses = fromAddresses;
    }
}
