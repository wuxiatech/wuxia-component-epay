package cn.wuxia.epay.epaylinks.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jdom2.JDOMException;

import cn.wuxia.epay.epaylinks.util.SHA256Util;
import cn.wuxia.epay.epaylinks.util.XMLUtil;

/**
 * 后台应答类<br/>
 *
 */
public class ResponseHelper {

    /** 应答原始内容 */
    private String content;

    /** 应答的参数 */
    private SortedMap parameters;

    /** debug信息 */
    private String debugMsg;

    /** 密钥 */
    private String key;

    /** 字符集 */
    private String charset;

    public ResponseHelper() {
        this.content = "";
        this.parameters = new TreeMap();
        this.debugMsg = "";
        this.key = "";
        this.charset = "GBK";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) throws Exception {
        this.content = content;

        this.doParse();
    }

    /**
     * 获取参数值
     * @param parameter 参数名称
     * @return String 
     */
    public String getParameter(String parameter) {
        String s = (String) this.parameters.get(parameter);
        return (null == s) ? "" : s;
    }

    /**
     * 设置参数值
     * @param parameter 参数名称
     * @param parameterValue 参数值
     */
    public void setParameter(String parameter, String parameterValue) {
        String v = "";
        if (null != parameterValue) {
            v = parameterValue.trim();
        }
        this.parameters.put(parameter, v);
    }

    /**
     * 返回所有的参数
     * @return SortedMap
     */
    public SortedMap getAllParameters() {
        return this.parameters;
    }

    public String getDebugMsg() {
        return debugMsg;
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

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * 使用SHA256算法验证签名。规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     * @return boolean
     */
    public boolean verifySign() {
        StringBuffer sb = new StringBuffer();
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }

        sb.append("key=" + this.getKey());

        //算出摘要
        String sign = SHA256Util.SHA256Encode(sb.toString(), this.charset).toLowerCase();

        String epaylinksSign = this.getParameter("sign").toLowerCase();

        //调试信息
        this.setDebugMsg(sb.toString() + " => sign:" + sign + " epaylinksSign:" + epaylinksSign);

        return epaylinksSign.equals(sign);
    }

    protected void setDebugMsg(String debugMsg) {
        this.debugMsg = debugMsg;
    }

    /**
     * 解析XML内容
     */
    protected void doParse() throws JDOMException, IOException {
        String xmlContent = this.getContent();

        //解析xml,得到map
        Map m = XMLUtil.doXMLParse(xmlContent);

        //设置参数
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String k = (String) it.next();
            String v = (String) m.get(k);
            this.setParameter(k, v);
        }

    }

}
