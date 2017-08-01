
/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package cn.wuxia.epay.alipay.constants;

import java.util.Properties;

import cn.wuxia.common.util.PropertiesUtils;

/**
 * 支付宝服务窗环境常量（demo中常量只是参考，需要修改成自己的常量值）
 * 
 * @author taixu.zqq
 * @version $Id: AlipayServiceConstants.java, v 0.1 2014年7月24日 下午4:33:49 taixu.zqq Exp $
 */
public class AlipayServiceEnvConstants {

    /**支付宝公钥-从支付宝服务窗获取*/
    public static final String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

    /**签名编码-视支付宝服务窗要求*/
    public static final String SIGN_CHARSET = "UTF-8";

    /**字符编码-传递给支付宝的数据编码*/
    public static final String CHARSET = "UTF-8";

    /**签名类型-视支付宝服务窗要求*/
    public static String SIGN_TYPE = "";

    public static String PARTNER = "";

    /** 服务窗appId  */
    //TODO !!!! 注：该appId必须设为开发者自己的服务窗id  这里只是个测试id
    public static String APP_ID = "";

    //开发者请使用openssl生成的密钥替换此处  请看文档：https://fuwu.alipay.com/platform/doc.htm#2-1接入指南
    //TODO !!!! 注：该私钥为测试账号私钥  开发者必须设置自己的私钥 , 否则会存在安全隐患 
    public static String PRIVATE_KEY = "";

    //TODO !!!! 注：该公钥为测试账号公钥  开发者必须设置自己的公钥 ,否则会存在安全隐患
    public static String PUBLIC_KEY = "";

    /**支付宝网关*/
    public static String ALIPAY_GATEWAY = "";

    /**授权访问令牌的授权类型*/
    public static final String GRANT_TYPE = "authorization_code";

    static {
        Properties properties = PropertiesUtils.loadProperties("classpath:alipay.config.properties");
        PARTNER = properties.getProperty("partner");
        PRIVATE_KEY = properties.getProperty("app_private_key");
        APP_ID = properties.getProperty("app_id");
        PUBLIC_KEY = properties.getProperty("app_public_key");
        ALIPAY_GATEWAY = properties.getProperty("alipay_gateway");
        SIGN_TYPE = properties.getProperty("sign_type");
    }
}
