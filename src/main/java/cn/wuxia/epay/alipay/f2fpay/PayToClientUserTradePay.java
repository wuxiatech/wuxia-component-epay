package cn.wuxia.epay.alipay.f2fpay;

import cn.wuxia.common.util.NumberUtil;
import cn.wuxia.epay.alipay.common.MyException;
import cn.wuxia.epay.alipay.config.AlipayConfig;
import cn.wuxia.epay.alipay.factory.AlipayAPIClientFactory;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import org.springframework.util.Assert;

/**
 * 转账给客户支付宝
 */
public class PayToClientUserTradePay {

    /**
     * 返回支付宝的订单号
     * @param outOrderNo
     * @param buyerAlipayAccount
     * @param amount
     * @param payerName
     * @param buyerName
     * @param remark
     * @return
     * @throws MyException
     */
    public static String transfer(String outOrderNo, String buyerAlipayAccount, double amount, String payerName, String buyerName, String remark)
            throws MyException {
        Assert.notNull(outOrderNo, "订单号不能为空");
        Assert.notNull(buyerAlipayAccount, "收款人账号不能为空");
        Assert.isTrue(amount > 0.1, "退款金额不能大于0.1元");
        String amountStr = NumberUtil.formatFinancing(amount);
        Assert.isTrue(amountStr.length() < 19, "退款金额大于最大退款额度");
        AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizContent("{" + "\"out_biz_no\":\"" + outOrderNo + "\"," + "\"payee_type\":\"ALIPAY_LOGONID\"," + "\"payee_account\":\""
                + buyerAlipayAccount + "\"," + "\"amount\":\"" + amountStr + "\"," + "\"payer_show_name\":\"" + payerName + "\","
                + "\"payee_real_name\":\"" + buyerName + "\"," + "\"remark\":\"" + remark + "\"" + "}");
        try {
            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return response.getOrderId();
            } else {
                System.out.println("调用失败"+response.getSubMsg());
                throw new MyException(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            throw new MyException("转账失败", e);
        }
    }

    public static void query(String outOrderNo, String orderId) throws MyException {
        Assert.notNull(outOrderNo, "订单号不能为空");
        Assert.notNull(orderId, "收款人账号不能为空");
        AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();

        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
        request.setBizContent("{" + "    \"out_biz_no\":\"3142321423432\"," + "    \"order_id\":\"20160627110070001502260006780837\"" + "  }");
        try {
            AlipayFundTransOrderQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
        } catch (AlipayApiException e) {
        }
    }

}
