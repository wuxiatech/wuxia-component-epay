package cn.wuxia.component.epay.trade.enums;

public enum PaymentTradeTypeEnum {
	// 退款
	TUIKUAN((short) 1, "退款"),
	// 充值
	CHONGZHI((short) 10, "充值"),
	// 订单付款
	DINGDAN((short) 20, "订单付款");
	private final short type;

	private String remark;

	private PaymentTradeTypeEnum(short type, String remark) {
		this.type = type;
		this.remark = remark;
	}

	public short getType() {
		return type;
	}

	public String getRemark() {
		return remark;
	}

	public static PaymentTradeTypeEnum get(short type) {
		for (PaymentTradeTypeEnum e : PaymentTradeTypeEnum.values()) {
			if (e.getType() == type)
				return e;
		}
		return null;
	}
}
