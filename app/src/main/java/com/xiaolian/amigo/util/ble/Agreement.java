package com.xiaolian.amigo.util.ble;

import com.xiaolian.amigo.util.Log;

import java.math.BigDecimal;
import java.util.Random;

/**
 *
 * Created by caidong on 2017/9/28.
 */
public class Agreement {

    private DES des = new DES();
    private static String TAG = "HnhAgreementImpl";

    private static Agreement agreement;

    public static Agreement getInstance() {
        if (null == agreement) {
            agreement = new Agreement();
        }
        return agreement;
    }

    public String createConnection() {
        String factor = "";
        String t = Integer.toHexString(new Random().nextInt(999999999));
        for (int i = 0; i < 16; i++) {
            if (t.length() >= i + 1) {
                factor += t.substring(i, i + 1);
            } else {
                factor += "0";
            }
        }
        FACTOR = factor;
        HEX = "A70108" + FACTOR;
        HEX = HEX + HexUtils.HexstrSum(HEX);
        return HEX;
    }

    private String SYCS = null;
    private String FACTOR = "0000000000000000"; //"AAABCDDEEFADABBB" ;
    public String KEY = null;
    private String YEHEX;

    public void InitKey(String Hex, String key) {
        SYCS = Hex.substring(10, 14);
        String temp = des.des_code(0, FACTOR, Hex.substring(6, 22));
        KEY = des.des_code(0, temp, key);
    }

    private String TEMPKEY = null;
    private String HEX = null;

    public String setBalance(String orderId, double money) {
        TEMPKEY = des.des_code(0, orderId + HexUtils.moneyToHexstr(money)
                + SYCS, KEY);
        HEX = "A70208" + TEMPKEY;
        HEX = HEX + HexUtils.HexstrSum(HEX);
        Log.i(TAG, "设备使用次数：" + SYCS);
        return HEX;
    }

    public String preCheckout(String orderId) {
        String temp = des.des_code(0, orderId + "0000" + SYCS, KEY);
        HEX = "A70808" + temp;
        HEX = HEX + HexUtils.HexstrSum(HEX);
        return HEX;
    }

    public String CloseValve() {
        HEX = "A70408" + TEMPKEY;
        HEX = HEX + HexUtils.HexstrSum(HEX);
        return HEX;
    }

    public String SetRate(String rate) {
        HEX = "A70508" + des.des_code(0, rate, KEY);
        HEX = HEX + HexUtils.HexstrSum(HEX);
        return HEX;
    }

    public String Checkout(String orderId) {
        String temp = des.des_code(0, orderId + YEHEX + SYCS, KEY);
        HEX = "A70708" + temp;
        HEX = HEX + HexUtils.HexstrSum(HEX);
        return HEX;
    }

    public BigDecimal getYE(String data) {
        String ye_hex = data.substring(data.length() - 18, data.length() - 2);
        YEHEX = ye_hex.substring(8, 12);
        String result = new DES().des_code(1, ye_hex, KEY);
        String res = Integer.valueOf(result.substring(8, 12), 16).toString();
        BigDecimal ye = new BigDecimal(res);
        BigDecimal a = new BigDecimal(100);
        ye = ye.divide(a, 2, BigDecimal.ROUND_HALF_UP);
        return ye;
    }

}
