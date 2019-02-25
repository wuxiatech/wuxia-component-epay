/*
* Created on :Jan 8, 2015
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 www.ibmall.cn All right reserved.
*/
package cn.wuxia.component.epay.bean;

import cn.wuxia.component.epay.trade.model.PaymentTrade;

public class EpayBean extends PaymentTrade {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private String orderNo;

    private boolean successTrade;

    private String clientIp;

    private Boolean needReceipt;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public boolean isSuccessTrade() {
        return successTrade;
    }

    public void setSuccessTrade(boolean successTrade) {
        this.successTrade = successTrade;
    }

    public Boolean getNeedReceipt() {
        return needReceipt;
    }

    public void setNeedReceipt(Boolean needReceipt) {
        this.needReceipt = needReceipt;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

}
