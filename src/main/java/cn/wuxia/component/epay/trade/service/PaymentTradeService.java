/**
 * 
 */
package cn.wuxia.component.epay.trade.service;

import java.math.BigDecimal;
import java.util.List;

import cn.wuxia.component.epay.trade.enums.PaymentTradeStatusEnum;
import cn.wuxia.component.epay.trade.model.PaymentTrade;

/**
 * [ticket id] Description of the class
 * 
 * @author songlin @ Version : V<Ver.No> <5 Jun, 2014>
 */

public interface PaymentTradeService {
    public PaymentTrade save(PaymentTrade e);

    /**
     * 查找某个状态的交易记录
     * @author songlin
     * @return
     */
    public List<PaymentTrade> findBySerialNoAndStatus(String serialNo, PaymentTradeStatusEnum status);

    /**
     * 查找个人成功付款和成功退款的交易
     * 
     * @author songlin
     * @return
     */
    public List<PaymentTrade> findSuccessTrans(String userId);

    /**
     * 查找个人成功付款的交易
     * 
     * @author songlin
     * @return
     */
    public List<PaymentTrade> findSuccessPay(String userId);

    /**
     * 查找个人成功退款的交易
     * 
     * @author songlin
     * @return
     */
    public List<PaymentTrade> findSuccessRefund(String userId);

    /**
     * 根据用户查找成功交易的数据
     * @author songlin
     * @param serialNo
     * @param userId
     * @return
     */
    public List<PaymentTrade> findSerialNoAndUser(String serialNo, String userId);

    /**
     * 根据用户退款中的数据
     * @author songlin
     * @param serialNo
     * @param userId
     * @return
     */
    public List<PaymentTrade> findRefunding(String serialNo, String userId);

    /**
     * 查找用户已退款的总额
     * @author songlin
     * @return
     */
    public BigDecimal findRefundAmount(String userId, String serialNo);
}
