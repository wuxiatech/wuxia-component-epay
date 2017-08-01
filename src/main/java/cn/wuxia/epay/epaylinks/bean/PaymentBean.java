/**
 * 
 */
package cn.wuxia.epay.epaylinks.bean;

import java.io.Serializable;

/**
 * @author songlin.li
 */
public class PaymentBean implements Serializable {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -8924540621882202121L;

    private String partner; //商户号

    private String out_trade_no; //商家订单号

    private String total_fee; //商品金额,以元为单位

    private String return_url; //交易完成后跳转的URL

    private String notify_url; //接收后台通知的URL

    private String currency_type; //货币种类

    private String order_create_ip; //创建订单的客户端IP（消费者电脑公网IP，用于防钓鱼支付）

    private String sign_type; //签名算法（暂时只支持SHA256）

    private String sign;

    //可选参数
    private String pay_id; //直连银行参数，例子是直接转跳到招商银行时的参数

    private String base64_memo; //订单备注的BASE64编码

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

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public String getOrder_create_ip() {
        return order_create_ip;
    }

    public void setOrder_create_ip(String order_create_ip) {
        this.order_create_ip = order_create_ip;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    public String getBase64_memo() {
        return base64_memo;
    }

    public void setBase64_memo(String base64_memo) {
        this.base64_memo = base64_memo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
