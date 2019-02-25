/**
 * 
 */
package cn.wuxia.component.epay.chinapay.bean;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Jackie.Gao
 */
public class RefundBean implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3614562980865515495L;

    private String merId;

    private String ordId;

    private String refundAmount;

    private String transDate;

    private String transType;

    private String version;

    private String returnUrl;

    private String priv1;

    private String chkValue;

    public RefundBean() {

    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getOrdId() {
        return ordId;
    }

    public void setOrdId(String ordId) {
        this.ordId = ordId;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getPriv1() {
        return priv1;
    }

    public void setPriv1(String priv1) {
        this.priv1 = priv1;
    }

    public String getChkValue() {
        return chkValue;
    }

    public void setChkValue(String chkValue) {
        this.chkValue = chkValue;
    }

    public String getQueryString() {
        return new StringBuffer("MerID=").append(merId).append("&OrderId=").append(ordId).append("&RefundAmount=").append(refundAmount)
                .append("&TransDate=").append(transDate).append("&TransType=").append(transType).append("&Version=").append(version)
                .append("&ReturnURL=").append(returnUrl).append("&Priv1=").append(priv1).append("&ChkValue=").append(chkValue).toString();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
