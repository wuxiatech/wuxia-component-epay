/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package cn.wuxia.component.epay.alipay.f2fpay;

import java.text.SimpleDateFormat;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.google.common.collect.Maps;

import cn.wuxia.common.util.JsonUtil;
import cn.wuxia.component.epay.alipay.factory.AlipayAPIClientFactory;

public class ToAlipayQrTradePay {
    protected final static Logger logger = LoggerFactory.getLogger("epay");

    /**
     * 条码下单支付
     * @param out_trade_no
     * @param auth_code
     * @author jinlong.rhj
     * @date 2015年4月28日
     * @version 1.0
     * @return 
     */
    public static AlipayTradePrecreateResponse qrPay(String out_trade_no, String total_amount, String subject, String nodify_url,
            String extend_params) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_expire = sdf.format(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        SortedMap<String, String> params = Maps.newTreeMap();
        params.put("out_trade_no", out_trade_no);
        params.put("total_amount", total_amount);
        //params.put("discountable_amount", "0.00");
        params.put("subject", subject);
        //params.put("body", "");
        //params.put("extend_params", "");
        params.put("time_expire", time_expire);
        System.out.println(JsonUtil.toJson(params));

        AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();

        // 使用SDK，构建群发请求模型
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizContent(JsonUtil.toJson(params));
        request.setNotifyUrl(nodify_url);
        AlipayTradePrecreateResponse response = null;
        try {

            // 使用SDK，调用交易下单接口
            response = alipayClient.execute(request);

            System.out.println(response.getBody());
            System.out.println(response.isSuccess());
            System.out.println(response.getMsg());
            // 这里只是简单的打印，请开发者根据实际情况自行进行处理
            if (null != response && response.isSuccess()) {
                if (response.getCode().equals("10000")) {
                    System.out.println("商户订单号：" + response.getOutTradeNo());
                    System.out.println("二维码值：" + response.getQrCode());//商户将此二维码值生成二维码，然后展示给用户，用户用支付宝手机钱包扫码完成支付
                    //二维码的生成，网上有许多开源方法，可以参看：http://blog.csdn.net/feiyu84/article/details/9089497

                } else {

                    //打印错误码
                    System.out.println("错误码：" + response.getSubCode());
                    System.out.println("错误描述：" + response.getSubMsg());
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            logger.error("", e);
        }

        return response;
    }

}
