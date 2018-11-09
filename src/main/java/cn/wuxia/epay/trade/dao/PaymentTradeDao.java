package cn.wuxia.epay.trade.dao;

import java.math.BigDecimal;
import java.util.List;

import cn.wuxia.common.hibernate.dao.BasicHibernateDao;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wuxia.epay.trade.enums.PaymentTradeStatusEnum;
import cn.wuxia.epay.trade.model.PaymentTrade;

/**
 * [ticket id] 充值交易
 * 
 * @author songlin @ Version : V<Ver.No> <10 Jun, 2014>
 */
@Component
public class PaymentTradeDao extends BasicHibernateDao<PaymentTrade, String> {
    /**
     * 不同的数据源使用不同的sessionFactory(名字不同)
     */
    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<PaymentTrade> findBySerialNoAndStatus(String serialNo, PaymentTradeStatusEnum status) {
        return find(Restrictions.eq("status", status), Restrictions.eq("serialNumber", serialNo));
    }

    public List<PaymentTrade> findByStatus(PaymentTradeStatusEnum status) {
        return findBy("status", status);
    }

    public List<PaymentTrade> findByStatus(PaymentTradeStatusEnum status, String userId) {
        return find("status", status, Restrictions.eq("userId", userId));
    }

    public List<PaymentTrade> findByStatus(PaymentTradeStatusEnum status, String userId, String serialNo) {
        return find("status", status, Restrictions.eq("userId", userId), Restrictions.eq("serialNumber", serialNo));
    }

    public List<PaymentTrade> findByUserId(String userId) {
        return find(Restrictions.or(Restrictions.eq("status", PaymentTradeStatusEnum.CHENGGONG),
                Restrictions.eq("status", PaymentTradeStatusEnum.TUIKUANCHENGGONG)), Restrictions.eq("userId", userId));
    }

    public List<PaymentTrade> findSerialNoAndUser(String serialNo, String userId) {
        return find(Restrictions.eq("serialNumber", serialNo), Restrictions.eq("userId", userId), Restrictions
                .or(Restrictions.eq("status", PaymentTradeStatusEnum.CHENGGONG), Restrictions.eq("status", PaymentTradeStatusEnum.TUIKUANCHENGGONG)));
    }

    public BigDecimal findTotalRefundAmount(String userId, String serialNumber) {
        String hql = "select sum(amount) from PaymentTrade where (status=? or status=?) and userId=? and serialNumber=?";
        return (BigDecimal) createQuery(hql, PaymentTradeStatusEnum.TUIKUANZHONG, PaymentTradeStatusEnum.TUIKUANCHENGGONG, userId, serialNumber)
                .uniqueResult();
    }
}
