/**
 * 
 */
package cn.wuxia.epay.epaylinks.bean;

import java.io.Serializable;

/**
 * @author songlin.li
 */
public class PaymentRefundBean extends PaymentBean implements Serializable {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private String total_amount; //交易总金额

    private String trans_type = "refund"; //交易类型

    private String out_refund_no; //退款的订单号

    private String refund_amount; //退款金额

    public String getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(String trans_type) {
        this.trans_type = trans_type;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

}
