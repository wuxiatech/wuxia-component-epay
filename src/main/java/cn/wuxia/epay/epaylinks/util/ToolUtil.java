package cn.wuxia.epay.epaylinks.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import cn.wuxia.common.exception.AppServiceException;
import cn.wuxia.common.util.NumberUtil;

public class ToolUtil {

    /**
     * 把对象转换成字符串
     * @param obj
     * @return String 转换成字符串,若对象为null,则返回空字符串.
     */
    public static String toString(Object obj) {
        if (obj == null)
            return "";

        return obj.toString();
    }

    /**
     * 把对象转换为int数值.
     * 
     * @param obj
     *            包含数字的对象.
     * @return int 转换后的数值,对不能转换的对象返回0。
     */
    public static int toInt(Object obj) {
        int a = 0;
        try {
            if (obj != null)
                a = Integer.parseInt(obj.toString());
        } catch (Exception e) {

        }
        return a;
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     * @return String
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    /**
     * 获取当前日期 yyyyMMdd
     * @param date
     * @return String
     */
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String strDate = formatter.format(date);
        return strDate;
    }

    /**
     * 取出一个指定长度大小的随机正整数.
     * 
     * @param length
     *            int 设定所取出随机数的长度。length小于11
     * @return int 返回生成的随机数。
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * 获取编码字符集
     * @param request
     * @return String
     */
    public static String getCharacterEncoding(HttpServletRequest request) {

        if (null == request) {
            return "gbk";
        }

        String enc = request.getCharacterEncoding();
        if (null == enc || "".equals(enc)) {
            enc = "gbk";
        }

        return enc;
    }

    /**
     * 获取unix时间，从1970-01-01 00:00:00开始的秒数
     * @param date
     * @return long
     */
    public static long getUnixTime(Date date) {
        if (null == date) {
            return 0;
        }

        return date.getTime() / 1000;
    }

    /**
     * 时间转换成字符串
     * @param date 时间
     * @param formatType 格式化类型
     * @return String
     */
    public static String date2String(Date date, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.format(date);
    }

    /**
     * 转换为中国银联在线的固定金额格式 将12.00, 10000 转换为 12.00, 10000.00
     * 
     * @author songlin
     * @param m
     * @return
     */
    public static String encodeTransAmount(String m) {
        DecimalFormat df = new DecimalFormat("##0.00#");
        String source = df.format(NumberUtil.toDouble(m));

        if (source.length() > 12) {
            throw new AppServiceException("超出最大交易额");
        }
        return source;
    }
}
