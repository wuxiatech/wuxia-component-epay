/*
* Created on :Jan 10, 2015
* Author     :songlin
* 支付平台
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 www.ibmall.cn All right reserved.
*/
package cn.wuxia.epay.enums;

public enum EpayPlatform {

	CHINAPAY("中国银联在线"), EPAYLINKS("易票联支付"), WECHATPAY("微信支付"), ALIPAY("支付宝"), BAIFUBAO("百度钱包");
	private String platformName;

	private EpayPlatform(String platformName) {
		this.platformName = platformName;
	}

	public String getPlatformName() {
		return platformName;
	}

}
