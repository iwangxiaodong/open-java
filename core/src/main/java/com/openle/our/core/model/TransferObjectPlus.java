package com.openle.our.core.model;

/**
 *
 * @author xiaodong
 */
public class TransferObjectPlus extends TransferObject {

    // 对象负载 - AIDL似乎不支持
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
