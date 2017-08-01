/*
* Created on :Jan 8, 2015
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 www.ibmall.cn All right reserved.
*/
package cn.wuxia.epay.bean;

import java.io.Serializable;

import cn.wuxia.epay.enums.EpayPlatform;

public class EpayRefundBean implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private String serialNumber;

    private String paymentNumber;

    private String refundAmount;

    private Short status;

    private String remark;

    private String userId;

    private boolean successTrade;

    private String clientIp;

    private EpayPlatform paymentPlatform;

    public EpayRefundBean() {
    }

    public EpayRefundBean(String serialNumber, String remark) {
        this.serialNumber = serialNumber;
        this.remark = remark;
    }

    public boolean isSuccessTrade() {
        return successTrade;
    }

    public void setSuccessTrade(boolean successTrade) {
        this.successTrade = successTrade;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public EpayPlatform getPaymentPlatform() {
        return paymentPlatform;
    }

    public void setPaymentPlatform(EpayPlatform paymentPlatform) {
        this.paymentPlatform = paymentPlatform;
    }

}
