package cn.wuxia.epay.epaylinks;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wuxia.common.util.ServletUtils;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.util.reflection.BeanUtil;
import cn.wuxia.epay.EpayException;
import cn.wuxia.epay.epaylinks.bean.PaymentResultBean;
import cn.wuxia.epay.epaylinks.util.SHA256Util;
import cn.wuxia.epay.epaylinks.util.ToolUtil;

/**
 * 请求应答协助类
 * @author fenghanhao
 *
 */
public class EpaylinksNotify {
    protected final Logger logger = LoggerFactory.getLogger("epay");

    /** 商户密钥 */
    private String key;

    /** 应答的参数 */
    private SortedMap<String, String> paramMap;

    /** 调试信息 */
    private String debugMsg;

    private HttpServletRequest request;

    private String urlEncoding;

    private PaymentResultBean paymentResultBean;

    /**
     * 构造函数
     * 
     * @param request
     * @param response
     */
    public EpaylinksNotify(HttpServletRequest request) throws EpayException {
        this.request = request;

        this.debugMsg = "";

        this.urlEncoding = "";

        paramMap = new TreeMap<>();
        Map<String, Object> m = ServletUtils.getParametersMap(request);
        try {
            this.paymentResultBean = (PaymentResultBean) BeanUtil.mapToBean(m, PaymentResultBean.class);
        } catch (Exception e) {
            throw new EpayException("转换出错", e);
        }
        for (Map.Entry<String, Object> set : m.entrySet()) {
            if (StringUtil.isNotBlank(set.getValue())) {
                paramMap.put(set.getKey(), set.getValue().toString());
            }
        }
    }

    /**
    *获取密钥
    */
    public String getKey() {
        return key;
    }

    /**
    *设置密钥
    */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 
     * @author songlin
     * @return
     */
    public boolean payStatue() {
        if ("1".equals(paymentResultBean.getPay_result())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 使用SHA256算法验证签名。规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     * @return boolean
     * @throws IntrospectionException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public boolean verifySign() throws EpayException {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (!"sign".equals(k) && StringUtil.isNotBlank(v)) {
                sb.append(k + "=" + v + "&");
            }
        }

        sb.append("key=" + this.getKey());

        //算出摘要
        String enc = ToolUtil.getCharacterEncoding(this.request);
        String sign = SHA256Util.SHA256Encode(sb.toString(), enc).toLowerCase();

        String tenpaySign = this.paymentResultBean.getSign().toLowerCase();

        //debug信息
        this.setDebugMsg(sb.toString() + " => sign:" + sign + " epaylinksSign:" + tenpaySign);

        return tenpaySign.equals(sign);
    }

    /**
     * 返回处理结果给支付网关服务器。
     * @param msg: Success or fail。
     * @throws IOException 
     */
    //    public void responseToGateway(String msg) throws IOException {
    //        String strHtml = msg;
    //        PrintWriter out = this.getHttpServletResponse().getWriter();
    //        out.println(strHtml);
    //        out.flush();
    //        out.close();
    //
    //    }

    /**
     * 获取uri编码
     * @return String
     */
    public String getUrlEncoding() {
        return urlEncoding;
    }

    /**
    *获取调试信息
    */
    public String getDebugMsg() {
        return debugMsg;
    }

    /**
    *设置调试信息
    */
    protected void setDebugMsg(String debugInfo) {
        this.debugMsg = debugInfo;
    }

    protected HttpServletRequest getHttpServletRequest() {
        return this.request;
    }

    public PaymentResultBean getPaymentResultBean() {
        return this.paymentResultBean;
    }

}
