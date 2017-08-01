/*
* Created on :Jan 9, 2015
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 www.ibmall.cn All right reserved.
*/
package cn.wuxia.epay.epaylinks;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.repo.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wuxia.common.util.NumberUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.util.reflection.BeanUtil;
import cn.wuxia.common.web.httpclient.HttpClientException;
import cn.wuxia.common.web.httpclient.HttpClientRequest;
import cn.wuxia.common.web.httpclient.HttpClientResponse;
import cn.wuxia.common.web.httpclient.HttpClientUtil;
import cn.wuxia.epay.EpayException;
import cn.wuxia.epay.EpayService;
import cn.wuxia.epay.bean.EpayBean;
import cn.wuxia.epay.bean.EpayRefundBean;
import cn.wuxia.epay.enums.EpayPlatform;
import cn.wuxia.epay.epaylinks.bean.PaymentRefundBean;
import cn.wuxia.epay.epaylinks.bean.PaymentRefundResultBean;
import cn.wuxia.epay.epaylinks.util.ToolUtil;
import cn.wuxia.epay.epaylinks.util.XMLUtil;
import cn.wuxia.epay.trade.model.PaymentTrade;
import cn.wuxia.epay.trade.service.PaymentTradeService;

@Service
@Transactional
public class EpaylinksService {
    protected final Logger logger = LoggerFactory.getLogger("epay");

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    private EpayService epayService;

    @Autowired
    private PaymentTradeService paymentTradeService;

    /**
     * 退款操作
     * @author songlin
     * @param paymentTradeNo
     * @param amount
     * @param remark
     * @param uid
     * @return
     * @throws EpayException
     */
    public boolean refund(EpayRefundBean refundBean, PaymentTrade pt) throws EpayException {

        String amount = ToolUtil.encodeTransAmount(refundBean.getRefundAmount());
        String total_fee = ToolUtil.encodeTransAmount("" + pt.getAmount().doubleValue());

        //当退款的金额大于订单的金额时
        if (NumberUtil.compare(NumberUtil.toDouble(amount), pt.getAmount().doubleValue()) == 1) {
            throw new EpayException("申请退款金额不能大于：" + NumberUtil.formatFinancing(pt.getAmount().doubleValue()));
        }
        BigDecimal totalRefundAmount = paymentTradeService.findRefundAmount(pt.getUserId(), pt.getSerialNumber());
        totalRefundAmount = totalRefundAmount == null ? new BigDecimal(0) : totalRefundAmount;
        // 当退款金额大于剩余的可退款金额时
        if (NumberUtil.compare(NumberUtil.toDouble(amount), pt.getAmount().subtract(totalRefundAmount)) > 0) {
            throw new EpayException("申请退款金额不能大于：" + NumberUtil.formatFinancing(pt.getAmount().subtract(totalRefundAmount).doubleValue()));
        }
        //当第一次不全额退款时
        if (NumberUtil.compare(totalRefundAmount, 0) == 0 && NumberUtil.compare(NumberUtil.toDouble(amount), pt.getAmount()) < 0) {

        }
        //退还剩余金额
        else if (NumberUtil.compare(totalRefundAmount, 0) > 0) {

        }
        //货币代码，人民币：RMB；港币：HKD；美元：USD （非人民币收单业务，需要与业务人员联系开通）
        String currency_type = Config.Currency.RMB.toString();
        //创建订单的客户端IP（消费者电脑公网IP）
        String order_create_ip = "183.6.128.162";
        //        String order_create_ip = super.getVisitorIp(); //创建订单的客户端IP（消费者电脑公网IP，用于防钓鱼支付）
        //商户密钥
        String key = Config.KEY; //商户密钥
        //签名类型
        String sign_type = Config.SignType.SHA256.toString();
        //直连银行参数（不停留易票联支付网关页面，直接转跳到银行支付页面）
        //String pay_id = "zhaohang";  //直连招商银行参数值
        String pay_id = "";
        //订单备注，该信息使用64位编码提交服务器，并将在支付完成后随支付结果原样返回
        String base64_memo = StringUtil.isNotBlank(refundBean.getRemark()) ? Base64.encodeToString(refundBean.getRemark().getBytes(), false) : "";
        //退款请求对象
        EpaylinksSubmit epaySubmit = new EpaylinksSubmit(request);
        //设置商户密钥
        epaySubmit.setKey(key);
        //退款
        epaySubmit.setGatewayUrl(Config.QUERY_REFUND_URL);

        //设置支付请求参数
        PaymentRefundBean parms = new PaymentRefundBean();
        parms.setPartner(Config.PARENER); //商户号
        parms.setOut_trade_no(pt.getSerialNumber());//商家订单号
        parms.setOut_refund_no(pt.getSerialNumber());
        parms.setTotal_amount(total_fee);//商品金额,以元为单位
        parms.setRefund_amount(amount); //退款金额
        parms.setSign_type(sign_type);//签名算法（暂时只支持SHA256）
        //可选参数
        epaySubmit.setPaymentBean(parms);
        //获取提交到网关的请求地址
        String requestUrl = epaySubmit.getRequestURL();

        //获取调试信息
        String debugMsg = epaySubmit.getDebugMsg();
        logger.debug("debugMsg:" + debugMsg);

        HttpClientRequest req = new HttpClientRequest();
        req.setUrl(requestUrl);
        logger.debug("requestUrl:" + requestUrl);
        HttpClientResponse res;
        try {
            res = HttpClientUtil.get(req);
        } catch (HttpClientException e1) {
            throw new EpayException(e1);
        }
        String callbackString;
        EpayBean paymentBean = new EpayBean();
        try {
            callbackString = new String(res.getByteResult(), "GBK");
            Map m = XMLUtil.doXMLParse(callbackString);
            PaymentRefundResultBean refundResult = (PaymentRefundResultBean) BeanUtil.mapToBean(m, PaymentRefundResultBean.class);
            if (StringUtil.equals("00", refundResult.getResp_code()) && StringUtil.equals("1", refundResult.getRefund_result())) {
                paymentBean.setSuccessTrade(true);
                paymentBean.setRemark("【" + EpayPlatform.EPAYLINKS.getPlatformName() + "】" + refundBean.getRemark());
            } else {
                paymentBean.setSuccessTrade(false);
                paymentBean.setRemark("【" + EpayPlatform.EPAYLINKS.getPlatformName() + "】" + refundResult.getResp_desc());
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new EpayException(e);
        }

        paymentBean.setAmount(new BigDecimal(amount));
        paymentBean.setSerialNumber(pt.getSerialNumber());
        paymentBean.setUserId(refundBean.getUserId());
        paymentBean.setPaymentPlatform(EpayPlatform.EPAYLINKS);
        epayService.afterRefund(paymentBean);
        return paymentBean.isSuccessTrade();
    }

}
