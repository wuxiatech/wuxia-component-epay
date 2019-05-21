/*
* Created on :2014年10月22日
* Author     :Cm
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 songlin.li All right reserved.
*/
package cn.wuxia.component.epay.chinapay.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.wuxia.component.epay.EpayException;
import cn.wuxia.component.epay.EpayService;
import cn.wuxia.component.epay.bean.EpayBean;
import cn.wuxia.component.epay.bean.EpayRefundBean;
import cn.wuxia.component.epay.chinapay.KeyPair;
import cn.wuxia.component.epay.chinapay.bean.QueryBean;
import cn.wuxia.component.epay.chinapay.bean.RefundBean;
import cn.wuxia.component.epay.chinapay.util.connection.CPHttpConnection;
import cn.wuxia.component.epay.chinapay.util.connection.Http;
import cn.wuxia.component.epay.chinapay.util.connection.HttpSSL;
import cn.wuxia.component.epay.enums.EpayPlatform;
import cn.wuxia.component.epay.trade.model.PaymentTrade;
import cn.wuxia.component.epay.trade.service.PaymentTradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chinapay.SecureLink;
import cn.wuxia.common.util.DateUtil;
import cn.wuxia.common.util.EncodeUtils;
import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.NumberUtil;
import cn.wuxia.common.util.ServletUtils;
import cn.wuxia.common.util.StringUtil;

/**
 * 
 * [ticket id]
 * 银联接口
 * @author wuwenhao
 * @ Version : V<Ver.No> <2014年10月22日>
 */
@Service
@Transactional
public class ChinaPayService {

    protected final Logger logger = LoggerFactory.getLogger("epay");

    @Autowired
    private PaymentTradeService paymentTradeService;

    private static final String STATUS_REFUND_SUCC = "3";

    private static final String STATUS_REFUND_FAIL = "8";

    @Autowired
    private EpayService epayService;

    /**
     * 银联退款接口
     * @author wuwenhao
     * @param paymentTradeNo 银联的NO
     * @param amount 支付用户
     * @param remark 备注
     * @return
     */
    public boolean refund(EpayRefundBean refundBean, PaymentTrade pt) throws EpayException {

        String transAmt = KeyPair.encodeTransAmount(refundBean.getRefundAmount());
        String remark = StringUtil.isBlank(refundBean.getRemark()) ? "申请退款" : refundBean.getRemark();
        // 退款订单数据准备
        String MerId = KeyPair.noCardMerId;
        String Version = KeyPair.version_2007;
        String OrderId = pt.getSerialNumber();
        String RefundAmount = transAmt;// 12
        String TransDate = DateUtil.dateToString(pt.getTransDate(), DateUtil.DateFormatter.FORMAT_YYYYMMDD);// 8
        //交易类型：0002为首次全额退款请求；0012为
        //        再次提交该笔全额退款; 0102为首次部分退款请求; 0112为再
        //        次提交该笔部分退款
        String TransType = "0002";// 4
        if (StringUtil.equals(RefundAmount, KeyPair.encodeTransAmount(pt.getAmount().toString()))) {

        }
        //当退款的金额大于订单的金额时
        if (NumberUtil.compare(NumberUtil.toDouble(refundBean.getRefundAmount()), pt.getAmount()) == 1) {
            throw new EpayException("申请退款金额不能大于：" + NumberUtil.formatFinancing(pt.getAmount().doubleValue()));
        }
        BigDecimal totalRefundAmount = paymentTradeService.findRefundAmount(refundBean.getUserId(), pt.getSerialNumber());
        totalRefundAmount = totalRefundAmount == null ? new BigDecimal(0) : totalRefundAmount;
        // 当退款金额大于剩余的可退款金额时
        if (NumberUtil.compare(NumberUtil.toDouble(refundBean.getRefundAmount()), pt.getAmount().subtract(totalRefundAmount)) > 0) {
            throw new EpayException("申请退款金额不能大于：" + NumberUtil.formatFinancing(pt.getAmount().subtract(totalRefundAmount).doubleValue()));
        }
        //当第一次不全额退款时
        if (NumberUtil.compare(totalRefundAmount, 0) == 0
                && NumberUtil.compare(NumberUtil.toDouble(refundBean.getRefundAmount()), pt.getAmount()) < 0) {
            TransType = "0102";
        }
        //退还剩余金额
        else if (NumberUtil.compare(totalRefundAmount, 0) > 0) {
            TransType = "0112";
        }
        String ReturnURL = "";//getRequestServerName() + "/chinapay/refundback?pt=";
        String Priv1 = EncodeUtils.base64UrlSafeEncode(remark.getBytes());
        if (StringUtil.length(Priv1) > 40)
            throw new EpayException("Remark过长：" + remark + "-" + Priv1.length());
        String ChkValue = null;

        SecureLink sl = KeyPair.checkPrivateKey(KeyPair.noCardMerId);
        ChkValue = sl.Sign(MerId + TransDate + TransType + OrderId + RefundAmount + Priv1);

        RefundBean refund = new RefundBean();
        refund.setMerId(MerId);
        refund.setOrdId(OrderId);
        refund.setRefundAmount(RefundAmount);
        refund.setTransDate(TransDate);
        refund.setTransType(TransType);
        refund.setVersion(Version);
        refund.setReturnUrl(ReturnURL);
        refund.setPriv1(Priv1);
        refund.setChkValue(ChkValue);

        String httpType = "SSL";
        String timeOut = "60000";
        Map<String, Object> reps = sendHttpMsg(KeyPair.properties.getProperty("chinapay.refund.url"), refund.getQueryString(), httpType, timeOut);
        logger.debug(remark + "{}", reps);
        /**
         * 在收到报文之后，可对报文数据进行验签。
         * 
         */
        boolean isSuccess = attestation(reps);
        String ResponseCode = (String) reps.get("ResponseCode");
        String Status = (String) reps.get("Status");

        EpayBean paymentBean = new EpayBean();
        boolean returnStatus = false;
        if (StringUtil.equals(Status, "1") || isSuccess || StringUtil.equals(Status, "3")) {
            paymentBean.setSuccessTrade(true);
            returnStatus = true;
        } else {
            paymentBean.setSuccessTrade(false);
        }
        paymentBean.setClientIp(refundBean.getClientIp());
        paymentBean.setPaymentPlatform(EpayPlatform.CHINAPAY);
        paymentBean.setRemark("【" + EpayPlatform.CHINAPAY.getPlatformName() + "】" + remark);
        paymentBean.setAmount(new BigDecimal(refundBean.getRefundAmount()));
        paymentBean.setSerialNumber(pt.getSerialNumber());
        paymentBean.setUserId(refundBean.getUserId());
        epayService.afterRefund(paymentBean);
        return returnStatus;
    }

    public boolean attestation(Map<String, Object> reps) {
        logger.info("<====Receive RefundResultData Start!");
        // 订单数据准备
        String ResCode = "" + reps.get("ResponseCode");
        String MerId = "" + reps.get("MerID");
        String OrdId = "" + reps.get("OrderId");
        String TransType = "" + reps.get("TransType");
        String RefundAmout = "" + reps.get("RefundAmout");
        String ProcessDate = "" + reps.get("ProcessDate");
        String SendTime = "" + reps.get("SendTime");
        String Status = "" + reps.get("Status");
        String Priv1 = "" + reps.get("Priv1");
        String CheckValue = "" + reps.get("CheckValue");

        //ResponseCode为0，且Status为3或8的时候，需要对chinapay返回数据进行验签。
        if (ResCode.equals("0") && (Status.equals(STATUS_REFUND_SUCC) || Status.equals(STATUS_REFUND_FAIL))) {
            try {
                SecureLink sl = KeyPair.checkPrivateKey(KeyPair.noCardMerId);
                String plainData = MerId + ProcessDate + TransType + OrdId + RefundAmout + Status + Priv1;
                return sl.verifyAuthToken(plainData, CheckValue);
            } catch (Exception e) {
                logger.error("", e);
                return false;
            }
        }
        return false;

    }

    /**
     * 发送http post报文，并且接受响应信息
     * 
     * @param strMsg 需要发送的交易报文,格式遵循httppost参数格式
     * @return String 服务器返回响应报文,如果处理失败，返回空字符串
     */
    private Map<String, Object> sendHttpMsg(String URL, String strMsg, String httpType, String timeOut) throws EpayException {
        CPHttpConnection httpSend = null;
        if (httpType.equals("SSL")) {
            httpSend = new HttpSSL(URL, timeOut);
        } else {
            httpSend = new Http(URL, timeOut);
        }
        // 设置获得响应结果的限制
        httpSend.setLenType(0);
        // 设置字符编码
        httpSend.setMsgEncoding("GBK");
        int returnCode = httpSend.sendMsg(strMsg);
        if (returnCode == 1) {
            try {
                String returnMsg = StringUtil.trim(new String(httpSend.getReceiveData(), "GBK"));
                returnMsg = StringUtil.trim(StringUtil.substringAfterLast(returnMsg, "<body>"));
                returnMsg = StringUtil.trim(StringUtil.substringBefore(returnMsg, "</body>"));
                Map<String, Object> m = ServletUtils.getUrlParams(returnMsg);
                return m;
            } catch (UnsupportedEncodingException e) {
                throw new EpayException("[getReceiveData Error!]");
            }
        } else {
            throw new EpayException(new StringBuffer("报文处理失败,失败代码=[").append(returnCode).append("]").toString());
        }
    }

    /**
     * 查询退款状态
     * @author songlin
     * @param paymentTradeNo
     * @param remark
     * @param uid
     * @return
     */
    public boolean queryRefundStatus(String paymentTradeNo, String remark, String uid) throws EpayException {
        PaymentTrade pt = null;
        List<PaymentTrade> list = paymentTradeService.findRefunding(paymentTradeNo, uid);
        if (ListUtil.isEmpty(list))
            return false;
        //FIXME 多次退款的情况需要另外考虑
        pt = list.get(0);
        // 查询订单数据准备
        String MerId = KeyPair.noCardMerId;
        String Version = "20060831";
        String OrderId = pt.getSerialNumber();
        String TransDate = DateUtil.dateToString(pt.getTransDate(), DateUtil.DateFormatter.FORMAT_YYYYMMDD);// 8
        String TransType = KeyPair.TransType_1;// 4
        String Resv = "";
        /*
         * try { Priv1 = Base64Util.base64Decoder(Priv1); } catch (Exception e1)
         * { // TODO Auto-generated catch block e1.printStackTrace(); }
         */
        String ChkValue = null;

        SecureLink sl = KeyPair.checkPrivateKey(MerId);

        ChkValue = sl.Sign(MerId + TransDate + OrderId + TransType);

        QueryBean query = new QueryBean();
        query.setMerId(MerId);
        query.setOrdId(OrderId);
        query.setTransDate(TransDate);
        query.setTransType(TransType);
        query.setVersion(Version);
        query.setResv(Resv);
        query.setChkValue(ChkValue);

        String httpType = "";// "SSL";
        String timeOut = "60000";
        Map<String, Object> reps = sendHttpMsg(KeyPair.properties.getProperty("chinapay.query.url"), query.getQueryString(), httpType, timeOut);
        logger.debug(remark + "{}", reps);
        /**
         * 在收到报文之后，可对报文数据进行验签。
         * 
         */
        boolean isSuccess = attestation(reps);
        String ResponseCode = (String) reps.get("ResponseCode");
        String Status = (String) reps.get("Status");
        if (StringUtil.equals(Status, "1") || isSuccess || StringUtil.equals(Status, "3")) {
            /**
             * 退款成功后保存交易
             */
            //            PaymentTrade paymentTrade = new PaymentTrade();
            //            //            paymentTrade.setAmount(new BigDecimal(amount));
            //            paymentTrade.setSerialNumber(pt.getSerialNumber());
            //            paymentTrade.setUserId(uid);
            //            paymentTrade.setTransDate(DateUtil.newInstanceDate());
            //            paymentTrade.setStatus(PaymentTradeStatusEnum.TUIKUANZHONG.getStatus());
            //            paymentTrade.setType(PaymentTradeTypeEnum.TUIKUAN.getType());
            //            paymentTrade.setRemark("【中国银联在线】" + remark + PaymentTradeStatusEnum.TUIKUANZHONG.getRemark());
            //            paymentTradeService.save(paymentTrade);
            return true;
        } else {
            //            PaymentTrade paymentTrade = new PaymentTrade();
            //            //            paymentTrade.setAmount(new BigDecimal(amount));
            //            paymentTrade.setSerialNumber(pt.getSerialNumber());
            //            paymentTrade.setUserId(uid);
            //            paymentTrade.setStatus(PaymentTradeStatusEnum.SHIBAI.getStatus());
            //            paymentTrade.setType(PaymentTradeTypeEnum.TUIKUAN.getType());
            //            paymentTrade.setRemark("【中国银联在线】" + remark + PaymentTradeStatusEnum.SHIBAI.getRemark());
            //            paymentTradeService.save(paymentTrade);
            return false;
        }
    }

}
