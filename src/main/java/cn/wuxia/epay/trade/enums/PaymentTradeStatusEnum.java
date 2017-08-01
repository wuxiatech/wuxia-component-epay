package cn.wuxia.epay.trade.enums;

import cn.wuxia.common.util.NumberUtil;

public enum PaymentTradeStatusEnum {
    // 代付款
    DAIFUKUAN((short) 1, "代付款"),
    // 成功付款
    CHENGGONG((short) 10, "成功付款"),
    //待退款
    DAITUIKUAN((short) 20, "待退款"),
    //退款成功
    TUIKUANZHONG((short) 30, "退款中"),
    //退款成功
    TUIKUANCHENGGONG((short) 40, "退款成功"),
    // 交易失败
    SHIBAI((short) 50, "交易失败");
    private final short status;

    private final String remark;

    private PaymentTradeStatusEnum(short status, String remark) {
        this.status = status;
        this.remark = remark;
    }

    public short getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }

    public static PaymentTradeStatusEnum get(short status) {
        for (PaymentTradeStatusEnum e : PaymentTradeStatusEnum.values()) {
            if (NumberUtil.equals(e.getStatus(), status)) {
                return e;
            }
        }
        return SHIBAI;
    }
}
