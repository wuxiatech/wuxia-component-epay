package cn.wuxia.epay.epaylinks;

import java.beans.IntrospectionException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.util.reflection.BeanUtil;
import cn.wuxia.epay.EpayException;
import cn.wuxia.epay.epaylinks.bean.PaymentBean;
import cn.wuxia.epay.epaylinks.util.SHA256Util;
import cn.wuxia.epay.epaylinks.util.ToolUtil;

/**
 * 提交请求协助类
 * @author fenghanhao
 * 
 */
public class EpaylinksSubmit {
    protected final Logger logger = LoggerFactory.getLogger("epay");

    /** 网关url地址 */
    private String gatewayUrl;

    /** 商户密钥 */
    private String key;

    /** 调试信息 */
    private String debugMsg;

    private HttpServletRequest request;

    /** 请求的参数 */
    private PaymentBean paymentBean;

    /**
     * 构造函数
     * @param request
     * @param response
     */
    public EpaylinksSubmit(HttpServletRequest request) {
        this.request = request;
        this.debugMsg = "";
    }

    /**
    *获取入口地址,不包含参数值
    */
    public String getGatewayUrl() {
        return gatewayUrl;
    }

    /**
    *设置入口地址,不包含参数值
    */
    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    /**
    *获取商户密钥
    */
    public String getKey() {
        return key;
    }

    /**
    *设置商户密钥
    */
    public void setKey(String key) {
        this.key = key;
    }

    /**
    *获取调试信息
    */
    public String getDebugMsg() {
        return debugMsg;
    }

    /**
     * 获取请求的URL地址，此地址包含参数和签名串
     * @return String
     * @throws UnsupportedEncodingException 
     * @throws IntrospectionException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public String getRequestURL() throws EpayException {
        try {
            this.buildRequestSign();

            StringBuffer sb = new StringBuffer();
            String enc = ToolUtil.getCharacterEncoding(this.request);
            Map<String, Object> es = BeanUtil.beanToMap(this.paymentBean);
            TreeMap<String, Object> treemap = new TreeMap<>(es);
            for (Map.Entry<String, Object> entry : treemap.entrySet()) {
                String k = (String) entry.getKey();
                String v = (String) entry.getValue();
                if (StringUtil.isNotBlank(v))
                    sb.append(k + "=" + URLEncoder.encode(v, enc) + "&");
            }

            //去掉最后一个&
            String reqPars = sb.substring(0, sb.lastIndexOf("&"));

            return this.getGatewayUrl() + "?" + reqPars;
        } catch (UnsupportedEncodingException | IllegalAccessException | InvocationTargetException | IntrospectionException e) {
            logger.error("", e);
            throw new EpayException(e.getMessage());
        }
    }

    /**
     * 使用SHA256算法生成签名结果,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     * @throws IntrospectionException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public void buildRequestSign() throws EpayException {
        try {
            StringBuffer sb = new StringBuffer();
            Map<String, Object> es = BeanUtil.beanToMap(this.paymentBean);
            TreeMap<String, Object> treemap = new TreeMap<>(es);
            for (Map.Entry<String, Object> entry : treemap.entrySet()) {
                String k = (String) entry.getKey();
                String v = (String) entry.getValue();
                if (StringUtil.isNotBlank(v) && !"sign".equals(k) && !"key".equals(k)) {
                    sb.append(k + "=" + v + "&");
                }
            }
            sb.append("key=" + this.getKey());

            String enc = ToolUtil.getCharacterEncoding(this.request);
            String sign = SHA256Util.SHA256Encode(sb.toString(), enc).toLowerCase();

            this.paymentBean.setSign(sign);

            //调试信息
            this.setDebugMsg(sb.toString() + " => sign:" + sign);
        } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
            logger.error("", e);
            throw new EpayException(e.getMessage());
        }

    }

    /**
    *设置调试信息
    */
    protected void setDebugMsg(String debugMsg) {
        this.debugMsg = debugMsg;
    }

    protected HttpServletRequest getHttpServletRequest() {
        return this.request;
    }

    public void setPaymentBean(PaymentBean paymentBean) {
        this.paymentBean = paymentBean;
    }

}
