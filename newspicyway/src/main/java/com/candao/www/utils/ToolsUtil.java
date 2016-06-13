package com.candao.www.utils;

import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: 张吉超 johnson
 * Date: 7/17/15
 * Time: 5:44 下午
 */
public class ToolsUtil {

    /**
     * 将数据格式成两位小数
     *
     * @param data
     * @return
     */
    public static String formatTwoDecimal(String data) {
        if (StringUtils.isBlank(data) || StringUtils.equals(data, "null")) {
            return "";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(Double.parseDouble(data));
    }

    /**
     * 4长度byte数组转int
     *
     * @param res
     * @return
     */
    public static int byte2int(byte[] res) {
        // 一个byte数据左移24位变成0x??000	000，再右移8位变成0x00??0000

        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }

    /**
     * int转为4个长度的byte
     *
     * @param res
     * @return
     */
    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];

        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }
}
