/**
 * 
 */
package cn.wuxia.epay.epaylinks.bean;

import java.io.Serializable;

/**
 * @author songlin.li
 */
public class PaymentRefundResultBean implements Serializable {
    /**
     * Comment for <code>serialVersionUIDprivate String /code>
     */
    private static final long serialVersionUID = -8924540621882202121L;

    private String resp_code;

    private String resp_desc;

    private String partner;

    private String out_trade_no;

    private String out_refund_no;

    private String refund_id;

    private String refund_amount;

    private String refund_result;

    private String refund_time;

    private String sign_type;

    private String sign;

    public String getResp_code() {
        return resp_code;
    }

    public void setResp_code(String resp_code) {
        this.resp_code = resp_code;
    }

    public String getResp_desc() {
        return resp_desc;
    }

    public void setResp_desc(String resp_desc) {
        this.resp_desc = resp_desc;
    }

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

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getRefund_result() {
        return refund_result;
    }

    public void setRefund_result(String refund_result) {
        this.refund_result = refund_result;
    }

    public String getRefund_time() {
        return refund_time;
    }

    public void setRefund_time(String refund_time) {
        this.refund_time = refund_time;
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

}
