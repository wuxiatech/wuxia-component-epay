/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package cn.wuxia.epay.alipay.executor;

import java.util.Map;

import cn.wuxia.epay.alipay.common.MyException;
import cn.wuxia.epay.alipay.util.AlipayMsgBuildUtil;

/**
 * 默认执行器(该执行器仅发送ack响应)
 * 
 * @author baoxing.gbx
 * @version $Id: InAlipayDefaultExecutor.java, v 0.1 Jul 30, 2014 10:22:11 AM baoxing.gbx Exp $
 */
public class InAlipayDefaultExecutor implements ActionExecutor {

    /** 业务参数 */
    private Map bizContent;

    public InAlipayDefaultExecutor(Map bizContent) {
        this.bizContent = bizContent;
    }

    public InAlipayDefaultExecutor() {
        super();
    }

    /**
     * 
     * @see com.alipay.executor.ActionExecutor#execute()
     */
    @Override
    public String execute() throws MyException {

        //取得发起请求的支付宝账号id
        final String fromUserId = (String)bizContent.get("FromUserId");

        return AlipayMsgBuildUtil.buildBaseAckMsg(fromUserId);
    }
}
