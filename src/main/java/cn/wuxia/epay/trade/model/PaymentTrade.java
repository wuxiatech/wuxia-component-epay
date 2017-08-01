package cn.wuxia.epay.trade.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.wuxia.common.entity.ValidationEntity;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.epay.enums.EpayPlatform;
import cn.wuxia.epay.trade.enums.PaymentTradeStatusEnum;
import cn.wuxia.epay.trade.enums.PaymentTradeTypeEnum;

/**
 * [ticket id] 支付交易
 * 
 * @author songlin @ Version : V<Ver.No> <5 Jun, 2014>
 */
@Entity
@Table(name = "payment_trade_detail")
public class PaymentTrade extends ValidationEntity {

    private static final long serialVersionUID = 3370031973837448641L;

    private String id;

    private String serialNumber;

    private String paymentNumber;

    private BigDecimal amount;

    private PaymentTradeTypeEnum type;

    private PaymentTradeStatusEnum status;

    private String remark;

    private String userId;

    private Timestamp transDate;

    private EpayPlatform paymentPlatform;

    public PaymentTrade() {
        super();
    }

    @GenericGenerator(name = "hibernate-uuid", strategy = "cn.wuxia.common.entity.Base64UuidGenerator")
    @GeneratedValue(generator = "hibernate-uuid")
    @Column(name = "ID", unique = true, nullable = false)
    @Id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = StringUtil.isNotBlank(id) ? id : null;
    }

    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "SERIAL_NUMBER")
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Enumerated(EnumType.STRING)
    public PaymentTradeStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PaymentTradeStatusEnum status) {
        this.status = status;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    public PaymentTradeTypeEnum getType() {
        return type;
    }

    public void setType(PaymentTradeTypeEnum type) {
        this.type = type;
    }

    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "TRANS_DATE")
    public Timestamp getTransDate() {
        return transDate;
    }

    public void setTransDate(Timestamp transDate) {
        this.transDate = transDate;
    }

    @Column(name = "PAYMENT_NUMBER")
    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    @Column(name = "PAYMENT_PLATFORM")
    @Enumerated(EnumType.STRING)
    public EpayPlatform getPaymentPlatform() {
        return paymentPlatform;
    }

    public void setPaymentPlatform(EpayPlatform paymentPlatform) {
        this.paymentPlatform = paymentPlatform;
    }

}
