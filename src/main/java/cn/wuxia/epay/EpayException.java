/*
* Created on :29 Aug, 2014
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 www.ibmall.cn All right reserved.
*/
package cn.wuxia.epay;

public class EpayException extends Exception {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 2714496706512845530L;

    public EpayException() {
        super();
    }

    public EpayException(Exception e) {
        super("", e);
    }

    public EpayException(String message, Exception e) {
        super(message, e);
    }

    public EpayException(String message) {
        super(message);
    }
}
