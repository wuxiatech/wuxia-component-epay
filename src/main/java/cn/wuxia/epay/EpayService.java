/*
* Created on :Jan 8, 2015
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 www.ibmall.cn All right reserved.
*/
package cn.wuxia.epay;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.NumberUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.epay.bean.EpayBean;
import cn.wuxia.epay.bean.EpayRefundBean;
import cn.wuxia.epay.chinapay.service.ChinaPayService;
import cn.wuxia.epay.epaylinks.EpaylinksService;
import cn.wuxia.epay.trade.enums.PaymentTradeStatusEnum;
import cn.wuxia.epay.trade.model.PaymentTrade;
import cn.wuxia.epay.trade.service.PaymentTradeService;

@Service
public abstract class EpayService {

    @Autowired
    private ChinaPayService chinapayService;

    @Autowired
    private EpaylinksService epaylinksService;

    @Autowired
    private PaymentTradeService paymentTradeService;

    /**
     * 支付前操作
     * @author songlin
     * @param paymentTrade
     */
    public abstract void beforePayment(EpayBean paymentBean);

    /**
     * 订单完成后的操作
     * @author songlin
     * @param paymentTrade
     * @param orderNo
     * @throws EpayException
     */
    public abstract boolean afterPayment(EpayBean paymentBean) throws EpayException;

    /**
     * 退款前操作
     * @author songlin
     * @param paymentTrade
     */
    public PaymentTrade beforeRefund(EpayRefundBean refundBean) throws EpayException {
        PaymentTrade pt = null;
        if (StringUtil.isNotBlank(refundBean.getSerialNumber())) {
            List<PaymentTrade> lists = paymentTradeService.findBySerialNoAndStatus(refundBean.getSerialNumber(), PaymentTradeStatusEnum.CHENGGONG);
            if (ListUtil.isNotEmpty(lists)) {
                pt = lists.get(0);
            }
        } else {
            throw new EpayException("交易流水号不能为空");
        }
        if (pt == null) {
            throw new EpayException("交易流水数据不正确");
        }
        //支付金额
        if (!NumberUtil.isNumber(refundBean.getRefundAmount())) {
            throw new EpayException("申请退款金额无效");
        }
        return pt;
    }

    /**
     * 退款完成后的操作
     * @author songlin
     * @param paymentTrade
     * @param orderNo
     * @throws EpayException
     */
    public abstract void afterRefund(EpayBean paymentBean) throws EpayException;

    /**
     * 退款简便操作
     * @author songlin
     * @param serialNumber
     * @param remark
     * @return
     */
    public boolean refund(String serialNumber, String remark) throws EpayException {
        return refund(new EpayRefundBean(serialNumber, remark));
    }

    /**
     * 退款操作
     * @author songlin
     * @param paymentBean
     * @throws EpayException
     */
    public boolean refund(EpayRefundBean refundBean) throws EpayException {
        PaymentTrade pt = beforeRefund(refundBean);
        switch (pt.getPaymentPlatform()) {
            case CHINAPAY:
                return chinapayService.refund(refundBean, pt);
            case EPAYLINKS:
                return epaylinksService.refund(refundBean, pt);
            default:
                break;

        }
        return false;
    }
}
