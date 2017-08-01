/*
 * Created on :30 May, 2014 Author :songlin Change History Version Date Author
 * Reason <Ver.No> <date> <who modify> <reason>
 */
package cn.wuxia.epay.chinapay;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import cn.wuxia.common.exception.AppServiceException;
import cn.wuxia.common.exception.ServiceException;
import cn.wuxia.common.util.DateUtil;
import cn.wuxia.common.util.FileUtil;
import cn.wuxia.common.util.NumberUtil;
import cn.wuxia.common.util.PropertiesUtils;

public class KeyPair {

    public static final String version_2004 = "20040916";

    public static final String version_2007 = "20070129";

    public static final String version_2008 = "20080515";

    public static final String version_2010 = "20100304";

    public static final String TransType_1 = "0001";

    public static final String TransType_2 = "0002";

    public static final String Cury_RMB = "156";

    public static final String TransSuccess = "1001";

    public static final Properties properties = PropertiesUtils.loadProperties("classpath:chinapay.config.properties");

    private static final PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

    private static final String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();

    public static final String b2cMerId = properties.getProperty("chinapay.b2cmerid");
    
    public static final String noCardMerId = properties.getProperty("chinapay.nocardmerid");;
    /**
     * 
     * @author songlin
     * @param pkpath
     * @return
     */
    private static String pkFile2Temp(String pkpath) {
        File f = new File(path + File.separator + pkpath);
        if (!f.exists()) {
            try {
                Resource[] resources = patternResolver.getResources("classpath*:" + pkpath);
                if (resources != null && resources.length > 0) {
                    FileUtil.copyInputStreamToFile(resources[0].getInputStream(), f);
                }
            } catch (IOException e) {
                FileUtil.deleteQuietly(f);
                throw new ServiceException("", e);
            }
        }
        return f.getPath();
    }

    /**
     * @author songlin
     * @param merId
     * @return
     * @throws ServiceException
     */
    public static chinapay.SecureLink checkPrivateKey(final String merId) throws ServiceException {
        chinapay.PrivateKey key1 = new chinapay.PrivateKey();
        String prkPath = "keys/MerPrK_" + merId + ".key";
        prkPath = pkFile2Temp(prkPath);
        boolean flag = key1.buildKey(merId, 0, prkPath);
        if (!flag) {
            throw new ServiceException("build private key error! check file " + prkPath);
        }
        return new chinapay.SecureLink(key1);
    }

    /**
     * @author songlin
     * @param merId
     * @return
     * @throws ServiceException
     */
    public static chinapay.SecureLink checkPublicKey() throws ServiceException {
        chinapay.PrivateKey key1 = new chinapay.PrivateKey();
        String prkPath = "keys/PgPubk.key";
        prkPath = pkFile2Temp(prkPath);
        boolean flag = key1.buildKey("999999999999999", 0, prkPath);
        if (!flag) {
            throw new ServiceException("build public key error! check file " + prkPath);
        }
        return new chinapay.SecureLink(key1);
    }

    /**
     * 2004版本获取订单代码16位数字， 年份后两位(yy)+月份(MM)+MerId(11-15)+日时(ddHH)+随机3位数,
     * 如：1406049250449697(14-06-04925-04-4-9697),
     * 1406049250414967(14-06-04925-04-14-967)
     * <p>
     * 订单号从第5位到第9位必须和商户号的第11位到第15位相同
     * </p>
     */
    public static synchronized String generateOrderNo(final String merId) {
        Date d = DateUtil.newInstanceDate();
        String orderPrefix = StringUtils.substring("" + DateUtil.getYear(d), 2)
                + ((DateUtil.getMonth(d) < 10) ? "0" + DateUtil.getMonth(d) : "" + DateUtil.getMonth(d));
        String orderMerId = StringUtils.substring(merId, 10, 15);
        String orderPostfix = DateUtil.getDay(d) < 10 ? "0" + DateUtil.getDay(d) : "" + DateUtil.getDay(d);
        int currentHour = DateUtil.getHour(d);
        String radomPostfix = currentHour < 10 ? (currentHour + "" + (new Random().nextInt(8999) + 1000)) : (currentHour + "" + (new Random()
                .nextInt(899) + 100));

        String result = orderPrefix + orderMerId + orderPostfix + radomPostfix;
        Assert.isTrue(result.length() == 16, "长度必须为16");
        return result;
    }

    /**
     * 其它版本生成订单号生成16位数字
     */
    public static synchronized String generateOrderNo() {
        String orderPrefix = DateUtil.dateToString(DateUtil.newInstanceDate(), DateUtil.DateFormatter.FORMAT_YYYYMMDDHHMMSS);
        orderPrefix = StringUtils.substring(orderPrefix, 2, orderPrefix.length());
        String radomPostfix = "" + (new Random().nextInt(8999) + 1000);
        String result = orderPrefix + radomPostfix;
        Assert.isTrue(result.length() == 16, "长度必须为16");
        return result;
    }

    /**
     * 转换为中国银联在线的固定金额格式 将12.00 转换为 000000001200
     * 
     * @author songlin
     * @param m
     * @return
     */
    public static String encodeTransAmount(String m) {
        DecimalFormat df = new DecimalFormat("##0.00#");
        String source = StringUtils.remove(df.format(NumberUtil.toDouble(m)), ".");

        if (source.length() > 12) {
            throw new AppServiceException("超出最大交易额");
        }
        while (source.length() < 12) {
            source = "0" + source;
        }

        if (source.length() != 12) {
            throw new AppServiceException("转换数字出错");
        }
        return source;
    }

    /**
     * 将000000001200 转换为 12.00
     * 
     * @author songlin
     * @param m
     * @return
     */
    public static String decodeTransAmount(String m) {
        String source = StringUtils.left(m, 10) + "." + StringUtils.right(m, 2);
        return "" + NumberUtil.toDouble(source);
    }

    public static void main(String[] args) {
        // b2c();
        // noCard();
        // publicKey();
        System.out.println(encodeTransAmount("40"));
        System.out.println(decodeTransAmount("000000000900"));
        System.out.println(decodeTransAmount(encodeTransAmount("40")));
        System.out.println(generateOrderNo(noCardMerId));
        System.out.println(generateOrderNo());
    }
}
