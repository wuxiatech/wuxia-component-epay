/**
 * 
 */
package cn.wuxia.epay.epaylinks.bean;

import java.io.Serializable;

/**
 * @author songlin.li
 */
public class PaymentResultBean implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -8924540621882202121L;

    private String partner; //商户

    private String out_trade_no; //订单号

    private String amount; //支付金额

    private String base64_memo; //备注

    private String sign_type; //签名类型

    private String sign; //返回的签名

    private String pay_no; //易票联的支付订单号

    private String pay_result; //支付结果（1表示成功,0表示未支付，2表示支付失败））

    private String pay_time; //支付时间

    private String sett_date; //清算日期

    private String sett_time; //清算时间

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBase64_memo() {
        return base64_memo;
    }

    public void setBase64_memo(String base64_memo) {
        this.base64_memo = base64_memo;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPay_no() {
        return pay_no;
    }

    public void setPay_no(String pay_no) {
        this.pay_no = pay_no;
    }

    public String getPay_result() {
        return pay_result;
    }

    public void setPay_result(String pay_result) {
        this.pay_result = pay_result;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getSett_date() {
        return sett_date;
    }

    public void setSett_date(String sett_date) {
        this.sett_date = sett_date;
    }

    public String getSett_time() {
        return sett_time;
    }

    public void setSett_time(String sett_time) {
        this.sett_time = sett_time;
    }

}
