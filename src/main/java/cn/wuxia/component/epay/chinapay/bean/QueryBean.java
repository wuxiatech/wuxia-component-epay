/**
 * 
 */
package cn.wuxia.component.epay.chinapay.bean;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author songlin.li
 */
public class QueryBean implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 8481583492460582570L;

    private String merId;

    private String ordId;

    private String transDate;

    private String transType;

    private String version;

    private String resv;

    private String chkValue;

    public QueryBean() {

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

    public String getResv() {
        return resv;
    }

    public void setResv(String resv) {
        this.resv = resv;
    }

    public String getChkValue() {
        return chkValue;
    }

    public void setChkValue(String chkValue) {
        this.chkValue = chkValue;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getQueryString() {
        return new StringBuffer("MerId=").append(merId).append("&OrdId=").append(ordId).append("&TransDate=").append(transDate).append("&TransType=")
                .append(transType).append("&Version=").append(version).append("&Resv=").append(resv).append("&ChkValue=").append(chkValue).toString();
    }
}
