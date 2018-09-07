package cn.wuxia.epay.alipay.config;

import java.util.Properties;

import cn.wuxia.common.util.PropertiesUtils;

/* *
 * 类名：AlipayConfig功能：基础配置类详细：设置帐户有关信息及返回路径版本：3.3日期：2012-08-10说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。提示：如何获取安全校验码和合作身份者ID
 * 1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 * 2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 * 3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？解决方法：
 * 1、检查浏览器配置，不让浏览器做弹框屏蔽设置2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
    private static Properties properties = PropertiesUtils.loadProperties("classpath:alipay.config.properties");

    // 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
    public static String partner = properties.getProperty("parner");

    /**
     * 支付宝分配给开发者的应用ID
     */
    public static String appid = properties.getProperty("app_id");
    // 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
    public static String seller_id = partner;

    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = properties.getProperty("app_private_key");

    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://商户网关地址/alipay.trade.wap.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://商户网关地址/alipay.trade.wap.pay-JAVA-UTF-8/return_url.jsp";

    // 请求网关地址
    public static String URL = "https://openapi.alipay.com/gateway.do";

    // 编码
    public static String CHARSET = "UTF-8";

    // 返回格式
    public static String FORMAT = "json";

    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = properties.getProperty("app_public_key");

    // 日志记录目录
    public static String log_path = "/log";

    // RSA2
    public static String SIGNTYPE = "RSA2";

    // 支付类型 ，无需修改
    public static String payment_type = "1";

    // 调用的接口名，无需修改
    public static String service = "create_direct_pay_by_user";
    //↓↓↓↓↓↓↓↓↓↓ 请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 防钓鱼时间戳  若要使用请调用类文件submit中的query_timestamp函数
    public static String anti_phishing_key = "";

    // 客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1
    public static String exter_invoke_ip = "";

    //↑↑↑↑↑↑↑↑↑↑请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    static {
        SIGNTYPE = properties.getProperty("sign_type");
    }
}
