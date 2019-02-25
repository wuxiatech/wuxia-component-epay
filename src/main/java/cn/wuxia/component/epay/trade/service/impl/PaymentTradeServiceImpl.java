package cn.wuxia.component.epay.trade.service.impl;

import java.math.BigDecimal;
import java.util.List;

import cn.wuxia.component.epay.trade.dao.PaymentTradeDao;
import cn.wuxia.component.epay.trade.enums.PaymentTradeStatusEnum;
import cn.wuxia.component.epay.trade.model.PaymentTrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wuxia.common.exception.AppServiceException;
import cn.wuxia.common.exception.ValidateException;
import cn.wuxia.common.sensitive.ValidtionSensitiveUtil;
import cn.wuxia.component.epay.trade.service.PaymentTradeService;

@Service
@Transactional
public class PaymentTradeServiceImpl implements PaymentTradeService {
    protected final Logger logger = LoggerFactory.getLogger("epay");

    @Autowired
    private PaymentTradeDao paymentTradeDao;

    @Override
    public PaymentTrade save(PaymentTrade e) {
        try {
            e.validate();
            ValidtionSensitiveUtil.validate(e);
        } catch (ValidateException e1) {
            throw new AppServiceException("", e1);
        }
        paymentTradeDao.save(e);
        return e;
    }

    @Override
    public List<PaymentTrade> findBySerialNoAndStatus(String serialNo, PaymentTradeStatusEnum status) {
        return paymentTradeDao.findBySerialNoAndStatus(serialNo, status);
    }

    @Override
    public List<PaymentTrade> findSuccessRefund(String userId) {
        return paymentTradeDao.findByStatus(PaymentTradeStatusEnum.TUIKUANCHENGGONG, userId);
    }

    @Override
    public List<PaymentTrade> findSuccessPay(String userId) {
        return paymentTradeDao.findByStatus(PaymentTradeStatusEnum.CHENGGONG, userId);
    }

    @Override
    public List<PaymentTrade> findSuccessTrans(String userId) {
        return paymentTradeDao.findByUserId(userId);
    }

    @Override
    public List<PaymentTrade> findSerialNoAndUser(String serialNo, String userId) {
        return paymentTradeDao.findSerialNoAndUser(serialNo, userId);
    }

    @Override
    public List<PaymentTrade> findRefunding(String serialNo, String userId) {
        return paymentTradeDao.findByStatus(PaymentTradeStatusEnum.TUIKUANZHONG, userId, serialNo);
    }

    @Override
    public BigDecimal findRefundAmount(String userId, String serialNo) {
        return paymentTradeDao.findTotalRefundAmount(userId, serialNo);
    }

}
