/*
* Created on :Jan 5, 2015
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 www.ibmall.cn All right reserved.
*/
package cn.wuxia.epay.epaylinks;

import java.util.Properties;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import cn.wuxia.common.util.PropertiesUtils;

public class Config {

    public static final Properties properties = PropertiesUtils.loadProperties("classpath:epaylinks.config.properties");

    private static final PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

    /**
     * 商户ID
     */
    public static final String PARENER = properties.getProperty("parener");

    /**
     * 商家密钥（key）
     */
    public static final String KEY = properties.getProperty("key");

    /**
     * 支付接口URL
     */
    public static final String PAY_URL = properties.getProperty("pay.url");

    /**
     * 订单查询/退款接口URL
     */
    public static final String QUERY_REFUND_URL = properties.getProperty("queryrefund.url");

    /**
     * 易票联商户后台管理系统
     */
    public static final String SYSTEM_URL = properties.getProperty("system.url");

    /**
     * 易票联签名类型
     */

    public enum SignType {
        SHA256
    }

    /**
     * 
     * 交易货币
     * @author songlin
     * @ Version : V<Ver.No> <Jan 9, 2015>
     */
    public enum Currency {
        RMB, HKD, USD
    }

    /**
     * 
     * 支付银行
     * @author songlin
     * @ Version : V<Ver.No> <Jan 9, 2015>
     */
    public enum Payment {
        gonghang, //    中国工商银行
        zhonghang, //   中国银行
        jiaohang, //    中国交通银行
        zhaohang, //    招商银行
        guangda, // 中国光大银行
        nonghang, //    中国农业银行
        nonghangb2b, // 农行企业网银
        zhongxin, //    中信银行
        jianhang, //    建设银行
        jianhangb2b, // 建行企业网银
        gonghangb2b, // 工行企业网银
        zhaohangb2b, // 招行企业网银
        shenfa, //  深圳发展银行(现已并入平安银行)
        minsheng, //    民生银行
        guangfa, // 广东发展银行
        xingye, //  兴业银行
        pufa, //    上海浦东发展银行
        alipay, //  支付宝
        tenpay, //  财付通
        foreignpay;//  外币支付（Visa/MasterCard/JCB）

    }

}
